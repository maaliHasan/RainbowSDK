package com.example.mhasan.rainbowsdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.manager.IMMessage;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ChatMessage;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.util.Config.LOGD;
import static net.java.sip.communicator.impl.protocol.jabber.extensions.jingle.SessionInfoType.hold;

/**
 * Created by mhasan on 8/23/2017.
 *
 */

public class ChatAdapter extends BaseAdapter {
    private static final String TAG = ChatAdapter.class.getSimpleName();
    private final List<IMMessage> chatMessages;
    private Context mContext;

    public ChatAdapter(List<IMMessage> chatMessages, Context mContext) {
        this.chatMessages = chatMessages;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        boolean  isRead= false;
        IMMessage chatMessage = (IMMessage) getItem(position);
        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        boolean myMsg = chatMessage.isMsgSent();
        IMMessage.DeliveryState deliveryState =chatMessage.getDeliveryState();
        if(deliveryState.name().equals("READ") || deliveryState.name().equals("SENT_CLIENT_READ")){
           isRead= true;
        }
        setAlignment(holder, myMsg ,isRead);
        holder.txtMessage.setText(chatMessage.getMessageContent());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(chatMessage.getTimeStamp());
        String date = DateFormat.format("dd MMM ", cal).toString();
        holder.txtInfo.setText(date);



        return view;
    }


    public void add(IMMessage message) {
        chatMessages.add(message);
    }

    public void add(List<IMMessage> messages) {
        chatMessages.addAll(messages);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlignment(ViewHolder holder, boolean isMe , boolean isRead) {

        if (isMe) {
            RelativeLayout.LayoutParams txtStatusPrams=  (RelativeLayout.LayoutParams)holder.txtStatus.getLayoutParams();
            txtStatusPrams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            txtStatusPrams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            txtStatusPrams.addRule(RelativeLayout.RIGHT_OF, holder.txtInfo.getId());
            holder.txtStatus.setLayoutParams(txtStatusPrams);

            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
            holder.txtInfo.setId(View.generateViewId());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);


            holder.contentTxtInfo.setGravity(Gravity.RIGHT);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.contentLayout.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.contentLayout.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams txtInfoPrams= (RelativeLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            txtInfoPrams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            txtInfoPrams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.txtInfo.setLayoutParams(txtInfoPrams);

            if(isRead){
                Log.d( "setAlignment", "setAlignment1: "+isRead);
                holder.txtStatus.setBackgroundResource(R.drawable.ic_msg_is_read);
            }else {
                Log.d( "setAlignment", "setAlignment: "+isRead);
                holder.txtStatus.setBackgroundResource(R.drawable.ic_msg_recived);
            }

        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);
            holder.contentTxtInfo.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams Rl = (LinearLayout.LayoutParams) holder.contentTxtInfo.getLayoutParams();
            Rl.gravity=Gravity.LEFT;
            holder.contentTxtInfo.setLayoutParams(Rl);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.contentLayout.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.contentLayout.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);
            holder.txtStatus.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams txtInfoPrams= (RelativeLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            txtInfoPrams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            txtInfoPrams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.txtInfo.setLayoutParams(txtInfoPrams);

        }
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public LinearLayout contentLayout;
        public LinearLayout contentWithBG;
        public RelativeLayout contentTxtInfo;
        public TextView txtInfo;
        public ImageView txtStatus;

    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = v.findViewById(R.id.txtMessage);
        holder.contentLayout = v.findViewById(R.id.contentLayout);
        holder.contentWithBG = v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.contentTxtInfo = (RelativeLayout)v.findViewById(R.id.txtInfoLayout);
        holder.txtStatus = v.findViewById(R.id.txtStatus);

        return holder;
    }
}
