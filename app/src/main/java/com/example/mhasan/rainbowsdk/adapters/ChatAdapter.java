package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ale.infra.contact.Contact;
import com.ale.infra.manager.IMMessage;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mhasan on 8/23/2017.
 *
 */

public class ChatAdapter extends BaseAdapter {
    private static final String TAG = ChatAdapter.class.getSimpleName();
    private final List<IMMessage> chatMessages;
    private Context mContext;
    private Contact contact;

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
        if (chatMessages != null && chatMessages.size() !=0) {
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
        boolean isRead = false;
        IMMessage chatMessage = (IMMessage) getItem(position);
        contact = (Contact) RainbowSdk.instance().contacts().getContactFromJabberId(chatMessage.getContactJid());
        LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        boolean myMsg = chatMessage.isMsgSent();
        IMMessage.DeliveryState deliveryState = chatMessage.getDeliveryState();
        if (deliveryState.name().equals("READ") || deliveryState.name().equals("SENT_CLIENT_READ")) {
            isRead = true;
        }
        setAlignment(holder, myMsg, isRead);
        holder.txtMessage.setText(chatMessage.getMessageContent()+"\n");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(chatMessage.getTimeStamp());
        String date = DateFormat.format("dd MMM ", cal).toString();
        holder.txtInfo.setText(date);
        if ((contact.getPhoto()) != null) {
            holder.senderPic.setImageBitmap(contact.getPhoto());
            Log.d(TAG, "contactPhoto: "+contact.getPhoto().toString());
        } else {
            holder.senderPic.setImageResource(R.drawable.ic_placeholder);
        }

        return view;
    }


    public void add(IMMessage message) {
        chatMessages.add(message);
    }

    public void add(List<IMMessage> messages) {

        chatMessages.addAll(messages);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAlignment(ViewHolder holder, boolean isMe, boolean isRead) {

        if (isMe) {
            holder.senderPic.setVisibility(View.INVISIBLE);
            if (isRead) {
                holder.txtStatus.setBackgroundResource(R.drawable.ic_msg_is_read);
            } else {
                holder.txtStatus.setBackgroundResource(R.drawable.ic_msg_recived);
            }

        } else {
            holder.txtMessage.setBackgroundResource(R.drawable.out_message_bg);
            holder.txtStatus.setVisibility(View.INVISIBLE);
            holder.senderPic.setVisibility(View.VISIBLE);

        }
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public ImageView txtStatus;
        public CircleImageView senderPic;

    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = v.findViewById(R.id.txtMessage);
        holder.senderPic = v.findViewById(R.id.senderPic);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.txtStatus = v.findViewById(R.id.txtStatus);

        return holder;
    }
}
