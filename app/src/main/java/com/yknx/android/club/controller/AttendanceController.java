package com.yknx.android.club.controller;

import android.database.Cursor;

import com.yknx.android.club.model.Attendance;
import com.yknx.android.club.model.ClubsContract;
import com.yknx.android.club.model.User;

import java.util.Date;

/**
 * Created by Yknx on 05/01/2015.
 */
public class AttendanceController {
    public static Attendance getFromCursor(Cursor c){
        Attendance result = new Attendance();
        Long id = c.getLong(c.getColumnIndex(ClubsContract.AttendanceEntry._ID));
        Long registration = c.getLong(c.getColumnIndex(ClubsContract.AttendanceEntry.COLUMN_ASSIST_REGISTRATION));
        int term =c.getInt(c.getColumnIndex(ClubsContract.AttendanceEntry.COLUMN_ASSIST_TERM));
        Date date = ClubsContract.getDateFromDb(c.getString(c.getColumnIndex(ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE)));
        result.setDate(date);
        result.setTerm(term);
        result.setRegistrationId(registration);
        result.setId(id);
        return result;
    }
}
