package com.yknx.android.club;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.yknx.android.club.controller.AttendanceController;
import com.yknx.android.club.controller.UserController;
import com.yknx.android.club.model.Attendance;
import com.yknx.android.club.model.ClubsContract;
import com.yknx.android.club.model.User;

public class AttendanceRowAdapter extends RecyclerView.Adapter<AttendanceRowAdapter.ViewHolder> implements LoaderManager.LoaderCallbacks<Cursor> {

private static final String LOG_TAG = AttendanceRowAdapter.class.getSimpleName();


    private static final int LOADER_ATT = 1;
    private List<Attendance> objects = new ArrayList<>();

    private Context context;
    private long registrationId;
    private int term;
    private LayoutInflater layoutInflater;



    public AttendanceRowAdapter(Context context, long registrationId, int term ) {
        this.context = context;
        this.registrationId = registrationId;
        this.term = term;
        this.layoutInflater = LayoutInflater.from(context);
    }




    public Attendance getItem(int position) {
        return objects.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initializeViews(getItem(position),holder,position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }



    private void initializeViews(Attendance object, ViewHolder holder, int position) {
        Date dt = object.getDate();
        DateFormat date = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat time = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        holder.userRowAccountTextview.setText(time.format(dt));
        holder.attendanceDate.setText(date.format(dt));
        //TODO implement
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Loading data.");
        switch (id) {
            case LOADER_ATT:
                Uri finalUri = ClubsContract.AttendanceEntry.buildAssistUriWithRegistrationAndTerm(registrationId,term);
                return new CursorLoader(context, finalUri, null, null, null, ClubsContract.AttendanceEntry.COLUMN_ASSIST_DATE + " ASC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ATT: {
                Log.d(LOG_TAG, "Data loaded.");
                objects = listFromCursor(data);
                notifyDataSetChanged();
                break;
            }
        }


        if (!data.isClosed()) data.close();
    }

    private List<Attendance> listFromCursor(Cursor data) {
        List<Attendance> result = new ArrayList<>();

        while (data.moveToNext()) {
            result.add(AttendanceController.getFromCursor(data));
        }
        return result;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

    private TextView attendanceDate;
    private TextView userRowAccountTextview;
        ViewHolder(View v){
            super(v);
            attendanceDate = (TextView) v.findViewById(R.id.attendance_date);
            userRowAccountTextview = (TextView) v.findViewById(R.id.attendance_hour);
        }
    }
}
