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

import static com.example.mhasan.rainbowsdk.R.id.contactList;
import static com.example.mhasan.rainbowsdk.R.id.fullName;

/**
 * Created by mhasan on 7/20/2017.
 */

public class PhoneEmailAdapter extends RecyclerView.Adapter<PhoneEmailAdapter.dataHolder> {
    ArrayList<String> emailAddresses;
    private LayoutInflater inflater;
    private Context mContext;

    public PhoneEmailAdapter(Context context, ArrayList<String> emailAddresses) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.emailAddresses = emailAddresses;
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
        View view = inflater.inflate(R.layout.email_phone_container, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        String current = emailAddresses.get(position);
        holder.emailType.setText("Email");
        holder.emailValue.setText(current);
//        if ((currentContact.getPhoto()) != null) {
//            holder.profilePic.setImageBitmap(currentContact.getPhoto());
//        } else {
//            holder.profilePic.setImageResource(R.drawable.ic_placeholder);
//        }
//        if (currentContact.isRoster()) {
//            holder.userIcon.setImageResource(R.drawable.ic_user_info);
//        } else {
//            holder.userIcon.setImageResource(R.drawable.ic_add_user);
//        }
    }


    @Override
    public int getItemCount() {
        return emailAddresses.size();
    }

    class dataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView emailType;
        TextView emailValue;
        ImageView phoneEmailIcon;

        public dataHolder(View itemView) {
            super(itemView);
            emailType = itemView.findViewById(R.id.type);
            emailValue = itemView.findViewById(R.id.value);
            phoneEmailIcon = itemView.findViewById(R.id.phoneEmailIcon);

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
