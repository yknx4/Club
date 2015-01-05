package com.yknx.android.club;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import com.yknx.android.club.model.ClubsContract;
import com.yknx.android.club.data.ClubsDbHelper;
import com.yknx.android.club.model.ClubsContract.ClubEntry;
import com.yknx.android.club.model.ClubsContract.RegistrationEntry;
import com.yknx.android.club.model.ClubsContract.UserEntry;

import java.util.Set;

/**
 * Created by Yknx on 07/08/2014.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

  public   static ContentValues createFakeClubValues() {
        String fakeName = "댄스";
        String fakeColor = "#007FFF";
        String fakeIcon = "";
        int terms = 3;
        int atLeast = 2;

// Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ClubEntry.COLUMN_CLUB_NAME, fakeName);
        testValues.put(ClubEntry.COLUMN_CLUB_COLOR, fakeColor);
        testValues.put(ClubEntry.COLUMN_CLUB_ICON_URI, fakeIcon);
        testValues.put(ClubEntry.COLUMN_CLUB_TERMS, terms);
        testValues.put(ClubEntry.COLUMN_CLUB_ATLEAST, atLeast);
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
        testValues.put(UserEntry.COLUMN_USER_NAME, fakeName);
        testValues.put(UserEntry.COLUMN_USER_ACCOUNT, fakeAccount);
        testValues.put(UserEntry.COLUMN_USER_PICTURE, fakePicture);
        testValues.put(UserEntry.COLUMN_USER_NOTE, fakeNote);

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
        testValues.put(UserEntry.COLUMN_USER_NAME, fakeName);
        testValues.put(UserEntry.COLUMN_USER_ACCOUNT, fakeAccount);
        testValues.put(UserEntry.COLUMN_USER_PICTURE, fakePicture);
        testValues.put(UserEntry.COLUMN_USER_NOTE, fakeNote);

        return testValues;
    }

    public  static ContentValues createRegistration(long club, long user) {
        ContentValues testValues = new ContentValues();
        testValues.put(RegistrationEntry.COLUMN_REGISTRATION_CLUB, club);
        testValues.put(RegistrationEntry.COLUMN_REGISTRATION_USER, user);
        return testValues;
    }

    public   static ContentValues createFakeAssist(long registrationId, int term) {
        ContentValues testValues = new ContentValues();

        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_REGISTRATION,registrationId);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_TERM, term);
        testValues.put(ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE, "20140910");
        return testValues;
    }

    public  static void validateCursorAgainstContentValues(
            Cursor valueCursor, ContentValues expectedValues) {

// If possible, move to the first row of the query results.
        assertTrue(valueCursor.moveToFirst());

// get the content values out of the cursor at the current position
        ContentValues resultValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(valueCursor, resultValues);

// make sure the values match the ones we put in
        validateContentValues(resultValues, expectedValues);
        valueCursor.close();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void validateContentValues(ContentValues output, ContentValues input) {
        Set<String> inputKeys = input.keySet();
        for (String key : inputKeys) {
            assertTrue(output.containsKey(key));
            Log.d(LOG_TAG, "Row " + key + ": " + input.getAsString(key) + " -> " + output.getAsString(key));
            assertTrue(output.getAsString(key).equals(input.getAsString(key)));
        }
    }

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(ClubsDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new ClubsDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        // Test com.yknx.sunshineapp.data we're going to insert into the DB to see if it works.


        ClubsDbHelper dbHelper = new ClubsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues clubValues = createFakeClubValues();
        long clubRowId;
        clubRowId = db.insert(ClubEntry.TABLE_NAME, null, clubValues);

        assertTrue(clubRowId != -1);
        Log.d(LOG_TAG, "New club id: " + clubRowId);

        // Data's inserted. IN THEORY. Now pull some out to stare at it and verify it made
        // the round trip.
        // Specify which clubColumns you want.
        String[] clubColumns = {
                ClubEntry._ID,
                ClubEntry.COLUMN_CLUB_NAME,
                ClubEntry.COLUMN_CLUB_ICON_URI,
                ClubEntry.COLUMN_CLUB_TERMS,
                ClubEntry.COLUMN_CLUB_ATLEAST,
                ClubEntry.COLUMN_CLUB_COLOR
        };
// A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                ClubEntry.TABLE_NAME, // Table to Query
                clubColumns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(cursor, clubValues);

        ContentValues firstUserValues = createFakeUserValues1();
        long firstUserRowId;
        firstUserRowId = db.insert(UserEntry.TABLE_NAME, null, firstUserValues);

        assertTrue(firstUserRowId != -1);
        Log.d(LOG_TAG, "New User Row id: " + firstUserRowId);

        // Data's inserted. IN THEORY. Now pull some out to stare at it and verify it made
// the round trip.
// Specify which clubColumns you want.
        String[] userColumns = {
                UserEntry._ID,
                UserEntry.COLUMN_USER_NAME,
                UserEntry.COLUMN_USER_ACCOUNT,
                UserEntry.COLUMN_USER_CAMPUS,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_PICTURE,
                UserEntry.COLUMN_USER_NOTE
        };
// A cursor is your primary interface to the query results.
        Cursor userCursor = db.query(
                UserEntry.TABLE_NAME, // Table to Query
                userColumns,
                UserEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{firstUserRowId + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(userCursor, firstUserValues);

        ContentValues secondUserValues = createFakeUserValues2();
        long secondUserRowId;
        secondUserRowId = db.insert(UserEntry.TABLE_NAME, null, secondUserValues);

        assertTrue(secondUserRowId != -1);
        Log.d(LOG_TAG, "New User Row id: " + secondUserRowId);


// A cursor is your primary interface to the query results.
        Cursor userCursor2 = db.query(
                UserEntry.TABLE_NAME, // Table to Query
                userColumns,
                UserEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{secondUserRowId + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(userCursor2, secondUserValues);

        ContentValues registrationValues = createRegistration(clubRowId,firstUserRowId);
        long registrationRowId1;
        registrationRowId1 = db.insert(RegistrationEntry.TABLE_NAME, null, registrationValues);

        assertTrue(registrationRowId1 != -1);
        Log.d(LOG_TAG, "New registration id: " + registrationRowId1);

        registrationValues = createRegistration(clubRowId,secondUserRowId);

        long registrationRowId2;
        registrationRowId2 = db.insert(RegistrationEntry.TABLE_NAME, null, registrationValues);

        assertTrue(registrationRowId2 != -1);
        Log.d(LOG_TAG, "New registration id: " + registrationRowId2);

        String[] registrationColumns = {
                RegistrationEntry._ID,
                RegistrationEntry.COLUMN_REGISTRATION_CLUB,
                RegistrationEntry.COLUMN_REGISTRATION_USER

        };
// A cursor is your primary interface to the query results.
        Cursor registrationCursor = db.query(
                RegistrationEntry.TABLE_NAME, // Table to Query
                registrationColumns,
                RegistrationEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{registrationRowId1 + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(registrationCursor, createRegistration(clubRowId,firstUserRowId));

        Cursor registrationCursor2 = db.query(
                RegistrationEntry.TABLE_NAME, // Table to Query
                registrationColumns,
                RegistrationEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{registrationRowId2 + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(registrationCursor2, createRegistration(clubRowId,secondUserRowId));

        ContentValues assistValues = createFakeAssist(registrationRowId1,1);
        long assistRowId1;
        assistRowId1 = db.insert(ClubsContract.AttendanceEntry.TABLE_NAME, null, assistValues);

        assertTrue(assistRowId1 != -1);
        Log.d(LOG_TAG, "New assist id: " + assistRowId1);

        assistValues = createFakeAssist(registrationRowId2,2);

        long assistRowId2;
        assistRowId2 = db.insert(ClubsContract.AttendanceEntry.TABLE_NAME, null, assistValues);

        assertTrue(assistRowId2 != -1);
        Log.d(LOG_TAG, "New assist id: " + assistRowId2);



        String[] assistColumns = {
                ClubsContract.AttendanceEntry._ID,

                ClubsContract.AttendanceEntry.COLUMN_ASSIST_REGISTRATION,
                ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE,
                ClubsContract.AttendanceEntry.COLUMN_ASSIST_TERM

        };
// A cursor is your primary interface to the query results.
        Cursor assistCursor = db.query(
                ClubsContract.AttendanceEntry.TABLE_NAME, // Table to Query
                assistColumns,
                ClubsContract.AttendanceEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{assistRowId1 + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(assistCursor, createFakeAssist(registrationRowId1,1));

        Cursor assistCursor2 = db.query(
                ClubsContract.AttendanceEntry.TABLE_NAME, // Table to Query
                assistColumns,
                ClubsContract.AttendanceEntry._ID + " = ? ", // Columns for the "where" clause
                new String[]{assistRowId2 + ""}, // Values for the "where" clause
                null, // clubColumns to group by
                null, // clubColumns to filter by row groups
                null // sort order
        );

        TestDb.validateCursorAgainstContentValues(assistCursor2, createFakeAssist(registrationRowId2,2));

        dbHelper.close();
    }
}