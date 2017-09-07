package com.example.mhasan.rainbowsdk.activites;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ale.infra.application.RainbowContext;
import com.ale.infra.contact.Contact;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.fragments.LoginInFragment;

import static com.ale.infra.application.RainbowContext.getPlatformServices;
import static com.ale.rainbowsdk.RainbowSdk.instance;
import static com.neovisionaries.i18n.LanguageAlpha3Code.run;
import static com.neovisionaries.i18n.LanguageCode.lo;

/**
 * Created by mhasan on 9/6/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String mConnectedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // getConnectedUser();
        //getSupportFragmentManager().getBackStackEntryCount() == 0 ||
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            showFragment();
        } else {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
        }

    }

    public void getConnectedUser() {
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
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
            }

            @Override
            public void onStartSucceeded() {
                mConnectedUser = instance().contacts().getUserLoginInCache();
                Log.d(TAG, "run: " + mConnectedUser);
                if (mConnectedUser == null) {
                    showFragment();
                } else {
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }

//                runOnUiThread(new Runnable(){
//
//                    @Override
//                    public void run() {
//
//
//
//                    }
//                });
            }
        });


    }

    public void showFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginInFragment loginInFragment = new LoginInFragment();
        fragmentTransaction.add(R.id.content, loginInFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
