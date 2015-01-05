package com.yknx.android.club.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yknx on 07/08/2014.
 */
public class ClubsContract {
    public static final String CONTENT_AUTHORITY = "com.yknx.android.club";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CLUB = "club";
    public static final String PATH_USER = "user";
    public static final String PATH_REGISTRATIONS = "registrations";
    public static final String PATH_ASSISTS = "assists";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String LOG_TAG = ClubsContract.class.getSimpleName();

    public static Date getDateFromDb(String dateString) {
        dateString = dateString.replaceAll("/", "");
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Log.d(LOG_TAG,"getDateFromDb: "+dateString);
            return dbDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_TAG,e.getMessage(),e);
            return null;
        }

    }

    public static String getDbDateString(Date date) {
// Because the API returns a unix timestamp (measured in seconds),
// it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String red = sdf.format(date);
        Log.d(LOG_TAG,"getDbDateString: "+red);

        return red;
    }

    public static final class ClubEntry implements BaseColumns {
        /*
        *   ACCESABLE VIA
        *  /club
        *  /club/id (#)
        *
        * */


        public static int AT_LEAST_DISABLED = -1;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLUB).build();
        public static final String TABLE_NAME = "clubs";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CLUB;
        public static final String COLUMN_CLUB_NAME = "name";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CLUB;
        public static final String COLUMN_CLUB_ICON_URI = "icon";
        public static final String COLUMN_CLUB_TERMS = "terms";
        public static final String COLUMN_CLUB_ATLEAST = "at_least";
        public static final String COLUMN_CLUB_COLOR = "color";
        public static Uri buildClubUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class UserEntry implements BaseColumns {
        /*
        *   ACCESABLE VIA
        *  /users
        *  /users/id/#
        *  /users/account/*
        *
        * */
        public static final String ACCOUNT_PATH = "account";
        public static final String ID_PATH = "id";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        public static final String TABLE_NAME = "users";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String COLUMN_USER_NAME = "name";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String COLUMN_USER_ACCOUNT = "account";
        public static final String COLUMN_USER_CAMPUS = "campus";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_PICTURE = "picture";
        public static final String COLUMN_USER_NOTE = "note";
        public static Uri buildUserUri(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(ID_PATH).build();
            Log.d(LOG_TAG,"buildUserUri(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }
        public static Uri builUserUriWithAccount(String account) {
            Uri finalUri =CONTENT_URI.buildUpon().appendPath(ACCOUNT_PATH).appendPath(account).build();
            Log.d(LOG_TAG,"buildUserUriWithAccount(String): "+finalUri);
            return finalUri;

        }
        public static String getAccoutFromUri(Uri uri){
            return uri.getLastPathSegment();
        }


    }

    public static final class RegistrationEntry implements BaseColumns {
        /*
        *   ACCESABLE VIA
        *  /registration
        *  /registration/club/#
        *  /registration/user/#
        *
        * */
        public static final int NO_REGISTRATION = -1;
        public static final String CLUB_PATH = "club";
        public static final String USER_PATH = "user";
        public static final String ID_PATH = "id";

         public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REGISTRATIONS).build();
        public static final String TABLE_NAME = "registrations";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_REGISTRATIONS;
        public static final String COLUMN_REGISTRATION_CLUB = "club";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_REGISTRATIONS;
        public static final String COLUMN_REGISTRATION_USER = "user";

        public static Uri builRegistrationUriWithClub(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(CLUB_PATH).build();
            Log.d(LOG_TAG,"buildRegistrationUriWithClub(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }

        public static Uri builRegistrationUriWithUser(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(USER_PATH).build();
            Log.d(LOG_TAG,"buildRegistrationUriWithUser(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }

        public static Uri builRegistrationUri(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(ID_PATH).build();
            Log.d(LOG_TAG,"buildRegistrationUri(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }

        public static String getClubFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
        public static String getUserFromUri(Uri uri){
            return uri.getLastPathSegment();
        }



    }

    public static final class AttendanceEntry implements BaseColumns {
        /*
       *   ACCESABLE VIA
       *  /assists
       *  /assists/registration/#
       *  /assists/registration/#/term (#)
       *  /assists/date/*
       *
       * */
        public static final String REGISTRATION_PATH = "registration";
        public static final String DATE_PATH = "date";
        public static final String ID_PATH = "id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ASSISTS).build();
        public static final String TABLE_NAME = "assists";
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ASSISTS;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ASSISTS;
        public static final String COLUMN_ASSIST_REGISTRATION = "registration";
        public static final String COLUMN_ASSIST_TERM = "term";
        public static final String COLUMN_ASSIST_DATE = "date";

        public static Uri buildAssistUriWithRegistration(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(REGISTRATION_PATH).build();
            Log.d(LOG_TAG,"buildAssistUriWithRegistration(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }
        public static Uri buildAssistUriWithRegistrationAndTerm(long id, int term){
            Uri finalUri = ContentUris.withAppendedId(buildAssistUriWithRegistration(id),term);
            Log.d(LOG_TAG,"buildAssistUriWithRegistrationAndTerm(long, int): "+finalUri);
            return  finalUri;
        }

        public static Uri buildAssistUriWithDate(String date) {
            Uri finalUri =CONTENT_URI.buildUpon().appendPath(DATE_PATH).appendPath(date).build();
            Log.d(LOG_TAG,"buildAssistUriWithDate(String): "+finalUri);
            return finalUri;

        }
        public static Uri builAssistUri(long id) {
            Uri contentUri = CONTENT_URI.buildUpon().appendPath(ID_PATH).build();
            Log.d(LOG_TAG,"buildRegistrationUri(long): "+contentUri+ " with id "+id);
            return ContentUris.withAppendedId(contentUri, id);
        }
        public static String getRegistrationFromUri(Uri uri){

            return uri.getPathSegments().get(2);
        }
        public static String getDateFromUri(Uri uri){

            return uri.getLastPathSegment();
        }


    }
}
