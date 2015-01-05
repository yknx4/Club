package com.yknx.android.club.Tasks;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.yknx.android.club.R;
import com.yknx.android.club.data.ClubsContract;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        Uri query = ClubsContract.AssistEntry.buildAssistUriWithRegistrationAndTerm(mRegistrationId,mTerm);
        return context.getContentResolver().query(query,null,null,null,null);

    }
}