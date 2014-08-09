package com.yknx.android.club;

import android.content.ContentValues;

import com.yknx.android.club.data.ClubsContract;

/**
 * Created by Yknx on 08/08/2014.
 */
public class Utility {

    public   static ContentValues createClubValues(String name, String color) {
        String fakeName = name;
        String fakeColor = color;
        String fakeIcon = "";
        int terms = 1;
        int atLeast = ClubsContract.ClubEntry.AT_LEAST_DISABLED;

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_NAME, fakeName);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR, fakeColor);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI, fakeIcon);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS, terms);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_ATLEAST, atLeast);
        return values;
    }

    public   static ContentValues createFakeClubValues() {
        String fakeName = "댄스";
        String fakeColor = "#007FFF";
        String fakeIcon = "";
        int terms = 3;
        int atLeast = 2;

// Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_NAME, fakeName);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR, fakeColor);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI, fakeIcon);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS, terms);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_ATLEAST, atLeast);
        return testValues;
    }
    public   static ContentValues createFakeClubValues2() {
        String fakeName = "倶楽部";
        String fakeColor = "#CC8400";
        String fakeIcon = "";
        int terms = 3;
        int atLeast = 2;

// Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_NAME, fakeName);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR, fakeColor);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI, fakeIcon);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS, terms);
        testValues.put(ClubsContract.ClubEntry.COLUMN_CLUB_ATLEAST, atLeast);
        return testValues;
    }

    public  static ContentValues createFakeUserValues1() {
        String fakeName = "Jorge Figueroa";
        String fakeAccount = "20094894";
        String fakeCampus = "Telematica";
        String fakeEmail = "yknx.4.b@gmail.com";
        String fakePicture = "";
        String fakeNote = "";


// Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_NAME, fakeName);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_ACCOUNT, fakeAccount);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_PICTURE, fakePicture);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_NOTE, fakeNote);

        return testValues;
    }

    public  static ContentValues createFakeUserValues2() {
        String fakeName = "Mariana Preciado";
        String fakeAccount = "20094476";
        String fakeCampus = "Psicologia";
        String fakeEmail = "mariana_cma_94@hotmail.com";
        String fakePicture = "";
        String fakeNote = "Se la come :)";


// Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_NAME, fakeName);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_ACCOUNT, fakeAccount);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_PICTURE, fakePicture);
        testValues.put(ClubsContract.UserEntry.COLUMN_USER_NOTE, fakeNote);

        return testValues;
    }

    public  static ContentValues createRegistration(long club, long user) {
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.RegistrationEntry.COLUMN_REGISTRATION_CLUB, club);
        testValues.put(ClubsContract.RegistrationEntry.COLUMN_REGISTRATION_USER, user);
        return testValues;
    }

    public   static ContentValues createFakeAssist(long registrationId, int term) {
        ContentValues testValues = new ContentValues();

        testValues.put(ClubsContract.AssistEntry.COLUMN_ASSIST_REGISTRATION,registrationId);
        testValues.put(ClubsContract.AssistEntry.COLUMN_ASSIST_TERM, term);
        testValues.put(ClubsContract.AssistEntry.COLUMN_ASSIST_DATE, "20140910");
        return testValues;
    }
}
