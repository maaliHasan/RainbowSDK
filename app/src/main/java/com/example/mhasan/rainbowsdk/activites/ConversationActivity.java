package com.example.mhasan.rainbowsdk.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ale.infra.manager.Conversation;
import com.ale.rainbowsdk.Conversations;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;

/**
 * Created by mhasan on 8/22/2017.
 *
 */

public class ConversationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       getConversation();
    }
    void getConversation(){
        Bundle data = getIntent().getExtras();
        String  id=data.getString("conversationId");

        Conversation conversation= (Conversation) RainbowSdk.instance().conversations().getConversationFromId(id);
        Toast.makeText(getApplicationContext(),conversation.getName(),Toast.LENGTH_LONG).show();

    }
}
