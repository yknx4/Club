package com.yknx.android.club.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yknx.android.club.Utility;

/**
 * Created by Yknx on 04/01/2015.
 */
public class Preferences {

    public static final int DEFAULT_VALUE_INT = 0 ;
    public static final long DEFAULT_VALUE_LONG = 0L ;

    public static final String PREF_SELECTED_TERM = "selected_term";
    public static final String PREF_LAST_SELECTED_CLUB = "pref_last_selected_club";
    public static final String PREF_LAST_SELECTED_POSITION = "pref_last_selected_POSITION";
    public static final String PREFERENCES_FILE = "com.yknx.android.club.preferences";

    public static int getSelectedTerm(Context context, Long clubId){
       return readSharedSettingInt(context, PREF_SELECTED_TERM + clubId, 0);
       // return readSharedSettingInt(context, PREF_SELECTED_TERM, DEFAULT_VALUE_INT);
   }
    public static void writeSelectedTerm(Context context, long clubId, int term){
       saveSharedSettingInt(context, PREF_SELECTED_TERM + clubId, term);
    }

   public static long getLastSelectedClub(Context context){
       return readSharedSettingLong(context, PREF_LAST_SELECTED_CLUB, DEFAULT_VALUE_LONG);
   }
    public static int getLastSelectedPosition(Context context){
        return readSharedSettingInt(context, PREF_LAST_SELECTED_POSITION, DEFAULT_VALUE_INT);
    }


    public static void saveSharedSettingString(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        //editor.apply();
        editor.commit();
    }

    public static void saveSharedSettingInt(Context ctx, String settingName, int settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(settingName, settingValue);
        //editor.apply();
        editor.commit();
    }

    public static void saveSharedSettingLong(Context ctx, String settingName, Long settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(settingName, settingValue);
        //editor.apply();
        editor.commit();
    }
    public static String readSharedSettingString(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPref.getString(settingName, defaultValue);
    }

    public static int readSharedSettingInt(Context ctx, String settingName, int defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getInt(settingName, defaultValue);
    }

    public static long readSharedSettingLong(Context context, String settingName, long defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getLong(settingName, defaultValue);
    }
}
