package com.example.mhasan.rainbowsdk.activites;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;


/**
 * Created by mhasan on 7/30/2017.
 *
 */

public class ContactData implements Parcelable {
    public String jobTitle;
    public String fullName;
    public Bitmap profilePic;
    public String presence;
    public String isRoster;
    public ArrayList<String> emailAddresses = new ArrayList<>();
    public ArrayList<String> phoneNumbers = new ArrayList< >();

    public ContactData(String jobTitle, String fullName, Bitmap profilePic, String presence,ArrayList<String> emailAddresses, ArrayList<String> phoneNumbers,String isRoster) {
        this.jobTitle = jobTitle;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.presence = presence;
        this.isRoster = isRoster;
        this.emailAddresses = emailAddresses;
        this.phoneNumbers = phoneNumbers;
    }

    protected ContactData(Parcel in) {
        jobTitle = in.readString();
        fullName = in.readString();
        profilePic = in.readParcelable(Bitmap.class.getClassLoader());
        presence = in.readString();
        isRoster = in.readString();
        emailAddresses = in.createStringArrayList();
        phoneNumbers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobTitle);
        dest.writeString(fullName);
        dest.writeParcelable(profilePic, flags);
        dest.writeString(presence);
        dest.writeString(isRoster);
        dest.writeStringList(emailAddresses);
        dest.writeStringList(phoneNumbers);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
