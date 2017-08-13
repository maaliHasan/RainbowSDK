package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ale.infra.contact.Contact;

import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mhasan on 8/10/2017.
 *
 */

public class DrawerItemCustomAdapter  extends RecyclerView.Adapter<DrawerItemCustomAdapter.dataHolder>{
    public static final String TAG = DrawerItemCustomAdapter.class.getSimpleName();
    ArrayList<Contact> contactList;
    private LayoutInflater inflater;
    private Context mContext;

    public DrawerItemCustomAdapter(ArrayList<Contact> contactList, LayoutInflater inflater, Context mContext) {
        this.contactList = contactList;
        this.inflater = inflater;
        this.mContext = mContext;
    }

    @Override
    public dataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_connected_contact_deatails, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        Contact currentContact = contactList.get(position);
        String fullName = currentContact.getFirstName() + " " + currentContact.getLastName();
        holder.contactPresence.setText(currentContact.getPresence().getPresence());
        holder.fullName.setText(fullName);

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class  dataHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fullName;
        CircleImageView status;
        TextView contactPresence;
        CircleImageView profilePic;
        ImageView userIcon;

        public dataHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            status = itemView.findViewById(R.id.status);
            contactPresence = itemView.findViewById(R.id.contactPresence);
            profilePic = itemView.findViewById(R.id.profile_pic);
            userIcon = itemView.findViewById(R.id.contactIcon);
            userIcon.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

        }

    }


}
