package com.yknx.android.club.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.yknx.android.club.data.ClubsContract;

/**
 * Created by Yknx on 04/01/2015.
 */
public class GetAttendancesTask  extends AsyncTask<Void,Integer,Cursor> {

    private Context context;
    private long mRegistrationId;
    private int mTerm;

    public GetAttendancesTask(Context context, long mRegistrationId, int mTerm) {
        this.context = context;
        this.mRegistrationId = mRegistrationId;
        this.mTerm = mTerm;
    }

    @Override
    protected Cursor doInBackground(Void... integers) {
        Uri query = ClubsContract.AttendanceEntry.buildAssistUriWithRegistrationAndTerm(mRegistrationId, mTerm);
        return context.getContentResolver().query(query,null,null,null,null);

    }
}