package com.example.mhasan.rainbowsdk.activites;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.ChatMgr;
import com.ale.infra.manager.Conversation;
import com.ale.infra.manager.IMMessage;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.ChatAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.util.Config.LOGD;
import static com.neovisionaries.i18n.LanguageCode.lo;
import static com.neovisionaries.i18n.LanguageCode.ms;

/**
 * Created by mhasan on 8/22/2017.
 *
 */

public class ChatActivity extends AppCompatActivity {
    public static final String TAG = ChatActivity.class.getSimpleName();
    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private ArrayList<IMMessage> chatHistory;
    Conversation conversation;
    String mTitle = " ";
    private ProgressDialog pDialog;
    private ChatMgr.IChatMgrListener m_listener = new ChatMgr.IChatMgrListener() {

        @Override
        public void onImReceived(Conversation conversation, IMMessage imMessage) {

        }

        @Override
        public void isTypingState(Contact contact, boolean b, String s) {

        }

        @Override
        public void onImSent(Conversation conversation) {

        }

        @Override
        public void onConversationsUpdated() {

        }
    };
    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // mContactAD.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    scroll();
                }
            });
            // Do something on the thread UI
            chatHistory.clear();
            ArrayItemList<IMMessage> messages = conversation.getMessages();
            int msgCount= messages.getCount();
            mTitle = conversation.getName();
            Log.d(TAG, "dataChanged: " + mTitle);
            for(int i=0;i<msgCount;i++){
                IMMessage msg = messages.get(i);
                if(msg !=null){
                    chatHistory.add(msg);
                }
            }



            pDialog.dismiss();
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatHistory = new ArrayList<>();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getConversation();
    }

    void getConversation() {
        loadDialog();
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        adapter = new ChatAdapter(chatHistory, getBaseContext());
        messagesContainer.setAdapter(adapter);

        Bundle data = getIntent().getExtras();
        String id = data.getString("conversationId");
        conversation = (Conversation) RainbowSdk.instance().conversations().getConversationFromId(id);
        setCustomTitle(conversation.getName());
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
        RainbowSdk.instance().im().getMoreMessagesFromConversation(conversation, 9);
        conversation.getMessages().registerChangeListener(m_changeListener);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
                RainbowSdk.instance().im().sendMessageToConversation(conversation, messageText);

            }

        });
    }

     private void scroll() {
        Log.d(TAG, "scroll: "+messagesContainer.getCount());
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    void setCustomTitle(String title) {
        Log.d(TAG, "setCustomTitle: "+title);
        TextView tabOne = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_title, null);
        tabOne.setText(title);
        getSupportActionBar().setCustomView(tabOne);
    }

    public void loadDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
