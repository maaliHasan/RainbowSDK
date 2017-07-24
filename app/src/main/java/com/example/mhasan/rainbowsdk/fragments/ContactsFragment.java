package com.example.mhasan.rainbowsdk.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.contact.Contact;
import com.ale.infra.list.ArrayItemList;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.ContactsAdapter;

import java.util.ArrayList;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ContactsFragment extends Fragment {
    public static final String TAG = ContactsFragment.class.getSimpleName();
    private RecyclerView mContactRV;
    private ContactsAdapter mContactAD;
    ArrayList<Contact> mContactList;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContactList= new ArrayList<>();
        connectToRainbow();
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactRV = getActivity().findViewById(R.id.contactList);
        mContactAD = new ContactsAdapter(getActivity(),this.mContactList);
        mContactRV.setAdapter(mContactAD);
        mContactRV.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void connectToRainbow() {
        RainbowSdk.instance().setNotificationBuilder(getActivity().getApplicationContext(), ContactsFragment.class,
                0, // You can set it to 0 if you have no app icon
                getString(R.string.app_name),
                "Connect to the app",
                Color.RED);
        RainbowSdk.instance().initialize(); // Will change in the future
        RainbowSdk.instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {
                Log.d(TAG, "onStartSucceeded: singnIn Succeeded");
                RainbowSdk.instance().connection().signin("mhasan@asaltech.com", "Password_123", "sandbox.openrainbow.com", new SigninResponseListener() {
                    @Override
                    public void onSigninSucceeded() {
                        Log.d(TAG, "onSigninSucceeded: singnIn Succeeded");
                        getContacts();
                    }

                    @Override
                    public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                        Log.d(TAG, "onRequestFailed: singnIn Failed");
                    }
                });
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String err) {
                // Do something
                Log.d(TAG, "onRequestFailed: " + err);
            }
        });

    }

    public void getContacts() {
        ArrayItemList mArrayItemList = RainbowSdk.instance().contacts().getRainbowContacts();
        int size= mArrayItemList.getCount();
        for(int i=0 ;i<size ;i++){
           Contact contact= (Contact)mArrayItemList.get(i);
           this.mContactList.add(contact);
        }
        Log.d("getContacts: ", mContactList.get(0).getFirstName());
    }



}
