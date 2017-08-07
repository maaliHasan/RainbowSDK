package com.example.mhasan.rainbowsdk.activites;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ale.infra.contact.DirectoryContact;
import com.ale.infra.contact.IContactSearchListener;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.CategoriesAdApter;
import com.example.mhasan.rainbowsdk.fragments.DirectoryContactsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mFragmentsContent;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private CategoriesAdApter mCategoriesAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectToRainbow();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.viewPagerLayout);
        mFragmentsContent = (RelativeLayout) findViewById(R.id.fragmentsContent);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mCategoriesAdapter = new CategoriesAdApter(this, getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCategoriesAdapter);
        mViewPager.setCurrentItem(0);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(ColorStateList.valueOf((getResources().getColor(R.color.colorPrimary))));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              //  mRelativeLayout.setVisibility(View.GONE);
                mFragmentsContent.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DirectoryContactsFragment DContactFatagment = new DirectoryContactsFragment();
                fragmentTransaction.add(R.id.fragmentsContent, DContactFatagment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public void connectToRainbow() {
        RainbowSdk.instance().setNotificationBuilder(getApplicationContext(), MainActivity.class,
                0, // You can set it to 0 if you have no app icon
                getString(R.string.app_name),
                "Connect to the app",
                Color.RED);
        RainbowSdk.instance().initialize(); // Will change in the future

        RainbowSdk.instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {
                RainbowSdk.instance().connection().signin("mhasan@asaltech.com","Password_123","sandbox.openrainbow.com",new SigninResponseListener() {
                    @Override
                    public void onSigninSucceeded() {
                        // You are now connected
                        // Do something on the thread UI
                        Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
                    }

                    @Override
                    public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                        // Do something on the thread UI
                        Log.d(TAG, "onRequestFailed: singnIn Failed");
                    }
                });
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String err) {
                // Do something
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int fragmentsCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentsCount > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
