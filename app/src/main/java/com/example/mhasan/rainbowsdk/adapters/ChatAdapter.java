package com.example.mhasan.rainbowsdk.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.manager.IMMessage;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ChatMessage;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mhasan on 8/23/2017.
 *
 */

public class ChatAdapter extends BaseAdapter {
    private final List<IMMessage> chatMessages;
    private Context mContext;

    public ChatAdapter(List<IMMessage> chatMessages, Context mContext) {
        this.chatMessages = chatMessages;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if(chatMessages!=null){
            return chatMessages.size();
        }else {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        if (chatMessages!=null){
            return chatMessages.get(position);
        }else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        IMMessage chatMessage = (IMMessage) getItem(position);
        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        boolean myMsg = chatMessage.isMsgSent();
        setAlignment(holder, myMsg);
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

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.contentLayout.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.contentLayout.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.contentLayout.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.contentLayout.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }
    private static class ViewHolder {
        public TextView txtMessage;
        public LinearLayout contentLayout;
        public LinearLayout contentWithBG;
        RelativeLayout contentTxtInfo;
        public TextView txtInfo;
    }
    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage =  v.findViewById(R.id.txtMessage);
        holder.contentLayout = v.findViewById(R.id.contentLayout);
        holder.contentWithBG = v.findViewById(R.id.contentWithBackground);
         holder.txtInfo = (TextView) v.findViewById(R.id.date);
        holder.contentTxtInfo=v.findViewById(R.id.txtInfo);

        return holder;
    }
}
