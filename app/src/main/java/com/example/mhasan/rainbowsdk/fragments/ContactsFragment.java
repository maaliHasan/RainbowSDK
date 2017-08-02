package com.example.mhasan.rainbowsdk.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ale.infra.contact.Contact;
import com.ale.infra.contact.EmailAddress;
import com.ale.infra.contact.PhoneNumber;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.proxy.users.IUserProxy;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ContactData;
import com.example.mhasan.rainbowsdk.activites.ContactDetails;
import com.example.mhasan.rainbowsdk.adapters.ContactsAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.mhasan.rainbowsdk.R.id.contactList;


/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ContactsFragment extends Fragment implements ContactsAdapter.OnItemClickListener {
    public static final String TAG = ContactsFragment.class.getSimpleName();
    private RecyclerView mContactRV;
    private ContactsAdapter mContactAD;
    ArrayList<Contact> mContactList;
    private Set<EmailAddress> mEmails;
    private Set<PhoneNumber> mPhones;
    private ProgressDialog pDialog;

    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContactAD.notifyDataSetChanged();
                }
            });
            mContactList.clear();
            ArrayItemList arrayItemList = RainbowSdk.instance().contacts().getRainbowContacts();
            int size = arrayItemList.getCount();
            for (int i = 0; i < size; i++) {
                Contact contact = (Contact) arrayItemList.get(i);
                mContactList.add(contact);
            }
            pDialog.dismiss();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContactList = new ArrayList<>();
        this.mEmails = new HashSet<>();
        this.mPhones = new HashSet<>();
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDialog();
        RainbowSdk.instance().contacts().getRainbowContacts().registerChangeListener(m_changeListener);
        mContactRV = getActivity().findViewById(contactList);
        mContactAD = new ContactsAdapter(getActivity(), mContactList);
        mContactAD.setOnItemClickedListener(this);
        mContactRV.setAdapter(mContactAD);
        mContactRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        RainbowSdk.instance().contacts().getRainbowContacts().unregisterChangeListener(m_changeListener);
        super.onDestroyView();
    }


    public void loadDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    public void onItemClicked(int position) {

        final String corporateId=mContactList.get(position).getCorporateId();
        RainbowSdk.instance().contacts().getUserDataFromId(corporateId, new IUserProxy.IGetUserDataListener() {
            @Override
            public void onSuccess(Contact contact) {
                getContactDetails(contact);
            }

            @Override
            public void onFailure(RainbowServiceException exception) {

            }
        });

    }
    public  void  getContactDetails(Contact contact){
        ArrayList<String> contactEmails = new ArrayList<>();
        ArrayList<String> contactPhones = new ArrayList<>();
        mEmails  = contact.getEmailAddresses();
        String id= contact.getCorporateId();
        String workEmail = contact.getEmailAddressForType(EmailAddress.EmailType.WORK);
        String homeEmail = contact.getEmailAddressForType(EmailAddress.EmailType.HOME);
        String OfficePhone=contact.getFirstAvailableNumber();
        String MobilePhone=contact.getFirstMobilePhoneNumber();
        String isRoster=String.valueOf(contact.isRoster());
        if (!workEmail.isEmpty()) {
            contactEmails.add(workEmail);
        }
        if (!homeEmail.isEmpty()) {
            contactEmails.add(homeEmail);
        }
        if (!OfficePhone.isEmpty()) {
            contactPhones.add(OfficePhone);
        }
        if (!MobilePhone.isEmpty()) {
            contactPhones.add(MobilePhone);
        }

        String fullName = contact.getFirstName() + " " + contact.getLastName();
        String jobTitle = contact.getJobTitle();
        Bitmap profilePic = contact.getPhoto();
        RainbowPresence presence = contact.getPresence();
        Intent intent = new Intent(getActivity(), ContactDetails.class);
        intent.putExtra("ContactData", new ContactData(fullName, jobTitle, profilePic, presence.name(), contactEmails,contactPhones,isRoster ,id));

        startActivity(intent);
    }
}
