package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class PendingInvitationsAdapter extends RecyclerView.Adapter<PendingInvitationsAdapter.pendingInvitationHolder> {
    public static final String TAG = PendingInvitationsAdapter.class.getSimpleName();
    private ArrayItemList<IRainbowContact> invitationSent ;
    private  List<IRainbowContact> pendingInvitations;
    private LayoutInflater inflater;
    private Context mContext;

    public PendingInvitationsAdapter(Context context, List<IRainbowContact> pendingInvitations) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.pendingInvitations = pendingInvitations;
    }


    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickedListener(OnItemClickListener listener) {
        this.mListener = listener;
    }



    @Override
    public int getItemCount() {
        return pendingInvitations.size();
    }


    @Override
    public pendingInvitationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pending_invitation, parent, false);
        return new PendingInvitationsAdapter.pendingInvitationHolder(view);
    }

    @Override
    public void onBindViewHolder(pendingInvitationHolder holder, int position) {
        IRainbowContact contact=pendingInvitations.get(position);


    }


    class pendingInvitationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView fullName;
        CircleImageView profilePic;
        ImageView acceptIcon;
        ImageView declineIcon;

        public pendingInvitationHolder(View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.fullName);
            profilePic = itemView.findViewById(R.id.profile_pic);
            acceptIcon = itemView.findViewById(R.id.acceptIcon);
            declineIcon = itemView.findViewById(R.id.declineIcon);
            acceptIcon.setOnClickListener(this);
            declineIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

}
