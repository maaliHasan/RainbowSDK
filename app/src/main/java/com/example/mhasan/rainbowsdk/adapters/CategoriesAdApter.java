package com.example.mhasan.rainbowsdk.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mhasan.rainbowsdk.fragments.ContactsFragment;
import com.example.mhasan.rainbowsdk.fragments.ConversationsFragment;
import com.example.mhasan.rainbowsdk.fragments.PendingInvitation;

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
                return new ConversationsFragment();
            case 1:
               return new ContactsFragment();
//            case 2:
//                return new PendingInvitation();

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
                return "" ;
            case 1:
                return "";
//            case 2:
//                return "Invitation";
        }
        return null;
    }
}




