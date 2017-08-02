package com.example.mhasan.rainbowsdk.activites;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.GridLayout.LayoutParams;

import com.example.mhasan.rainbowsdk.R;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.mhasan.rainbowsdk.R.id.linearLayout;


/**
 * Created by mhasan on 7/30/2017.
 *
 */

public class ContactDetails extends AppCompatActivity {
    ContactData mContact;

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
        mContact = (ContactData) data.getParcelable("ContactData");
       boolean isRoster=Boolean.valueOf(mContact.isRoster);

        fullName.setText(mContact.fullName);
        jobTitle.setText(mContact.jobTitle);
        presence.setText(mContact.presence);
        if ((mContact.profilePic) != null) {
            pic.setImageBitmap(mContact.profilePic);
        } else {
            pic.setImageResource(R.drawable.ic_placeholder);
        }


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int emailsCount = mContact.emailAddresses.size();
        int phoneCount = mContact.phoneNumbers.size();

        for (int i = 0; i < emailsCount; i++) {
            TextView workEmail = new TextView(this);
            workEmail.setId(View.generateViewId());
            TextView workEmailValue = new TextView(this);
            workEmailValue.setId(View.generateViewId());
            ImageView icon = new ImageView(this);
            ImageView addUserIcon = new ImageView(this);
            TextView divider = new TextView(this);
            divider.setId(View.generateViewId());
            TextView invitationLabel = new TextView(this);

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

            RelativeLayout.LayoutParams inLParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            inLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);

            RelativeLayout.LayoutParams emailValueParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            emailValueParams.addRule(RelativeLayout.BELOW, workEmail.getId());

            if (i == 0) {
                workEmail.setText("Work-Email");
            } else {
                workEmail.setText("Home-Email");
            }

            workEmail.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (workEmail.getParent() != null)
                ((ViewGroup) workEmail.getParent()).removeView(workEmail);
            relativeLayout.addView(workEmail, emailParams);

            String EMAIL = mContact.emailAddresses.get(i).toString();
            workEmailValue.setText(EMAIL);
            if (workEmailValue.getParent() != null)
                ((ViewGroup) workEmailValue.getParent()).removeView(workEmailValue);
            relativeLayout.addView(workEmailValue, emailValueParams);

            divider.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
            if (divider.getParent() != null)
                ((ViewGroup) divider.getParent()).removeView(divider);
            relativeLayout.addView(divider, dividerParams);

            icon.setImageResource(R.drawable.ic_email);
            if (icon.getParent() != null)
                ((ViewGroup) icon.getParent()).removeView(icon);
            relativeLayout.addView(icon, iconParams);

            if (relativeLayout.getParent() != null)
                ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
            linearLayout.addView(relativeLayout);
//
//            if(!isRoster){
//                if (divider.getParent() != null)
//                    ((ViewGroup) divider.getParent()).removeView(divider);
//                relativeLayout.addView(divider, dividerParams);
//                invitationLabel.setText("Add Contact to my Network");
//                invitationLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
//                if (invitationLabel.getParent() != null)
//                    ((ViewGroup) invitationLabel.getParent()).removeView(invitationLabel);
//                relativeLayout.addView(invitationLabel, inLParams);
//
//
//            }


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
                phoneLabel.setText("Professional-mobile");
                icon.setImageResource(R.drawable.ic_phone);
            } else {
                phoneLabel.setText("Professional-phone");
                icon.setImageResource(R.drawable.ic_mobile);
            }

            phoneLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (phoneLabel.getParent() != null)
                ((ViewGroup) phoneLabel.getParent()).removeView(phoneLabel);
            relativeLayout.addView(phoneLabel, emailParams);

            String EMAIL = mContact.phoneNumbers.get(i).toString();
            phoneValue.setText(EMAIL);
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






    }
}
