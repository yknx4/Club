package com.yknx.android.club.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.yknx.android.club.Utility;
import com.yknx.android.club.data.ClubsContract.AssistEntry;
import com.yknx.android.club.data.ClubsContract.ClubEntry;
import com.yknx.android.club.data.ClubsContract.RegistrationEntry;
import com.yknx.android.club.data.ClubsContract.UserEntry;
import com.yknx.android.club.model.Club;

/**
 * Created by Yknx on 07/08/2014.
 */
public class ClubsProvider extends ContentProvider {
    private static final String LOG_TAG = ClubsProvider.class.getSimpleName();

    private static final int ASSIST = 100;
    private static final int ASSIST_WITH_REGISTRATION = 101;
    private static final int ASSIST_WITH_REGISTRATION_AND_TERM = 102;
    private static final int ASSIST_WITH_DATE = 103;
    private static final int ASSIST_ID = 104;

    private static final int CLUB = 300;
    private static final int CLUB_ID = 301;

    private static final int USER = 500;
    private static final int USER_ID = 501;
    private static final int USER_ACCOUNT = 502;

    private static final int REGISTRATION = 700;
    private static final int REGISTRATION_ID = 703;
    private static final int REGISTRATION_CLUB = 701;
    private static final int REGISTRATION_USER = 702;


    private static final String URI_AUTORITHY = ClubsContract.CONTENT_AUTHORITY;


    private static final SQLiteQueryBuilder sAssistQueryBuilder;
    private static final SQLiteQueryBuilder sRegistrationQueryBuilder;

    static {
        sAssistQueryBuilder = new SQLiteQueryBuilder();
        sAssistQueryBuilder.setTables(
                AssistEntry.TABLE_NAME + " INNER JOIN " +
                        RegistrationEntry.TABLE_NAME +
                        " ON " + AssistEntry.TABLE_NAME +
                        "." + AssistEntry.COLUMN_ASSIST_REGISTRATION +
                        " = " + RegistrationEntry.TABLE_NAME +
                        "." + RegistrationEntry._ID
        );
    }
    //TODO: sRegistrationQueryBuilder
    static {
        sRegistrationQueryBuilder = new SQLiteQueryBuilder();
        sRegistrationQueryBuilder.setTables(
                RegistrationEntry.TABLE_NAME + " INNER JOIN " +
                        ClubEntry.TABLE_NAME +
                        " ON " + RegistrationEntry.TABLE_NAME +
                        "." + RegistrationEntry.COLUMN_REGISTRATION_CLUB +
                        " = " + ClubEntry.TABLE_NAME +
                        "." + ClubEntry._ID
        );
    }
//
//    private static final String sLocationSettingSelection =
//            WeatherContract.LocationEntry.TABLE_NAME +
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_POSTALCODE + " = ? AND " +
//                    WeatherContract.LocationEntry.COLUMN_LOCATION_COUNTRYCODE + "= ?";
//    private static final String sLocationSettingWithStartDateSelection =
//            WeatherContract.LocationEntry.TABLE_NAME +
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_POSTALCODE + " = ? AND " +
//                    WeatherContract.LocationEntry.COLUMN_LOCATION_COUNTRYCODE + "= ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATETEXT + " >= ? ";
//    private static final String sLocationSettingWithDaySelectionn =
//            WeatherContract.LocationEntry.TABLE_NAME +
//                    "." + WeatherContract.LocationEntry.COLUMN_LOCATION_POSTALCODE + " = ? AND " +
//                    WeatherContract.LocationEntry.COLUMN_LOCATION_COUNTRYCODE + "= ? AND " +
//                    WeatherContract.WeatherEntry.COLUMN_DATETEXT + " = ? ";


    private UriMatcher mUriMatcher = buildUriMatcher();
    private ClubsDbHelper mOpenHelper;

    private static void addMatchUri(UriMatcher matcher, String Path, int code) {
        matcher.addURI(URI_AUTORITHY, Path, code);
        Log.d(LOG_TAG,"Added uri "+URI_AUTORITHY+"/"+Path+" with code "+code);
    }

    private static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        addMatchUri(matcher, ClubsContract.PATH_CLUB, CLUB);
        addMatchUri(matcher, ClubsContract.PATH_CLUB + "/#", CLUB_ID);

        addMatchUri(matcher, ClubsContract.PATH_USER, USER);
        addMatchUri(matcher, ClubsContract.PATH_USER + "/" + UserEntry.ID_PATH + "/#", USER_ID);
        addMatchUri(matcher, ClubsContract.PATH_USER + "/" + UserEntry.ACCOUNT_PATH + "/*", USER_ACCOUNT);

        addMatchUri(matcher, ClubsContract.PATH_REGISTRATIONS, REGISTRATION);
        addMatchUri(matcher, ClubsContract.PATH_REGISTRATIONS + "/" + RegistrationEntry.CLUB_PATH + "/#", REGISTRATION_CLUB);
        addMatchUri(matcher, ClubsContract.PATH_REGISTRATIONS + "/" + RegistrationEntry.USER_PATH + "/#", REGISTRATION_USER);
        addMatchUri(matcher,ClubsContract.PATH_REGISTRATIONS+"/"+RegistrationEntry.ID_PATH+"/#",REGISTRATION_ID);

        addMatchUri(matcher, ClubsContract.PATH_ASSISTS, ASSIST);
        addMatchUri(matcher, ClubsContract.PATH_ASSISTS + "/" + AssistEntry.REGISTRATION_PATH + "/#", ASSIST_WITH_REGISTRATION);
        addMatchUri(matcher, ClubsContract.PATH_ASSISTS + "/" + AssistEntry.REGISTRATION_PATH + "/#/#", ASSIST_WITH_REGISTRATION_AND_TERM);
        addMatchUri(matcher, ClubsContract.PATH_ASSISTS + "/" + AssistEntry.DATE_PATH + "/*", ASSIST_WITH_DATE);
        addMatchUri(matcher, ClubsContract.PATH_ASSISTS + "/" + AssistEntry.ID_PATH + "/#", ASSIST_ID);


        //matcher.addURI(URI_AUTORITHY,WeatherContract.PATH_WEATHER, ASSIST);
        //matcher.addURI(URI_AUTORITHY,WeatherContract.PATH_WEATHER+"/*", ASSIST_WITH_REGISTRATION);
        //matcher.addURI(URI_AUTORITHY,WeatherContract.PATH_WEATHER+"/*/*",WEATHER_WITH_LOCATION_AND_DATE);
        //matcher.addURI(URI_AUTORITHY,WeatherContract.PATH_LOCATION,LOCATION);
        //matcher.addURI(URI_AUTORITHY,WeatherContract.PATH_LOCATION+"/#",LOCATION_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ClubsDbHelper(getContext());
        return true;
    }

    private Cursor basicQuery(String TableName, String[] projection, String selection, String[] selectionArgs,
                              String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(
                TableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    private Cursor basicQueryWithDbAndQueryBuilder(SQLiteQueryBuilder qBuilder,SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs,
                                    String sortOrder){
        return  qBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);

    }

    private Cursor basicQueryWithId(String TableName, String idRowName, long id, String[] projection, String sortOrder) {
        String idSelection = idRowName + " = '" + id + "'";
        return basicQuery(TableName, projection, idSelection, null, sortOrder);

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (mUriMatcher.match(uri)) {

            // "location"
            case CLUB: {
                retCursor = basicQuery(ClubEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case USER: {
                retCursor = basicQuery(UserEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case REGISTRATION: {
                retCursor = basicQuery(RegistrationEntry.TABLE_NAME, projection, selection, selectionArgs, sortOrder);
                break;
            }
            case ASSIST: {
                retCursor = basicQueryWithDbAndQueryBuilder(sAssistQueryBuilder,mOpenHelper.getWritableDatabase(),projection,selection,selectionArgs,sortOrder);
                break;
            }
            case CLUB_ID: {
                retCursor = basicQueryWithId(ClubEntry.TABLE_NAME, ClubEntry._ID, ContentUris.parseId(uri), projection, sortOrder);
                break;
            }
            case USER_ID: {
                retCursor = basicQueryWithId(UserEntry.TABLE_NAME, UserEntry._ID, ContentUris.parseId(uri), projection, sortOrder);
                break;
            }
            case ASSIST_ID:{
                retCursor = basicQueryWithId(AssistEntry.TABLE_NAME,AssistEntry._ID,ContentUris.parseId(uri),projection,sortOrder);
                break;
            }
            case REGISTRATION_ID:{
                retCursor = basicQueryWithId(RegistrationEntry.TABLE_NAME,RegistrationEntry._ID,ContentUris.parseId(uri),projection,sortOrder);
                break;
            }

            case USER_ACCOUNT: {
                String account = UserEntry.getAccoutFromUri(uri);
                String selectionA = UserEntry.COLUMN_USER_ACCOUNT + " = ? ";
                String[] selectionArgsA = new String[]{account};
                retCursor = basicQuery(UserEntry.TABLE_NAME, projection, selectionA, selectionArgsA, sortOrder);
                break;
            }
            case REGISTRATION_CLUB: {
                String club = RegistrationEntry.getClubFromUri(uri);
                String selectionC = RegistrationEntry.COLUMN_REGISTRATION_CLUB + " = ?";
                String[] selectionArgsC = new String[]{club};
                retCursor = basicQuery(RegistrationEntry.TABLE_NAME, projection, selectionC, selectionArgsC, sortOrder);
                break;
            }
            case REGISTRATION_USER: {
                String user = RegistrationEntry.getUserFromUri(uri);
                String selectionU = RegistrationEntry.COLUMN_REGISTRATION_USER + " = ?";
                String[] selectionArgsU = new String[]{user};
                retCursor = basicQuery(RegistrationEntry.TABLE_NAME, projection, selectionU, selectionArgsU, sortOrder);
                break;
            }
            case ASSIST_WITH_REGISTRATION: {
                long registration = ContentUris.parseId(uri);
                String customSelection = AssistEntry.COLUMN_ASSIST_REGISTRATION + " = ?";
                String[] customSelectionArgs = new String[]{registration + ""};
                retCursor = basicQueryWithDbAndQueryBuilder(sAssistQueryBuilder,mOpenHelper.getWritableDatabase(),projection,customSelection,customSelectionArgs,sortOrder);
//                retCursor = basicQuery(RegistrationEntry.TABLE_NAME, projection, customSelection, customSelectionArgs, sortOrder);
                break;
            }
            case ASSIST_WITH_REGISTRATION_AND_TERM: {
                String registration = AssistEntry.getRegistrationFromUri(uri);
                long term = ContentUris.parseId(uri);
                String customSelection = AssistEntry.COLUMN_ASSIST_REGISTRATION + " = ? AND " +
                        AssistEntry.COLUMN_ASSIST_TERM + " = ? ";
                String[] customSelectionArgs = new String[]{registration, term + ""};
                retCursor = basicQueryWithDbAndQueryBuilder(sAssistQueryBuilder,mOpenHelper.getWritableDatabase(),projection,customSelection,customSelectionArgs,sortOrder);
                break;
            }
            case ASSIST_WITH_DATE: {
                String date = AssistEntry.getDateFromUri(uri);
                String customSelection = AssistEntry.COLUMN_ASSIST_DATE + " = ?";
                String[] customSelectionArgs = new String[]{date};
                retCursor = basicQueryWithDbAndQueryBuilder(sAssistQueryBuilder,mOpenHelper.getWritableDatabase(),projection,customSelection,customSelectionArgs,sortOrder);
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case CLUB:
                return ClubEntry.CONTENT_TYPE;
            case CLUB_ID:
                return ClubEntry.CONTENT_ITEM_TYPE;

            case USER:
                return UserEntry.CONTENT_TYPE;
            case USER_ID:
            case USER_ACCOUNT:
                return UserEntry.CONTENT_ITEM_TYPE;

            case REGISTRATION_ID:
                return RegistrationEntry.CONTENT_ITEM_TYPE;
            case REGISTRATION:
            case REGISTRATION_CLUB:
            case REGISTRATION_USER:
                return RegistrationEntry.CONTENT_TYPE;

            case ASSIST_ID:
                return  AssistEntry.CONTENT_ITEM_TYPE;
            case ASSIST:
            case ASSIST_WITH_REGISTRATION:
            case ASSIST_WITH_REGISTRATION_AND_TERM:
            case ASSIST_WITH_DATE:
                return AssistEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri = null;
        long _id;
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ASSIST:
                _id = db.insert(AssistEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = AssistEntry.builAssistUri(_id);
                } else throw new SQLException("Failed to insert row into " + uri);
                break;
            case CLUB:
                _id = db.insert(ClubEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = ClubEntry.buildClubUri(_id);
                } else throw new SQLException("Failed to insert row into " + uri);
                break;
            case USER:
                _id = db.insert(UserEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = UserEntry.buildUserUri(_id);
                } else throw new SQLException("Failed to insert row into " + uri);
                break;
            case REGISTRATION:
                _id = db.insert(RegistrationEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = RegistrationEntry.builRegistrationUri(_id);
                } else throw new SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri toNotify = null;
        int affected;
        final int match = mUriMatcher.match(uri);
        switch (match) {

            case ASSIST:
                affected = db.delete(AssistEntry.TABLE_NAME, selection, selectionArgs);
                if (affected > 0) {
                    toNotify = AssistEntry.CONTENT_URI;
                } else Log.w(LOG_TAG, "Failed to delete  " + uri);
                break;

            case CLUB:
                affected = db.delete(ClubEntry.TABLE_NAME, selection, selectionArgs);
                if (affected > 0) {
                    toNotify = ClubEntry.CONTENT_URI;
                } else Log.w(LOG_TAG, "Failed to delete  " + uri);
                break;

            case USER:
                affected = db.delete(UserEntry.TABLE_NAME, selection, selectionArgs);
                if (affected > 0) {
                    toNotify = UserEntry.CONTENT_URI;
                } else Log.w(LOG_TAG, "Failed to delete  " + uri);
                break;

            case REGISTRATION:
                affected = db.delete(RegistrationEntry.TABLE_NAME, selection, selectionArgs);
                if (affected > 0) {
                    toNotify = RegistrationEntry.CONTENT_URI;
                } else Log.w(LOG_TAG, "Failed to delete  " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (toNotify != null) getContext().getContentResolver().notifyChange(toNotify, null);
        return affected;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return  super.bulkInsert(uri,values);
    }


    private int basicUpdate(String tableName,Uri toNotify, ContentValues values, String selection, String[] selectionArgs){
        int affected = mOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
        if (affected > 0) {
            getContext().getContentResolver().notifyChange(toNotify, null);
        } else throw new SQLException("Failed to update row");
        return  affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int affected;
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case CLUB:{
                affected = basicUpdate(ClubEntry.TABLE_NAME,ClubEntry.CONTENT_URI,values,selection,selectionArgs);
                break;
            }
            case USER:{
                affected = basicUpdate(UserEntry.TABLE_NAME,UserEntry.CONTENT_URI,values,selection,selectionArgs);
                break;
            }
            case REGISTRATION:{
                affected = basicUpdate(RegistrationEntry.TABLE_NAME, RegistrationEntry.CONTENT_URI, values, selection, selectionArgs);
                break;
            }
            case ASSIST:{
                affected = basicUpdate(RegistrationEntry.TABLE_NAME, RegistrationEntry.CONTENT_URI, values, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return affected;
    }

public static long addClub(Context mContext, String name, String color){
    ContentResolver mContentResolver = mContext.getContentResolver();

    ContentValues clubValues = Utility.createClubValues(name,color);

    return ContentUris.parseId(mContentResolver.insert(ClubsContract.ClubEntry.CONTENT_URI, clubValues));

}
    public static Club getClub(Context mContext,long clubId){
        ContentResolver mContentResolver = mContext.getContentResolver();
        Cursor data = mContentResolver.query(ClubsContract.ClubEntry.buildClubUri(clubId),null,null,null,null);
        Club result = new Club();
        result.id = clubId;
        if(data.moveToFirst()){


        result.name = data.getString(data.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_NAME));
        result.color = data.getString(data.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR));
        result.icon = Uri.parse(data.getString(data.getColumnIndex(ClubEntry.COLUMN_CLUB_ICON_URI)));
        result.atLeast = data.getInt(data.getColumnIndex(ClubEntry.COLUMN_CLUB_ATLEAST));
        result.terms = data.getInt(data.getColumnIndex(ClubEntry.COLUMN_CLUB_TERMS));
        }
        return result;
    }

    public static int getRegistration(Context mContext,long clubId, long userId){
        ContentResolver mContentResolver = mContext.getContentResolver();
        Uri userClubs = RegistrationEntry.builRegistrationUriWithUser(userId);
        Cursor cUserClubs = mContentResolver.query(userClubs,null,null,null,null);
        if(hasClub(cUserClubs,clubId)){
            int id = cUserClubs.getInt(cUserClubs.getColumnIndex(RegistrationEntry._ID));

            return id;
        }
        else {
            return RegistrationEntry.NO_REGISTRATION;
        }
    }
    private static Boolean hasClub (Cursor data, long clubId){
        if(data.moveToFirst()){
            long regClub;
            int columnId = data.getColumnIndex(RegistrationEntry.COLUMN_REGISTRATION_CLUB);
            do{
                regClub = data.getLong(columnId);
                if(regClub==clubId) return true;
            }
            while (data.moveToNext());
        }
        return false;

    }

    public static int updateClub(Context mContext, Club club){
        ContentValues updatedValues = Utility.createClubValues(club);
        int count = mContext.getContentResolver().update(
                ClubEntry.CONTENT_URI, updatedValues, ClubEntry._ID + "= ?",
                new String[]{Long.toString(club.id)});
        return count;
    }

}
