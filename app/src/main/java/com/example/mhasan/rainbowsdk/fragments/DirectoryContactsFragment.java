package com.example.mhasan.rainbowsdk.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.DirectoryContact;
import com.ale.infra.contact.EmailAddress;
import com.ale.infra.contact.IContactSearchListener;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.proxy.users.IUserProxy;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ContactData;
import com.example.mhasan.rainbowsdk.activites.ContactDetails;
import com.example.mhasan.rainbowsdk.adapters.ContactsAdapter;
import com.example.mhasan.rainbowsdk.adapters.DirectoryContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhasan on 7/25/2017.
 *
 */

public class DirectoryContactsFragment extends Fragment implements DirectoryContactsAdapter.OnItemClickListener {
    private RecyclerView mContactRV;
    private DirectoryContactsAdapter mContactAD;
    ArrayList<DirectoryContact> mContactList;
    private IContactSearchListener m_changeListener = new IContactSearchListener() {

        @Override
        public void searchStarted() {

        }

        @Override
        public void searchFinished(List<DirectoryContact> list) {
            mContactList.clear();
            int size= list.size();
            for(int i=0;i<size;i++){
                DirectoryContact contact = list.get(i);
                mContactList.add(contact);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mContactAD.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void searchError() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContactList = new ArrayList<>();
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_directory_contacts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactRV = getActivity().findViewById(R.id.directoryContactList);
        mContactAD=new DirectoryContactsAdapter(getContext(),mContactList);
        mContactAD.setOnItemClickedListener(this);
        mContactRV.setAdapter(mContactAD);
        mContactRV.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RainbowSdk.instance().contacts().searchByName(newText, m_changeListener);
                return false;
            }
        });

    }

    @Override
    public void onItemClicked(int position) {
        String corporateId=mContactList.get(position).getCorporateId();
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
        intent.putExtra("ContactData", new ContactData(fullName, jobTitle, profilePic, presence.name(), contactEmails,contactPhones,isRoster));

        startActivity(intent);
    }
}
