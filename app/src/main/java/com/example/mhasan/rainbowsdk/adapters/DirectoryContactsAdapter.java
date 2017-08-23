package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ale.infra.contact.DirectoryContact;
import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class DirectoryContactsAdapter extends RecyclerView.Adapter<DirectoryContactsAdapter.dataHolder> {
    ArrayList<DirectoryContact> mContactList;
    private LayoutInflater inflater;
    private Context mContext;

    public DirectoryContactsAdapter(Context context, ArrayList<DirectoryContact>  contactList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.mContactList = contactList;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickedListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public dataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_contact, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        DirectoryContact currentContact = mContactList.get(position);
        String fullName = currentContact.getFirstName() + " " + currentContact.getLastName();
        holder.fullName.setText(fullName);
        if ((currentContact.getPhoto())!=null) {
            holder.profilePic.setImageBitmap(currentContact.getPhoto());
        }else {
            holder.profilePic.setImageResource(R.drawable.ic_placeholder);
        }
        if(currentContact.isRoster()){
            holder.userIcon.setImageResource(R.drawable.ic_user_info);
        }else {
            holder.userIcon.setImageResource(R.drawable.ic_add_user);
        }
    }


    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    class dataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fullName;
        CircleImageView profilePic;
        ImageView userIcon;
        TextView noResult;

        public dataHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            profilePic = itemView.findViewById(R.id.profile_pic);
            userIcon=itemView.findViewById(R.id.contactIcon);
            noResult=itemView.findViewById(R.id.noResult);
            userIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClicked(getAdapterPosition());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
