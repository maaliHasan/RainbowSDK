package com.example.mhasan.rainbowsdk.activites;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.ale.infra.contact.EmailAddress;
import com.ale.infra.contact.PhoneNumber;
import com.ale.infra.contact.RainbowPresence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by mhasan on 7/30/2017.
 *
 */

public class ContactData implements Parcelable {
    public String jobTitle;
    public String fullName;
    public Bitmap profilePic;
    public String presence;
    public Set<EmailAddress> emailAddresses = new HashSet<>();
    public Set<PhoneNumber> phoneNumbers = new HashSet< >();

    public ContactData(String jobTitle, String fullName, Bitmap profilePic, String presence, Set<EmailAddress> emailAddresses, Set<PhoneNumber> phoneNumbers) {
        this.jobTitle = jobTitle;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.presence = presence;
        this.emailAddresses = emailAddresses;
        this.phoneNumbers = phoneNumbers;
    }


    protected ContactData(Parcel in) {
        jobTitle = in.readString();
        fullName = in.readString();
        profilePic = in.readParcelable(Bitmap.class.getClassLoader());
        presence = in.readString();
    }

    public static final Creator<ContactData> CREATOR = new Creator<ContactData>() {
        @Override
        public ContactData createFromParcel(Parcel in) {
            return new ContactData(in);
        }

        @Override
        public ContactData[] newArray(int size) {
            return new ContactData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(jobTitle);
        parcel.writeString(fullName);
        parcel.writeParcelable(profilePic, i);
        parcel.writeString(presence);
    }
}
