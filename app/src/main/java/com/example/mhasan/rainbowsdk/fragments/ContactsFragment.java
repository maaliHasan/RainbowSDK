package com.example.mhasan.rainbowsdk.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ale.infra.contact.Contact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
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
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDialog();
        RainbowSdk.instance().contacts().getRainbowContacts().registerChangeListener(m_changeListener);
        mContactRV = getActivity().findViewById(R.id.contactList);
        mContactAD = new ContactsAdapter(getActivity(), mContactList);
        mContactRV.setAdapter(mContactAD);
        mContactRV.setLayoutManager(new LinearLayoutManager(getActivity()));
       // mContactAD.notifyDataSetChanged();

    }
    @Override
    public void onDestroyView() {
        RainbowSdk.instance().contacts().getRainbowContacts().unregisterChangeListener(m_changeListener);
        super.onDestroyView();
    }


public  void loadDialog(){
    pDialog = new ProgressDialog(getActivity());
    pDialog.setMessage("Loading");
    pDialog.setCancelable(false);
    pDialog.show();
}


}
