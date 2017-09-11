package com.example.mhasan.rainbowsdk.activites;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.fragments.LoginInFragment;

import static com.ale.rainbowsdk.RainbowSdk.instance;


/**
 * Created by mhasan on 9/6/2017.
 *
 */

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String mConnectedUser;
    private String mConnectedUserPassword ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         getConnectedUser();

    }

    public void getConnectedUser() {
        Log.d(TAG, "getConnectedUser: "+"get connected user");
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
                mConnectedUserPassword=instance().contacts().getUserPasswordInCache();
                Log.d(TAG, "run: " + mConnectedUser);
                if (mConnectedUser.equals(null) ||mConnectedUser.equals("")) {
                    showFragment();
                } else {
                connectToRainbow(mConnectedUser,mConnectedUserPassword,"official");

                }

            }
        });


    }

    public void showFragment() {
        Log.d(TAG, "showFragment: "+"showFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginInFragment loginInFragment = new LoginInFragment();
        fragmentTransaction.add(R.id.content, loginInFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public  void connectToRainbow(final String email, final String password, final String accountType) {
        instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {
                switch (accountType) {
                    case "official":
                        instance().connection().signin(email, password, new SigninResponseListener() {
                            @Override
                            public void onSigninSucceeded() {
                                // You are now connected
                                // Do something on the thread UI
                                Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                                // Do something on the thread UI
                                new LoginInFragment();
                            }
                        });
                        break;
                    case "sandbox":
                        instance().connection().signin(email, password, "sandbox.openrainbow.com", new SigninResponseListener() {
                            @Override
                            public void onSigninSucceeded() {
                                // You are now connected
                                // Do something on the thread UI
                                Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                                // Do something on the thread UI
                                new LoginInFragment();
                            }
                        });
                }
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String err) {
                // Do something
            }
        });

    }

}
