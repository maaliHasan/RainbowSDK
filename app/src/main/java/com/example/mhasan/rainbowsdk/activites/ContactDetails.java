package com.example.mhasan.rainbowsdk.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.mhasan.rainbowsdk.R;



import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mhasan on 7/30/2017.
 *
 */

public class ContactDetails extends AppCompatActivity {
    ContactData mContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_deatails);
        getContactData();
    }

    public void getContactData() {
        TextView fullName = (TextView) findViewById(R.id.fullName);
        TextView presence = (TextView) findViewById(R.id.presence);
        TextView jobTitle = (TextView) findViewById(R.id.jobTitle);
        CircleImageView pic = (CircleImageView) findViewById(R.id.profile_pic);

        Bundle data = getIntent().getExtras();
        mContact = (ContactData) data.getParcelable("ContactData");

        fullName.setText(mContact.fullName);
        jobTitle.setText(mContact.jobTitle);
      //  presence.setText("Online");
        presence.setText(mContact.presence);
        if ((mContact.profilePic) != null) {
            pic.setImageBitmap(mContact.profilePic);
        } else {
            pic.setImageResource(R.drawable.ic_placeholder);
        }



    }
}
