package com.example.mhasan.rainbowsdk.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.Conversation;
import com.ale.infra.manager.IMMessage;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.ChatAdapter;

import java.util.ArrayList;


/**
 * Created by mhasan on 8/22/2017.
 *
 */

public class ChatActivity extends AppCompatActivity {
    public static final String TAG = ChatActivity.class.getSimpleName();
    private EditText messageET;
    private ListView messagesContainer;
    private ChatAdapter adapter;
    private ArrayList<IMMessage> chatMessages;
    Conversation conversation;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String mTitle = " ";
    private ProgressDialog pDialog;
    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            messagesContainer.clearFocus();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    scroll();
                }
            });
            chatMessages.clear();
            ArrayItemList<IMMessage> messages = conversation.getMessages();
            int msgCount = messages.getCount();
            mTitle = conversation.getName();
            Log.d(TAG, "dataChanged: " + msgCount);
            for (int i = 0; i < msgCount; i++) {
                IMMessage msg = messages.get(i);
                if (msg != null) {
                    chatMessages.add(msg);
                }
            }
            pDialog.dismiss();
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatMessages = new ArrayList<>();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener((new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMoreMessages();
            }
        }));
        getConversation();
    }

    void getConversation() {
        loadDialog();
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        ImageButton sendBtn = (ImageButton) findViewById(R.id.chatSendButton);
        adapter = new ChatAdapter(chatMessages, getBaseContext());
        messagesContainer.setAdapter(adapter);
        messagesContainer.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}
            /**
             *  check if the first row being shown matches the first top-most position,
             *  and then enable the SwipeRefreshLayout. Otherwise, disable it.
             */
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (messagesContainer == null || messagesContainer.getChildCount() == 0) ? 0 : messagesContainer.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                Log.d(TAG, "onScroll: " + firstVisibleItem);
                Log.d(TAG, "visibleItemCount: " + messagesContainer.getFirstVisiblePosition());
            }
        });

        Bundle data = getIntent().getExtras();
        String id = data.getString("conversationId");
        conversation = (Conversation) RainbowSdk.instance().conversations().getConversationFromId(id);
        Contact  contact =conversation.getContact();
        String fullName=contact.getFirstName()+" "+contact.getLastName();
        setCustomTitle(fullName);
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
        RainbowSdk.instance().im().getMessagesFromConversation(conversation, 9);
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
                messageET.setText(" ");

            }

        });
    }
    private void getMoreMessages() {
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
        RainbowSdk.instance().im().getMoreMessagesFromConversation(conversation, 9);
        conversation.getMessages().registerChangeListener(m_changeListener);
        mSwipeRefreshLayout.setRefreshing(false);
        scroll();
    }
    private void scroll() {
        Log.d(TAG, "scroll: " + messagesContainer.getCount());
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    void setCustomTitle(String title) {
        TextView tabOne = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.custom_title, null);
        tabOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tabOne.setText(title);
        getSupportActionBar().setCustomView(tabOne);
    }

    public void loadDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
