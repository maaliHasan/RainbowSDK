package com.example.mhasan.rainbowsdk.activites;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.list.ArrayItemList;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.adapters.CategoriesAdApter;
import com.example.mhasan.rainbowsdk.fragments.DirectoryContactsFragment;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ale.rainbowsdk.RainbowSdk.instance;
import static com.example.mhasan.rainbowsdk.R.id.status;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private String mUserName;
    private String mEmail;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mFragmentsContent;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView mNameText;
    private TextView mEmailText;
    private TextView mPresence;
    private CircleImageView mProfilePic;
    private CircleImageView mPresenceIcon;
    private ImageView mViewPresence;
    private Contact mConnectedContact;
    private Contact mUpdatedConnectedContact;
    private Contact.ContactListener m_contactListener = new Contact.ContactListener() {

        @Override
        public void contactUpdated(Contact contact) {

        }

        @Override
        public void onPresenceChanged(Contact contact, RainbowPresence rainbowPresence) {
            mUpdatedConnectedContact = new Contact();
            mUpdatedConnectedContact = (Contact) instance().myProfile().getConnectedUser();
            Log.d(TAG, "onPresenceChanged: " + mUpdatedConnectedContact.getFirstName() + " " + mUpdatedConnectedContact.getPresence().getPresence());
            updateAdapter();
        }

        @Override
        public void onActionInProgress(boolean b) {

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectToRainbow();


        mRelativeLayout = (RelativeLayout) findViewById(R.id.viewPagerLayout);
        mFragmentsContent = (RelativeLayout) findViewById(R.id.fragmentsContent);

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View mHeaderLayout = navigationView.getHeaderView(0);
        mNameText = mHeaderLayout.findViewById(R.id.username);
        mEmailText = mHeaderLayout.findViewById(R.id.email);
        mPresence = mHeaderLayout.findViewById(R.id.presence);
        mProfilePic = mHeaderLayout.findViewById(R.id.profile_image);
        mPresenceIcon = mHeaderLayout.findViewById(status);
        mViewPresence = mHeaderLayout.findViewById(R.id.view_more);
        mViewPresence.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (item.getItemId()) {
                    case R.id.logout:
                        getPendingSentInvitations();
                        Toast.makeText(getApplicationContext(), "logOut Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                updateConnectedUserInfo(mConnectedContact);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        navigationView.bringToFront();
        mDrawerLayout.requestLayout();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        final TabLayout TopTabLayout = (TabLayout) findViewById(R.id.tabs);
        TopTabLayout.setTabTextColors(ColorStateList.valueOf((getResources().getColor(R.color.colorPrimary))));
        TopTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        CategoriesAdApter mCategoriesAdapter = new CategoriesAdApter(getSupportFragmentManager());
        mViewPager.setAdapter(mCategoriesAdapter);
        mViewPager.setCurrentItem(0);
        TopTabLayout.setupWithViewPager(mViewPager);
        TopTabLayout.getTabAt(0).setIcon(R.drawable.ic_conversation);
        TopTabLayout.getTabAt(1).setIcon(R.drawable.ic_contact);

       // TopTabLayout.setVisibility(View.GONE);
//        final TabLayout BttomTabLayout = (TabLayout)findViewById(R.id.tabs2);
//        BttomTabLayout.addTab(BttomTabLayout.newTab().setIcon(R.drawable.ic_contact));
//        BttomTabLayout.addTab(BttomTabLayout.newTab().setIcon(R.drawable.ic_conversation));
//        BttomTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
//        BttomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    mViewPager.setAdapter(mCategoriesAdapter);
//                    mViewPager.setCurrentItem(0);
//                    TopTabLayout.setupWithViewPager(mViewPager);
//                    TopTabLayout.setVisibility(View.VISIBLE);
//                    TopTabLayout.addOnTabSelectedListener(
//                            new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
//                                @Override
//                                public void onTabSelected(TabLayout.Tab tab) {
//                                    super.onTabSelected(tab);
//                                    TopTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
//                                }
//                            }
//                    );
//
//                    Toast.makeText(getApplicationContext(), "contacts Selected", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
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
                DirectoryContactsFragment DContactFaragment = new DirectoryContactsFragment();
                fragmentTransaction.add(R.id.fragmentsContent, DContactFaragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public void connectToRainbow() {
        instance().setNotificationBuilder(getApplicationContext(), MainActivity.class,
                0, // You can set it to 0 if you have no app icon
                getString(R.string.app_name),
                "Connect to the app",
                Color.RED);
        if (!instance().isInitialized()) {
            instance().initialize(); // Will change in the future
        }


        instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {
                instance().connection().signin("mhasan@asaltech.com", "Asal@123", new SigninResponseListener() {
                    @Override
                    public void onSigninSucceeded() {
                        // You are now connected
                        // Do something on the thread UI
                        Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
                        getConnectedUserInfo();
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
        // super.onBackPressed();
        int fragmentsCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "onBackPressed1: " + fragmentsCount);
        if (fragmentsCount ==1) {
            getSupportFragmentManager().popBackStack();
            Log.d(TAG, "onBackPressed: " + fragmentsCount);
            mFragmentsContent.setVisibility(View.GONE);
         mRelativeLayout.setVisibility(View.VISIBLE);

        }
        mRelativeLayout.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();

    }

    private void getConnectedUserInfo() {
        mConnectedContact = new Contact();
        mConnectedContact = (Contact) instance().myProfile().getConnectedUser();
        mConnectedContact.registerChangeListener(m_contactListener);
        Log.d(TAG, "onItemClick: " + mConnectedContact.getFirstName());
    }

    private void updateConnectedUserInfo(Contact contact) {
        Log.d(TAG, "updateConnectedUserInfo: " + mUpdatedConnectedContact.getPresence().getPresence());
        mUserName = contact.getFirstName() + " " + contact.getLastName();
        Log.d(TAG, "updateConnectedUserInfo: " + contact.getFirstName() + " " + contact.getPresence());
        mEmail = contact.getLoginEmail();
        Bitmap mUserPic = contact.getPhoto();
        mNameText.setText(mUserName);
        mEmailText.setText(mEmail);
        if ((contact.getPhoto()) != null) {
            mProfilePic.setImageBitmap(mUserPic);
        } else {
            mProfilePic.setImageResource(R.drawable.ic_placeholder);
        }

        if (contact.getPresence().isOnline() || contact.getPresence().isMobileOnline()) {
            mPresence.setText("Online");
            mPresenceIcon.setImageResource(R.drawable.ic_online);
        } else if (contact.getPresence().isAway() || contact.getPresence().isManualAway()) {
            mPresence.setText("Away");
            mPresenceIcon.setImageResource(R.drawable.ic_away);
        } else if (contact.getPresence().isBusy() || contact.getPresence().isManualAway() || contact.getPresence().isUnsubscribed()) {
            mPresence.setText("Offline");
            mPresenceIcon.setImageResource(R.drawable.ic_offline);
        } else if (contact.getPresence().getPresence().equals("DoNotDisturb")) {
            mPresence.setText("Do not disturb");
            mPresenceIcon.setImageResource(R.drawable.ic_not_distrub);
        } else {
            mPresence.setText("Offline");
            mPresenceIcon.setImageResource(R.drawable.ic_offline);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.view_more:
                PopupMenu popupMenu = new PopupMenu(this, mViewPresence);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String presence = (String) item.getTitle();
                        switch (presence) {
                            case "Offline":
                                instance().myProfile().setPresenceTo(RainbowPresence.OFFLINE);
                                updateAdapter();
                                break;
                            case "Online":
                                instance().myProfile().setPresenceTo(RainbowPresence.ONLINE);
                                updateAdapter();
                                break;
                            case "Away":
                                instance().myProfile().setPresenceTo(RainbowPresence.AWAY);
                                updateAdapter();
                                break;
                            case "Do not disturb":
                                instance().myProfile().setPresenceTo(RainbowPresence.DND);
                                updateAdapter();
                                break;

                        }
                        updateAdapter();
                        return true;
                    }
                });
                popupMenu.show();
                break;
        }

    }

    private void updateAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateConnectedUserInfo(mUpdatedConnectedContact);

            }
        });


    }

    void getPendingSentInvitations() {
//        Contact contact=new Contact();
//        contact= (Contact) RainbowSdk.instance().contacts().getContactFromJabberId("c47e988b453b420b8806516979dece01@openrainbow.com");
        Log.d(TAG, "getPendingSentInvitations: ");
        List<IRainbowContact> pendingSentInvitations = RainbowSdk.instance().contacts().getPendingSentInvitations();
        Log.d(TAG, "getPendingSentInvitations: " + pendingSentInvitations.size());
        List<IRainbowContact> pendingReceivedInvitations = RainbowSdk.instance().contacts().getPendingReceivedInvitations();
        Log.d(TAG, "getPendingReceivedInvitations: " + pendingReceivedInvitations.size());
        ArrayItemList<IRainbowContact> invitationSent = RainbowSdk.instance().contacts().getReceivedInvitations();
        Log.d(TAG, "getReceivedInvitations: " + invitationSent.getCount());
        ArrayItemList<IRainbowContact> sentInvitation = RainbowSdk.instance().contacts().getSentInvitations();
        Log.d(TAG, "getSentInvitations: " + sentInvitation);

    }


}
