package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.manager.Conversation;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.room.Room;
import com.example.mhasan.rainbowsdk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.neovisionaries.i18n.LanguageCode.ig;


/**
 *
 * Created by mhasan on 7/20/2017.
 */

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.dataHolder> {
    public static final String TAG = ConversationsAdapter.class.getSimpleName();
    private ArrayList<Conversation> conversationList;
    private LayoutInflater inflater;
    private Context mContext;

    public ConversationsAdapter(Context context, ArrayList<Conversation> conversationList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.conversationList = conversationList;
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

        return conversationList.size();
    }

    class dataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView lastMsg;
        CircleImageView status;
        TextView time;
        TextView unReadMsg;
        CircleImageView profilePic;
        RelativeLayout conversationLayout;



        public dataHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.time);
            unReadMsg=itemView.findViewById(R.id.unReadMsg);
            profilePic = itemView.findViewById(R.id.profile_pic);
            lastMsg = itemView.findViewById(R.id.lastMsg2);
            conversationLayout=itemView.findViewById(R.id.conversation_layout);
            conversationLayout.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            mListener.onItemClicked(getAdapterPosition());

        }
    }

    @Override
    public dataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_conversation, parent, false);
        return new dataHolder(view);
    }

    @Override
    public void onBindViewHolder(dataHolder holder, int position) {
        Conversation currentConversation = conversationList.get(position);
        Contact contact = currentConversation.getContact();
        IMMessage lastMessage = currentConversation.getLastMessage();
        int unReadMsg=currentConversation.getUnreadMsgNb();
        Log.d(TAG, "onBindViewHolder: "+unReadMsg);

        if (currentConversation.isRoomType()) {
            int roomUnReadMsg=currentConversation.getUnreadMsgNb();
            Log.d(TAG, "RoomunReadMsg: "+roomUnReadMsg);
            Room currentRoom = currentConversation.getRoom();
            holder.name.setText(currentRoom.getName());
            holder.profilePic.setImageResource(R.drawable.ic_group);
            holder.status.setVisibility(View.INVISIBLE);

            if(roomUnReadMsg>0){
                holder.unReadMsg.setText(String.valueOf(roomUnReadMsg));
                holder.unReadMsg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.name.setText(contact.getFirstName() + " " + contact.getLastName());
            if ((contact.getPhoto()) != null) {
                holder.profilePic.setImageBitmap(contact.getPhoto());

            } else {
                holder.profilePic.setImageResource(R.drawable.ic_placeholder);
            }
            if (contact.getPresence().isOnline()) {
                holder.status.setImageResource(R.drawable.ic_online);
            } else if (contact.getPresence().isMobileOnline()) {
                holder.status.setImageResource(R.drawable.ic_online);
            } else if (contact.getPresence().isAway()) {
                holder.status.setImageResource(R.drawable.ic_away);
            } else if (contact.getPresence().isOffline()) {
                holder.status.setImageResource(R.drawable.ic_offline);
            } else if (contact.getPresence().getPresence().equals("DoNotDisturb")) {
                holder.status.setImageResource(R.drawable.ic_not_distrub);
            } else {
                holder.status.setImageResource(R.drawable.ic_offline);
            }
            if(unReadMsg>0){
                holder.unReadMsg.setText(String.valueOf(unReadMsg));
                holder.unReadMsg.setVisibility(View.VISIBLE);
            }
        }

        if ((lastMessage.getMessageContent()) != null) {
            holder.lastMsg.setText(lastMessage.getMessageContent());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(lastMessage.getTimeStamp());
            String date = DateFormat.format("dd MMM ", cal).toString();
            holder.time.setText(date);
        } else {
            holder.lastMsg.setText(" ");
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
