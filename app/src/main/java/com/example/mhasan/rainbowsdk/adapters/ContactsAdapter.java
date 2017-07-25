package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.dataHolder> {
    ArrayList<Contact> contactList;
    private LayoutInflater inflater;
    private Context mContext;

    public ContactsAdapter(Context context, ArrayList<Contact> contactList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.contactList = contactList;
    }


    @Override
    public dataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_container, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        Contact currentContact = contactList.get(position);
        String fullName = currentContact.getFirstName() + " " + currentContact.getLastName();
        holder.fullName.setText(fullName);
        Log.d("fullName", fullName);
        if ((currentContact.getPhoto())!=null) {
            holder.profilePic.setImageBitmap(currentContact.getPhoto());
        }else {
            holder.profilePic.setImageResource(R.drawable.ic_placeholder);
        }
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class dataHolder extends RecyclerView.ViewHolder {
        TextView fullName;
        CircleImageView profilePic;

        public dataHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
