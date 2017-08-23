package com.example.mhasan.rainbowsdk.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.Conversation;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.ChatActivity;
import com.example.mhasan.rainbowsdk.adapters.ConversationsAdapter;

import java.util.ArrayList;

import static com.example.mhasan.rainbowsdk.R.id.conversationList;
import static com.example.mhasan.rainbowsdk.adapters.ContactsAdapter.TAG;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class ConversationsFragment extends Fragment  implements ConversationsAdapter.OnItemClickListener{
    private ProgressDialog pDialog;
    private ConversationsAdapter mConversationAD;
    private ArrayList<Conversation> mConversationList;

    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {

            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mConversationAD.notifyDataSetChanged();
                }
            });
            // Do something on the thread UI
            ArrayItemList<IRainbowConversation> conversations = RainbowSdk.instance().conversations().getAllConversations();
            Log.d(TAG, "dataChanged: " + conversations.getCount());
            mConversationList.clear();
            int size = conversations.getCount();
            for (int i = 0; i < size; i++) {
                Conversation conversation = (Conversation) conversations.get(i);
                Log.d(TAG, "dataChanged: " + conversation.getLastMessage());
                // conversation.registerChangeListener();
                mConversationList.add(conversation);
            }
           pDialog.dismiss();
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mConversationList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDialog();
        RainbowSdk.instance().conversations().getAllConversations().registerChangeListener(m_changeListener);
        RecyclerView mConversationRV = getActivity().findViewById(conversationList);
        mConversationAD = new ConversationsAdapter(getActivity(), mConversationList);
         mConversationAD.setOnItemClickedListener(this);
        mConversationRV.setAdapter(mConversationAD);
        mConversationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void loadDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onItemClicked(int position) {
        Conversation currentConversation = mConversationList.get(position);
        String conversationId= currentConversation.getId() ;
        Intent intent= new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("conversationId",conversationId);
        startActivity(intent);

    }
}
