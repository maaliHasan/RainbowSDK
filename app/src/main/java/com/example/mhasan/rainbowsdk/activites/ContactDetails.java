package com.example.mhasan.rainbowsdk.activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;
import android.widget.Toast;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.listener.IRainbowContactManagementListener;
import com.ale.listener.IRainbowSentInvitationListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.example.mhasan.rainbowsdk.R;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.neovisionaries.i18n.LanguageAlpha3Code.bre;


/**
 * Created by mhasan on 7/30/2017.
 *
 */

public class ContactDetails extends AppCompatActivity implements View.OnClickListener {
    public final static String AFTER_Delete_USER = "deleted";
    public final static String AFTER_ADD_USER = "added";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE=0x0000000;
    ContactData mContact;
    private RelativeLayout invitationLayout;
    private ImageView addUserIcon;
    private TextView pendingLabel;
    private RelativeLayout.LayoutParams pendingLabelParams;
    private TextView invitationLabel;
    private LinearLayout linearLayout;
    private ProgressDialog pDialog;
    private String phoneNum;
    private String mobileNum;
    public static final String TAG = ContactDetails.class.getSimpleName();
    private IRainbowSentInvitationListener mAddContactListener = new IRainbowSentInvitationListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onInvitationSentSuccess(String s) {
            Boolean isInvitationSent = true;
            updateInvitationLayout(AFTER_ADD_USER);
            Log.d(TAG, "onInvitationSentSuccess: " + s);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onInvitationSentError(RainbowServiceException e) {
            Log.d(TAG, "onInvitationSentError: " + e.getDetailsMessage());
            updateInvitationLayout(AFTER_ADD_USER);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getBaseContext(), "This User invitation has already been sent  in the last 3600 seconds, do not send email again", Toast.LENGTH_LONG).show();
                }
            });

        }

        @Override
        public void onInvitationError() {
            Log.d(TAG, "onInvitationError: ");

        }
    };

    private IRainbowContactManagementListener mRemoveContactListener = new IRainbowContactManagementListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void OnContactRemoveSuccess(String s) {
            updateInvitationLayout(AFTER_Delete_USER);
            Log.d(TAG, "OnContactRemoveSuccess: " + s);

        }

        @Override
        public void onContactRemovedError(Exception e) {
            Log.d(TAG, "onContactRemovedError: " + e.toString());

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_deatails);
        getContactData();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getContactData() {
        TextView fullName = (TextView) findViewById(R.id.fullName);
        TextView presence = (TextView) findViewById(R.id.presence);
        TextView jobTitle = (TextView) findViewById(R.id.jobTitle);
        CircleImageView pic = (CircleImageView) findViewById(R.id.profile_pic);

        Bundle data = getIntent().getExtras();
        mContact = data.getParcelable("ContactData");
        boolean isRoster = Boolean.valueOf(mContact.isRoster);

        fullName.setText(mContact.fullName);
        jobTitle.setText(mContact.jobTitle);
        presence.setText(mContact.presence);
        if ((mContact.profilePic) != null) {
            pic.setImageBitmap(mContact.profilePic);
        } else {
            pic.setImageResource(R.drawable.ic_placeholder);
        }


        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        invitationLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams IRLParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        invitationLayout.setLayoutParams(IRLParams);

        TextView inDivider = new TextView(this);
        inDivider.setId(View.generateViewId());
        addUserIcon = new ImageView(this);
        invitationLabel = new TextView(this);
        pendingLabel = new TextView(this);
        invitationLabel.setId(View.generateViewId());

        RelativeLayout.LayoutParams inLParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inLParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        RelativeLayout.LayoutParams addUserIconParams = new RelativeLayout.LayoutParams(48, 48);
        addUserIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        pendingLabelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pendingLabelParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        pendingLabel.setVisibility(View.GONE);


        RelativeLayout.LayoutParams inDividerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
        inDividerParams.addRule(RelativeLayout.BELOW, invitationLabel.getId());
        inDividerParams.setMargins(0, 20, 0, 10);


        if (!isRoster) {
            invitationLabel.setText(R.string.addContact);
            invitationLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            addUserIcon.setOnClickListener(this);
            addUserIcon.setId(R.id.addContact);
            addUserIcon.setImageResource(R.drawable.ic_add_user);

        } else {
            invitationLabel.setText(R.string.removeContact);
            invitationLabel.setTextColor(getResources().getColor(R.color.red));
            addUserIcon.setId(R.id.removeContact);
            addUserIcon.setOnClickListener(this);
            addUserIcon.setImageResource(R.drawable.ic_remove_user);
        }
        if (invitationLabel.getParent() != null)
            ((ViewGroup) invitationLabel.getParent()).removeView(invitationLabel);
        invitationLayout.addView(invitationLabel, inLParams);


        if (addUserIcon.getParent() != null)
            ((ViewGroup) addUserIcon.getParent()).removeView(addUserIcon);
        invitationLayout.addView(addUserIcon, addUserIconParams);

        invitationLayout.addView(inDivider, inDividerParams);

        if (invitationLayout.getParent() != null)
            ((ViewGroup) invitationLayout.getParent()).removeView(invitationLayout);
        linearLayout.addView(invitationLayout);
        /**
         * for testing till isRoster bug fixed !

         if( mContact.emailAddresses.get(0).toString().equals("mabedalkareem@asaltech.com")){
         invitationLabel.setText("Remove Contact from my Network");
         invitationLabel.setTextColor(getResources().getColor(R.color.red));
         addUserIcon.setId(R.id.removeContact);
         addUserIcon.setOnClickListener(this);
         addUserIcon.setImageResource(R.drawable.ic_remove_user);

         }
         /****/

        int emailsCount = mContact.emailAddresses.size();
        int phoneCount = mContact.phoneNumbers.size();

        for (int i = 0; i < emailsCount; i++) {
            TextView workEmail = new TextView(this);
            workEmail.setId(View.generateViewId());
            TextView workEmailValue = new TextView(this);
            workEmailValue.setId(View.generateViewId());
            ImageView icon = new ImageView(this);
            TextView divider = new TextView(this);
            divider.setId(View.generateViewId());

            RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(RLParams);

            RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
            dividerParams.addRule(RelativeLayout.BELOW, workEmailValue.getId());
            dividerParams.setMargins(0, 20, 0, 10);

            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(48, 48);
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams emailParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            RelativeLayout.LayoutParams emailValueParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailValueParams.addRule(RelativeLayout.BELOW, workEmail.getId());

            if (i == 0) {
                workEmail.setText(R.string.workEmail);
            } else {
                workEmail.setText(R.string.homeEmail);
            }

            workEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (workEmail.getParent() != null)
                ((ViewGroup) workEmail.getParent()).removeView(workEmail);
            relativeLayout.addView(workEmail, emailParams);

            String email = mContact.emailAddresses.get(i).toString();
            workEmailValue.setText(email);
            if (workEmailValue.getParent() != null)
                ((ViewGroup) workEmailValue.getParent()).removeView(workEmailValue);
            relativeLayout.addView(workEmailValue, emailValueParams);

            divider.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
            if (divider.getParent() != null)
                ((ViewGroup) divider.getParent()).removeView(divider);
            relativeLayout.addView(divider, dividerParams);

            icon.setImageResource(R.drawable.ic_email);
            icon.setId(R.id.sendEmail);
            if (icon.getParent() != null)
                ((ViewGroup) icon.getParent()).removeView(icon);
            icon.setOnClickListener(this);
            relativeLayout.addView(icon, iconParams);

            if (relativeLayout.getParent() != null)
                ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
            linearLayout.addView(relativeLayout);


        }

        for (int i = 0; i < phoneCount; i++) {
            TextView phoneLabel = new TextView(this);
            phoneLabel.setId(View.generateViewId());
            TextView phoneValue = new TextView(this);
            phoneValue.setId(View.generateViewId());
            ImageView icon = new ImageView(this);
            TextView divider = new TextView(this);

            RelativeLayout relativeLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(RLParams);

            RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
            dividerParams.addRule(RelativeLayout.BELOW, phoneValue.getId());
            dividerParams.setMargins(0, 20, 0, 10);


            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(48, 48);
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            RelativeLayout.LayoutParams emailParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            RelativeLayout.LayoutParams emailValueParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailValueParams.addRule(RelativeLayout.BELOW, phoneLabel.getId());

            if (i == 0) {
                mobileNum = mContact.phoneNumbers.get(i).toString();
                phoneLabel.setText(R.string.profMobile);
                icon.setImageResource(R.drawable.ic_phone);
                icon.setId(R.id.profMobile);
                icon.setOnClickListener(this);
            } else {
                phoneNum = mContact.phoneNumbers.get(i).toString();
                phoneLabel.setText(R.string.profPhone);
                icon.setImageResource(R.drawable.ic_mobile);
                icon.setId(R.id.profPhone);
                icon.setOnClickListener(this);
            }

            phoneLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (phoneLabel.getParent() != null)
                ((ViewGroup) phoneLabel.getParent()).removeView(phoneLabel);
            relativeLayout.addView(phoneLabel, emailParams);

            String phone = mContact.phoneNumbers.get(i).toString();
            phoneValue.setText(phone);
            if (phoneValue.getParent() != null)
                ((ViewGroup) phoneValue.getParent()).removeView(phoneValue);
            relativeLayout.addView(phoneValue, emailValueParams);

            divider.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
            if (divider.getParent() != null)
                ((ViewGroup) divider.getParent()).removeView(divider);
            relativeLayout.addView(divider, dividerParams);


            if (icon.getParent() != null)
                ((ViewGroup) icon.getParent()).removeView(icon);
            relativeLayout.addView(icon, iconParams);

            if (relativeLayout.getParent() != null)
                ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
            linearLayout.addView(relativeLayout);

        }

        RelativeLayout audioLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams RLParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        audioLayout.setLayoutParams(RLParams);
        TextView audioCall = new TextView(this);
        ImageView audioIcon = new ImageView(this);

        RelativeLayout.LayoutParams audioIconParams = new RelativeLayout.LayoutParams(48, 48);
        audioIconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        RelativeLayout.LayoutParams audioCallParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        audioCall.setText(R.string.audioCall);
        audioCall.setTextColor(getResources().getColor(R.color.colorPrimary));
        audioIcon.setImageResource(R.drawable.ic_audio_call);
        audioIcon.setId(R.id.audioCall);

        if (audioCall.getParent() != null)
            ((ViewGroup) audioCall.getParent()).removeView(audioCall);
        audioLayout.addView(audioCall, audioCallParams);

        if (audioIcon.getParent() != null)
            ((ViewGroup) audioIcon.getParent()).removeView(audioIcon);
        audioIcon.setOnClickListener(this);
        audioLayout.addView(audioIcon, audioIconParams);

        if (audioLayout.getParent() != null)
            ((ViewGroup) audioLayout.getParent()).removeView(audioLayout);
        linearLayout.addView(audioLayout);
    }

    @Override
    public void onClick(View view) {
        String mainEmail = mContact.emailAddresses.get(0);
        String id = mContact.corporateId;
        String jid = mContact.jId;
        switch (view.getId()) {
            case R.id.removeContact:
                RainbowSdk.instance().contacts().removeContactFromRoster(jid, mainEmail, mRemoveContactListener);
                break;
            case R.id.addContact:
                RainbowSdk.instance().contacts().addRainbowContactToRoster(id, mainEmail, mAddContactListener);
                break;
            case R.id.sendEmail:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.profMobile:
                Log.d(TAG, "onClick: "+mobileNum);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mobileNum));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE
                           );
                    return;
                }
                startActivity(callIntent);
                break;
            case R.id.profPhone:
                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:" + phoneNum));
                startActivity(callIntent2);
                break;
            case  R.id.audioCall:
                Log.d(TAG, "onClick: "+"audio clicked");
                Intent audioIntent= new Intent(getBaseContext(),audioCallActivity.class);
                startActivity(audioIntent);
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void updateInvitationLayout(String status) {
        switch (status) {
            case AFTER_Delete_USER:
                invitationLabel.setText(R.string.addContact);
                invitationLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                addUserIcon.setOnClickListener(this);
                addUserIcon.setId(R.id.addContact);
                addUserIcon.setImageResource(R.drawable.ic_add_user);
                break;
            case AFTER_ADD_USER:
                addUserIcon.setVisibility(View.GONE);
                pendingLabel.setText(R.string.pending);
                pendingLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
                pendingLabel.setVisibility(View.VISIBLE);
                if (pendingLabel.getParent() != null)
                    ((ViewGroup) pendingLabel.getParent()).removeView(pendingLabel);
                invitationLayout.addView(pendingLabel, pendingLabelParams);
                Log.d(TAG, "updateInvitationLayout: " + pendingLabel.getText());
                break;
        }


    }
}
