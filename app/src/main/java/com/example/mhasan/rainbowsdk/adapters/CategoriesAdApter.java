package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ale.infra.manager.Conversation;
import com.example.mhasan.rainbowsdk.fragments.ContactsFragment;
import com.example.mhasan.rainbowsdk.fragments.ConversationsFragment;
import com.example.mhasan.rainbowsdk.fragments.RainbowContactsFragment;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class CategoriesAdApter extends FragmentStatePagerAdapter {

    public CategoriesAdApter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ContactsFragment();
            case 1:
                return new RainbowContactsFragment();
//            case 2:
//                return new ConversationsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts" ;
            case 1:
                return "ALL Contacts";
            case 2:
                return "Conversation";


        }
        return null;
    }
}




