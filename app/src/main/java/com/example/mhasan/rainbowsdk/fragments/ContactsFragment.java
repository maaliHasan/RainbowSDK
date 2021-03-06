package com.example.mhasan.rainbowsdk.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.EmailAddress;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ContactData;
import com.example.mhasan.rainbowsdk.activites.ContactDetails;
import com.example.mhasan.rainbowsdk.adapters.ContactsAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.ale.rainbowsdk.RainbowSdk.instance;
import static com.example.mhasan.rainbowsdk.R.id.contactList;


/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ContactsFragment extends Fragment implements ContactsAdapter.OnItemClickListener, IItemListChangeListener {
    public static final String TAG = ContactsFragment.class.getSimpleName();
    private RecyclerView mContactRV;
    private ContactsAdapter mContactAD;
    private ArrayList<Contact> mContactList;
    List<IRainbowContact> mPendingInvitationList;
    private ProgressDialog pDialog;

    private Contact.ContactListener m_contactListener = new Contact.ContactListener() {

        @Override
        public void contactUpdated(Contact contact) {
            ListIterator<Contact> iterator = mContactList.listIterator();
            while (iterator.hasNext()) {
                Contact next = iterator.next();
                if (next.equals(contact)) {
                    iterator.set(contact);

                }
            }
            updateAdapter();

        }

        @Override
        public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {
            Log.d(TAG, "onPresenceChanged: " + contact.getFirstName() + "  " + rainbowPresence.getPresence());
        }

        @Override
        public void onActionInProgress(boolean b) {

        }
    };
    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {

        @Override
        public void dataChanged() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mContactRV.getRecycledViewPool().clear();
                   // mContactAD.notifyDataSetChanged();
                    mContactAD.notifyItemRangeChanged(0, mContactList.size());
                }
            });
            mContactList.clear();
            ArrayItemList arrayItemList = instance().contacts().getRainbowContacts();
            int size = arrayItemList.getCount();
            for (int i = 0; i < size; i++) {
                Contact contact = (Contact) arrayItemList.get(i);
                contact.registerChangeListener(m_contactListener);
                mContactList.add(contact);
            }
         // pDialog.dismiss();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContactList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // loadDialog();
        instance().contacts().getRainbowContacts().registerChangeListener(m_changeListener);
        mContactRV = getActivity().findViewById(contactList);
        mContactRV.setItemAnimator(null);
        mContactAD = new ContactsAdapter(getActivity(), mContactList);
        mContactAD.setOnItemClickedListener(this);
        mContactRV.setAdapter(mContactAD);
        mContactRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        instance().contacts().getRainbowContacts().unregisterChangeListener(m_changeListener);
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
        final String corporateId = mContactList.get(position).getCorporateId();
        Contact currentContact = (Contact) RainbowSdk.instance().contacts().getContactFromCorporateId(corporateId);
        if (currentContact.isBot()) {
            Toast.makeText(getContext(), "This  is Bot Contact !", Toast.LENGTH_LONG).show();
        }
        getContactDetails(currentContact);
    }

    public void getContactDetails(Contact contact) {
        ArrayList<String> contactEmails = new ArrayList<>();
        ArrayList<String> contactPhones = new ArrayList<>();
        String id = contact.getCorporateId();
        String jId = contact.getContactId();
        String workEmail = contact.getEmailAddressForType(EmailAddress.EmailType.WORK);
        String homeEmail = contact.getEmailAddressForType(EmailAddress.EmailType.HOME);
        String OfficePhone = contact.getFirstAvailableNumber();
        String MobilePhone = contact.getFirstMobilePhoneNumber();
        String isRoster = String.valueOf(contact.isRoster());
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
        intent.putExtra("ContactData", new ContactData(fullName, jobTitle, profilePic, presence.getPresence(), contactEmails, contactPhones, isRoster, id, jId));
        startActivity(intent);
    }

    private void updateAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactAD.notifyDataSetChanged();

            }
        });
    }

    void getPendingSentInvitations() {
        List<IRainbowContact> pendingSentInvitations = RainbowSdk.instance().contacts().getPendingSentInvitations();
        Log.d(TAG, "getPendingSentInvitations: " + pendingSentInvitations.size());
        mPendingInvitationList = RainbowSdk.instance().contacts().getPendingReceivedInvitations();
        Log.d(TAG, "getPendingReceivedInvitations1: " + mPendingInvitationList.size());
        ArrayItemList<IRainbowContact> invitationSent = RainbowSdk.instance().contacts().getReceivedInvitations();
        Log.d(TAG, "getReceivedInvitations1: " + invitationSent.getCount());
        ArrayItemList<IRainbowContact> sentInvitation = RainbowSdk.instance().contacts().getSentInvitations();
        Log.d(TAG, "getSentInvitations1: " + sentInvitation);

    }

    @Override
    public void dataChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPendingSentInvitations();
            }
        });

    }


}
