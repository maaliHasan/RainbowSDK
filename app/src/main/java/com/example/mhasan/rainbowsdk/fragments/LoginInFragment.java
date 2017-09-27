package com.example.mhasan.rainbowsdk.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;
import com.example.mhasan.rainbowsdk.activites.MainActivity;
import static com.ale.rainbowsdk.RainbowSdk.instance;

/**
 * Created by mhasan on 9/6/2017.
 *
 */

public class LoginInFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = LoginInFragment.class.getSimpleName();
    private EditText mEmail;
    private EditText mPassword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button enter = view.findViewById(R.id.btn_enter);
        mEmail =  view.findViewById(R.id.et_email);
        mPassword =  view.findViewById(R.id.et_password);
        String mConnectedUser = instance().contacts().getUserLoginInCache();
        mEmail.setText(mConnectedUser);
        //String mConnectedUserPassword=instance().contacts().getUserPasswordInCache();
        TextView forgotPassword =  view.findViewById(R.id.tv_forgot_password);
        enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String accountType;
        String mUserEmail = mEmail.getText().toString();
        String mUserPassword = mPassword.getText().toString();
        switch (view.getId()) {
            case R.id.btn_enter:
                connectToRainbow(mUserEmail, mUserPassword);
                break;
        }

    }


    public  void connectToRainbow(final String email, final String password) {
        instance().connection().start(new StartResponseListener() {
            @Override
            public void onStartSucceeded() {

                instance().connection().signin(email, password, "sandbox.openrainbow.com", new SigninResponseListener() {
                    @Override
                    public void onSigninSucceeded() {
                        // You are now connected
                        // Do something on the thread UI
                        Log.d(TAG, "onSigninSucceeded: singnIn Succeesed");
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                        // Do something on the thread UI
                        mEmail.setText(" ");
                        mPassword.setText(" ");
                        Toast.makeText(getContext(), "Incorrect Credential ", Toast.LENGTH_LONG).show();
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
    public void onResume() {
        super.onResume();
    }
}
