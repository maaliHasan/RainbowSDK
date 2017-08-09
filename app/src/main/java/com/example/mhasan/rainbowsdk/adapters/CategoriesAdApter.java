package com.example.mhasan.rainbowsdk.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import com.example.mhasan.rainbowsdk.fragments.ContactsFragment;
import com.example.mhasan.rainbowsdk.fragments.RainbowContactsFragment;
import com.example.mhasan.rainbowsdk.fragments.SettingsFragment;

/**
 * Created by mhasan on 7/20/2017.
 *
 */

public class CategoriesAdApter extends FragmentPagerAdapter {
    private Context mContex;
    public CategoriesAdApter(Context context,FragmentManager fm) {
        super(fm);
        mContex=context;

    }

    @Override
    public Fragment getItem(int position) {
      switch (position){
          case 0:
              return new ContactsFragment();
          case 1:
              return new RainbowContactsFragment();
          case 2:
              return new SettingsFragment();
      }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts" ;
            case 1:
                return "Conversation";
            case 2:
                return "ALL Contacts";

        }
        return null;
    }
}
