package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.RainbowPresence;
import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mhasan.rainbowsdk.R.id.contactList;


/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.dataHolder> {
    public static final String TAG = ContactsAdapter.class.getSimpleName();
    ArrayList<Contact> contactList;
    private LayoutInflater inflater;
    private Context mContext;

    public ContactsAdapter(Context context, ArrayList<Contact> contactList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.contactList = contactList;
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
        View view = inflater.inflate(R.layout.contact_container, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        Contact currentContact = contactList.get(position);
        String fullName = currentContact.getFirstName() + " " + currentContact.getLastName();
        holder.contactPresence.setText(currentContact.getPresence().getPresence());
        holder.fullName.setText(fullName);
        if ((currentContact.getPhoto()) != null) {
            holder.profilePic.setImageBitmap(currentContact.getPhoto());
        } else {
            holder.profilePic.setImageResource(R.drawable.ic_placeholder);
        }
        if (currentContact.isRoster()) {
            holder.userIcon.setImageResource(R.drawable.ic_user_info);
        } else {
            holder.userIcon.setImageResource(R.drawable.ic_add_user);
        }
        if (currentContact.getPresence().isOnline()) {
            holder.contactPresence.setText("Online");
            holder.status.setImageResource(R.drawable.ic_online);
        } else if (currentContact.getPresence().isMobileOnline()) {
            holder.contactPresence.setText("Online On mobile");
            holder.status.setImageResource(R.drawable.ic_online);
        } else if (currentContact.getPresence().isAway()) {
            holder.contactPresence.setText("Away");
            holder.status.setImageResource(R.drawable.ic_away);
        } else if (currentContact.getPresence().isOffline()) {
            holder.contactPresence.setText("Offline");
            holder.status.setImageResource(R.drawable.ic_offline);
        } else if (currentContact.getPresence().getPresence().equals("DoNotDisturb")) {
            holder.contactPresence.setText("Do not disturb");
            holder.status.setImageResource(R.drawable.ic_not_distrub);
        } else {
            holder.contactPresence.setText("Offline");
            holder.status.setImageResource(R.drawable.ic_offline);
        }

    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class dataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            mListener.onItemClicked(getAdapterPosition());

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
