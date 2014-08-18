package com.yknx.android.club.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yknx.android.club.data.ClubsContract.AssistEntry;
import com.yknx.android.club.data.ClubsContract.ClubEntry;
import com.yknx.android.club.data.ClubsContract.RegistrationEntry;
import com.yknx.android.club.data.ClubsContract.UserEntry;

/**
 * Created by Yknx on 07/08/2014.
 */
public class ClubsDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "clubs.db";

    public ClubsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude

        // TBD

        final String SQL_CREATE_CLUBS_TABLE = "CREATE TABLE " + ClubEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast com.yknx.sunshineapp.data
                // should be sorted accordingly.
                ClubEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather com.yknx.sunshineapp.data
                ClubEntry.COLUMN_CLUB_NAME + " TEXT NOT NULL, " +
                ClubEntry.COLUMN_CLUB_ICON_URI + " TEXT, " +
                ClubEntry.COLUMN_CLUB_COLOR + " TEXT NOT NULL, " +
                ClubEntry.COLUMN_CLUB_TERMS + " INTEGER NOT NULL, " +
                ClubEntry.COLUMN_CLUB_ATLEAST + " INTEGER NOT NULL," +

                // To assure the application have just one club entry per name

                " UNIQUE (" + ClubEntry.COLUMN_CLUB_NAME  + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast com.yknx.sunshineapp.data
                // should be sorted accordingly.
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather com.yknx.sunshineapp.data
                UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_USER_ACCOUNT + " TEXT NOT NULL, " +
                UserEntry.COLUMN_USER_CAMPUS + " TEXT, " +
                UserEntry.COLUMN_USER_EMAIL + " TEXT, " +
                UserEntry.COLUMN_USER_PICTURE + " TEXT, " +
                UserEntry.COLUMN_USER_NOTE+ " TEXT, " +

                // To assure the application have just one club entry per name

                " UNIQUE (" + UserEntry.COLUMN_USER_ACCOUNT  + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_REGISTRATIONS_TABLE = "CREATE TABLE " + RegistrationEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast com.yknx.sunshineapp.data
                // should be sorted accordingly.
                RegistrationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather com.yknx.sunshineapp.data

                RegistrationEntry.COLUMN_REGISTRATION_CLUB + " INTEGER NOT NULL, " +
                RegistrationEntry.COLUMN_REGISTRATION_USER + " INTEGER NOT NULL, " +


                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + RegistrationEntry.COLUMN_REGISTRATION_CLUB + ") REFERENCES " +
                ClubEntry.TABLE_NAME + " (" + ClubEntry._ID + "), " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + RegistrationEntry.COLUMN_REGISTRATION_USER + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + RegistrationEntry.COLUMN_REGISTRATION_CLUB + ", " +
                RegistrationEntry.COLUMN_REGISTRATION_USER + ") ON CONFLICT IGNORE);";

        final String SQL_CREATE_ASSISTS_TABLE = "CREATE TABLE " + AssistEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast com.yknx.sunshineapp.data
                // should be sorted accordingly.
                AssistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather com.yknx.sunshineapp.data

                AssistEntry.COLUMN_ASSIST_DATE + " TEXT NOT NULL, " +
                AssistEntry.COLUMN_ASSIST_REGISTRATION + " INTEGER NOT NULL, " +
                AssistEntry.COLUMN_ASSIST_TERM + " INTEGER NOT NULL, " +


                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + AssistEntry.COLUMN_ASSIST_REGISTRATION + ") REFERENCES " +
                RegistrationEntry.TABLE_NAME + " (" + RegistrationEntry._ID + ") " +



                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " );";
       // Log.d(ClubsDbHelper.class.getSimpleName(),SQL_CREATE_REGISTRATIONS_TABLE);
        //Log.d(ClubsDbHelper.class.getSimpleName(),SQL_CREATE_ASSISTS_TABLE);




        sqLiteDatabase.execSQL(SQL_CREATE_CLUBS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REGISTRATIONS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ASSISTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /*sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+WeatherEntry.TABLE_NAME);*/
        onCreate(sqLiteDatabase);

    }
}
