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
    public String corporateId ;
    public String jId;
    public ArrayList<String> emailAddresses = new ArrayList<>();
    public ArrayList<String> phoneNumbers = new ArrayList<>();

    public ContactData(String fullName ,String jobTitle , Bitmap profilePic, String presence, ArrayList<String> emailAddresses, ArrayList<String> phoneNumbers, String isRoster, String corporateId ,String jId) {
        this.jobTitle = jobTitle;
        this.fullName = fullName;
        this.profilePic = profilePic;
        this.presence = presence;
        this.isRoster = isRoster;
        this.corporateId = corporateId;
        this.emailAddresses = emailAddresses;
        this.phoneNumbers = phoneNumbers;
        this.jId=jId;
    }


    protected ContactData(Parcel in) {
        jobTitle = in.readString();
        fullName = in.readString();
        profilePic = in.readParcelable(Bitmap.class.getClassLoader());
        presence = in.readString();
        isRoster = in.readString();
        corporateId = in.readString();
        jId = in.readString();
        emailAddresses = in.createStringArrayList();
        phoneNumbers = in.createStringArrayList();
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
        parcel.writeString(isRoster);
        parcel.writeString(corporateId);
        parcel.writeString(jId);
        parcel.writeStringList(emailAddresses);
        parcel.writeStringList(phoneNumbers);
    }
}
