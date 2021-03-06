package com.yknx.android.club;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yknx.android.club.model.ClubsContract;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.Preferences;

import java.util.Date;
import java.util.Random;

/**
 * Created by Yknx on 08/08/2014.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();
    private static final String CLUB_ID = "club_id";
    private static final String USER_ID = "user_id";


    public static ContentValues createClubValues(Club club) {
        String fakeName = club.name;
        String fakeColor = club.color;
        String fakeIcon = "";
        if (club.icon != null)
            fakeIcon = club.icon.toString();
        int terms = club.terms;
        int atLeast = club.atLeast;

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_NAME, fakeName);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR, fakeColor);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI, fakeIcon);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS, terms);
        values.put(ClubsContract.ClubEntry.COLUMN_CLUB_ATLEAST, atLeast);
        return values;
    }

    public static ContentValues createClubValues(String name, String color) {
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

    public static ContentValues createFakeClubValues() {
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

    public static ContentValues createFakeClubValues2() {
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

    public static ContentValues createFakeUserValues1() {
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

    public static ContentValues createFakeUserValues2() {
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

    public static ContentValues createRegistration(long club, long user) {
        ContentValues testValues = new ContentValues();
        testValues.put(ClubsContract.RegistrationEntry.COLUMN_REGISTRATION_CLUB, club);
        testValues.put(ClubsContract.RegistrationEntry.COLUMN_REGISTRATION_USER, user);
        return testValues;
    }

    public static ContentValues createFakeAssist(long registrationId, int term) {
        ContentValues testValues = new ContentValues();

        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_REGISTRATION, registrationId);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_TERM, term);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE, "201409101314");
        return testValues;
    }

    public static ContentValues createAssist(long registrationId, int term) {
        ContentValues testValues = new ContentValues();

        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_REGISTRATION, registrationId);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_TERM, term);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE, ClubsContract.getDbDateString(new Date()));
        return testValues;
    }

    public static void setHeader(View rootView, long clubId, Context mContext) {
        setHeader(rootView, mContext.getContentResolver().query(ClubsContract.ClubEntry.buildClubUri(clubId), null, null, null, null), mContext);
    }

    public static void setHeader(View rootView, Cursor cursor, Context mContext) {
        setHeader(rootView, 0, cursor, mContext);

    }


    public static void setActionBarColorFromBaseColor(int color, Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int darkerColor = darker(color, .8F);
            Window window = ((Activity) context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(darkerColor);
        }
    }
    public static void setActionBarColorFromClubColor(Club club, Context context){

            int color = Color.parseColor(club.color);
            setActionBarColorFromBaseColor(color, context);

    }


    public static void updateHeaderTerm(View view, Long clubId, Context mContext) {
        TextView termView = (TextView) view.findViewById(R.id.fragment_club_select_term_textview);
        String term = mContext.getString(R.string.format_terms, Preferences.getSelectedTerm(mContext,clubId));
        if (termView != null) termView.setText(term);
    }
    public static void setHeader(View rootView, int position, Cursor cursor, Context mContext) {
        if (!cursor.moveToPosition(position)) return;
        String icon_uri = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI));
        String name = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_NAME));
        long clubId = cursor.getLong(cursor.getColumnIndex(ClubsContract.ClubEntry._ID));

        int termps = cursor.getInt(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS));

        LinearLayout header = (LinearLayout) rootView.findViewById(R.id.fragment_club_select_header);
        ImageView headerIcon = (ImageView) rootView.findViewById(R.id.fragment_club_select_club_icon);
        TextView nameView = (TextView) rootView.findViewById(R.id.fragment_club_select_title_textview);
        TextView usersView = (TextView) rootView.findViewById(R.id.fragment_club_select_users_textview);
        TextView termView = (TextView) rootView.findViewById(R.id.fragment_club_select_term_textview);

        Cursor clubs = mContext.getContentResolver().query(ClubsContract.RegistrationEntry.builRegistrationUriWithClub(clubId), null, null, null, null);


        Random mRandom = new Random();

        String users = mContext.getString(R.string.format_users, clubs.getCount());
        String term = mContext.getString(R.string.format_terms, Preferences.getSelectedTerm(mContext,clubId));


        if (nameView != null) nameView.setText(name);
        if (usersView != null) usersView.setText(users);
        if (termView != null) termView.setText(term);

        int color = Color.parseColor(cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR)));
        if (header == null || headerIcon == null) return;
        header.setBackgroundColor(color);

        setActionBarColorFromBaseColor(color,mContext);


        if (icon_uri == null || icon_uri.equals("")) {
            //headerIcon.setImageResource(R.drawable.ic_action_about);
            headerIcon.setImageBitmap(null);
        } else headerIcon.setImageURI(Uri.parse(icon_uri));
    }

    /**
     * Returns darker version of specified <code>color</code>.
     */
    public static int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    public static Drawable getIcon(long id) {
        return null;
    }

    public static void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
    public static void hideKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
    
    public static Bundle putClub(Club club, Bundle bundle){
        return putClub(club.id,bundle);
    }
    public static Bundle putClub(long id, Bundle bundle){
        return putID(id, CLUB_ID,bundle);
    }
    public static Bundle putUser(User user, Bundle bundle) {return putUser(user.getId(),bundle);}

    public static Bundle putUser(long id, Bundle bundle) {
        return putID(id,USER_ID,bundle);
    }

    public static Bundle putID(long id, String paramName, Bundle bundle){
        if(bundle == null) bundle = new Bundle();
        bundle.putLong(paramName, id);
        return bundle;
    }


}
