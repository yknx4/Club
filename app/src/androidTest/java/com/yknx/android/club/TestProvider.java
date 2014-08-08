package com.yknx.android.club;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.yknx.android.club.data.ClubsContract.AssistEntry;
import com.yknx.android.club.data.ClubsContract.ClubEntry;
import com.yknx.android.club.data.ClubsContract.RegistrationEntry;
import com.yknx.android.club.data.ClubsContract.UserEntry;

/**
 * Created by Yknx on 07/08/2014.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    public final String TEST_CITY_NAME = "North Pole";
    public final String TEST_CITY_POSTALCODE = "28001";
    public final String TEST_CITY_COUNTRYCODE = "MX";
    public final String TEST_DATE = "20141205";

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                AssistEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                RegistrationEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                UserEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ClubEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                AssistEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                RegistrationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ClubEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

    }

    public void setUp() {
        deleteAllRecords();
    }

    public void testDeleteDb() throws Throwable {
        deleteAllRecords();

    }

    public void testGetType() {
        // content://com.example.android.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(ClubEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(ClubEntry.CONTENT_TYPE, type);


        String account = "20094894";
        // content://com.example.android.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                UserEntry.builUserUriWithAccount(account));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
        assertEquals(UserEntry.CONTENT_ITEM_TYPE, type);


    }

    public void testUpdateLocation() {
// Create a new map of clubValues, where column names are the keys
        ContentValues clubValues = TestDb.createFakeClubValues();

        Uri locationUri = mContext.getContentResolver().
                insert(ClubEntry.CONTENT_URI, clubValues);
        long locationRowId = ContentUris.parseId(locationUri);

// Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(clubValues);
        updatedValues.put(ClubEntry._ID, locationRowId);
        updatedValues.put(ClubEntry.COLUMN_CLUB_NAME, "Danzeu");

        int count = mContext.getContentResolver().update(
                ClubEntry.CONTENT_URI, updatedValues, ClubEntry._ID + "= ?",
                new String[]{Long.toString(locationRowId)});

        assertEquals(count, 1);

// A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ClubEntry.buildClubUri(locationRowId),
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(cursor, updatedValues);
    }

    // Make sure we can still delete after adding/updating stuff
    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }

    public void testInsertReadProvider() {
        // Test com.yknx.sunshineapp.data we're going to insert into the DB to see if it works.

        ContentResolver mContentResolver = getContext().getContentResolver();

        ContentValues clubValues = TestDb.createFakeClubValues();
        long clubRowId;
        clubRowId = ContentUris.parseId(mContentResolver.insert(ClubEntry.CONTENT_URI,clubValues));

        assertTrue(clubRowId != -1);
        Log.d(LOG_TAG, "New club id: " + clubRowId);


// A cursor is your primary interface to the query results.
        Cursor cursor = mContentResolver.query(
                ClubEntry.CONTENT_URI, // Table to Query
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // clubColumns to group by
        );

        TestDb.validateCursorAgainstContentValues(cursor, clubValues);

        ContentValues firstUserValues = TestDb.createFakeUserValues1();
        long firstUserRowId;
        firstUserRowId = ContentUris.parseId(mContentResolver.insert(UserEntry.CONTENT_URI, firstUserValues));

        assertTrue(firstUserRowId != -1);
        Log.d(LOG_TAG, "New User Row id: " + firstUserRowId);


// A cursor is your primary interface to the query results.
        Cursor userCursor = mContentResolver.query(
                UserEntry.CONTENT_URI, // Table to Query
                null,
                UserEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{firstUserRowId + ""}, // Values for the "where" clause
                null // clubColumns to group by

        );

        TestDb.validateCursorAgainstContentValues(userCursor, firstUserValues);

        ContentValues secondUserValues = TestDb.createFakeUserValues2();
        long secondUserRowId;
        secondUserRowId = ContentUris.parseId(mContentResolver.insert(UserEntry.CONTENT_URI, secondUserValues));

        assertTrue(secondUserRowId != -1);
        Log.d(LOG_TAG, "New User Row id: " + secondUserRowId);


// A cursor is your primary interface to the query results.
        Cursor userCursor2 = mContentResolver.query(
                UserEntry.CONTENT_URI, // Table to Query
                null,
                UserEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{secondUserRowId + ""}, // Values for the "where" clause
                null
        );

        TestDb.validateCursorAgainstContentValues(userCursor2, secondUserValues);

        ContentValues registrationValues = TestDb.createRegistration(clubRowId, firstUserRowId);
        long registrationRowId1;
        registrationRowId1 = ContentUris.parseId(mContentResolver.insert(RegistrationEntry.CONTENT_URI, registrationValues));

        assertTrue(registrationRowId1 != -1);
        Log.d(LOG_TAG, "New registration id: " + registrationRowId1);

        registrationValues = TestDb.createRegistration(clubRowId, secondUserRowId);

        long registrationRowId2;
        registrationRowId2 = ContentUris.parseId(mContentResolver.insert(RegistrationEntry.CONTENT_URI, registrationValues));

        assertTrue(registrationRowId2 != -1);
        Log.d(LOG_TAG, "New registration id: " + registrationRowId2);


// A cursor is your primary interface to the query results.
        Cursor registrationCursor = mContentResolver.query(
                RegistrationEntry.CONTENT_URI, // Table to Query
                null,
                RegistrationEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{registrationRowId1 + ""}, // Values for the "where" clause
                null // clubColumns to group by

        );

        TestDb.validateCursorAgainstContentValues(registrationCursor, TestDb.createRegistration(clubRowId, firstUserRowId));

        Cursor registrationCursor2 = mContentResolver.query(
                RegistrationEntry.CONTENT_URI, // Table to Query
                null,
                RegistrationEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{registrationRowId2 + ""}, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(registrationCursor2, TestDb.createRegistration(clubRowId, secondUserRowId));

        ContentValues assistValues = TestDb.createFakeAssist(registrationRowId1, 1);
        long assistRowId1;
        assistRowId1 = ContentUris.parseId(mContentResolver.insert(AssistEntry.CONTENT_URI, assistValues));

        assertTrue(assistRowId1 != -1);
        Log.d(LOG_TAG, "New assist id: " + assistRowId1);

        assistValues = TestDb.createFakeAssist(registrationRowId2, 2);

        long assistRowId2;
        assistRowId2 = ContentUris.parseId(mContentResolver.insert(AssistEntry.CONTENT_URI, assistValues));

        assertTrue(assistRowId2 != -1);
        Log.d(LOG_TAG, "New assist id: " + assistRowId2);


// A cursor is your primary interface to the query results.
        Cursor assistCursor = mContentResolver.query(
                AssistEntry.CONTENT_URI, // Table to Query
                null,
                AssistEntry.TABLE_NAME+"."+AssistEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{assistRowId1 + ""}, // Values for the "where" clause
                null
        );

        TestDb.validateCursorAgainstContentValues(assistCursor, TestDb.createFakeAssist(registrationRowId1, 1));

        Cursor assistCursor2 = mContentResolver.query(
                AssistEntry.CONTENT_URI, // Table to Query
                null,
                AssistEntry.TABLE_NAME+"."+AssistEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{assistRowId2 + ""}, // Values for the "where" clause
                null
        );

        TestDb.validateCursorAgainstContentValues(assistCursor2, TestDb.createFakeAssist(registrationRowId2, 2));

    }


}
