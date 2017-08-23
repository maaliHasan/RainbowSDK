package com.example.mhasan.rainbowsdk.activites;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.Conversation;
import com.ale.infra.manager.IMMessage;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.ChatAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mhasan on 8/22/2017.
 *
 */

public class ChatActivity extends AppCompatActivity {
public static final String TAG=ChatActivity.class.getSimpleName();
    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private ArrayList<IMMessage> chatHistory;
    Conversation conversation;
    String mTitle =" ";
    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            // Do something on the thread UI
            ArrayItemList<IMMessage> messages = conversation.getMessages();
            mTitle= conversation.getName();
            Log.d(TAG, "dataChanged: "+mTitle);
            IMMessage msg=messages.get(0);
            IMMessage msg2=messages.get(1);
            chatHistory.add(msg);
            chatHistory.add(msg2);
            for (int i = 0; i < chatHistory.size(); i++) {
                IMMessage message = chatHistory.get(i);
                displayMessage(message);
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatHistory= new ArrayList<>();
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
         setCustomTitle(mTitle);
        getConversation();
    }

    void getConversation() {

        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "send", Toast.LENGTH_LONG).show();
            }

        });

       // loadDummyHistory();
        Bundle data = getIntent().getExtras();
        String id = data.getString("conversationId");
        conversation = (Conversation) RainbowSdk.instance().conversations().getConversationFromId(id);
        RainbowSdk.instance().im().getMessagesFromConversation(conversation, 2);
        conversation.getMessages().registerChangeListener(m_changeListener);
        adapter = new ChatAdapter(chatHistory,getBaseContext());
        messagesContainer.setAdapter(adapter);
//        Toast.makeText(getApplicationContext(), conversation.getName(), Toast.LENGTH_LONG).show();

    }

    public void displayMessage(IMMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {

        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

//    private void loadDummyHistory() {
//
//        chatHistory = new ArrayList<IMMessage>();
//
//        ChatMessage msg = new ChatMessage();
////        msg.setId(1);
////        msg.setMe(false);
////        msg.setMessage("Hi");
////        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg);
//        ChatMessage msg1 = new ChatMessage();
////        msg1.setId(2);
////        msg1.setMe(false);
////        msg1.setMessage("How r u doing???");
////        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
//        chatHistory.add(msg1);
//
//        adapter = new ChatAdapter(getBaseContext(), new ArrayList<IMMessage>());
//        messagesContainer.setAdapter(adapter);
//
//        for (int i = 0; i < chatHistory.size(); i++) {
//            IMMessage message = chatHistory.get(i);
//            displayMessage(message);
//        }
//
//    }

    void setCustomTitle(String title){
        TextView tabOne = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_title, null);
        tabOne.setText(title);
        getSupportActionBar().setCustomView(tabOne);
    }
}
