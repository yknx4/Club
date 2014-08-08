package com.yknx.android.club;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yknx.android.club.data.ClubsContract;

import java.util.Random;

/**
 * Created by Yknx on 08/08/2014.
 */
public class ClubSelectFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = ClubSelectFragment.class.getSimpleName();
    ClubAdapter mClubAdapter;
    private int mListViewPosition = ListView.INVALID_POSITION;
    private ListView listView;
    private View mainView;

    private static final String LISTVIEW_POSITION = "lv_position";

    private static final int CLUB_LOADER = 0;

    public ClubSelectFragment() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String sortOrder = ClubsContract.ClubEntry.COLUMN_CLUB_NAME+" ASC ";
        return new CursorLoader(getActivity(), ClubsContract.ClubEntry.CONTENT_URI,null,null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()==0){
            //TODO: Add handler when Data = 0
        }
        mClubAdapter.swapCursor(data);

        if(mListViewPosition!= ListView.INVALID_POSITION){
            listView.setSelection(mListViewPosition);
            setHeader(mainView,mListViewPosition);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClubAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mListViewPosition!= ListView.INVALID_POSITION){
            outState.putInt(LISTVIEW_POSITION,mListViewPosition);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CLUB_LOADER,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
            getLoaderManager().restartLoader(CLUB_LOADER, null, this);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(LISTVIEW_POSITION))
            mListViewPosition = savedInstanceState.getInt(LISTVIEW_POSITION);
        mClubAdapter = new ClubAdapter(
                getActivity(),
                null,
                0
        );







        final View rootView = inflater.inflate(R.layout.fragment_club_select, container, false);


        final ImageButton phdSet = (ImageButton)rootView.findViewById(R.id.fragment_club_settings_button);
        phdSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFakeData();
            }
        });

        listView = (ListView) rootView.findViewById(R.id.listview_clubs);
        listView.setAdapter(mClubAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListViewPosition = position;
                setHeader(rootView,position);

            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(LISTVIEW_POSITION)) {
            mListViewPosition = savedInstanceState.getInt(LISTVIEW_POSITION);
        }
        mainView = rootView;
        return rootView;
    }

    private void setHeader(View rootView, int position) {

        Cursor cursor = mClubAdapter.getCursor();
        if(!cursor.moveToPosition(position)) return;
        String icon_uri = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI));
        String name = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_NAME));
        long clubId = cursor.getLong(cursor.getColumnIndex(ClubsContract.ClubEntry._ID));

        int termps = cursor.getInt(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_TERMS));

        LinearLayout header = (LinearLayout)rootView.findViewById(R.id.fragment_club_select_header);
        ImageView headerIcon = (ImageView)rootView.findViewById(R.id.fragment_club_select_club_icon);
        TextView nameView = (TextView)rootView.findViewById(R.id.fragment_club_select_title_textview);
        TextView usersView = (TextView)rootView.findViewById(R.id.fragment_club_select_users_textview);
        TextView termView = (TextView)rootView.findViewById(R.id.fragment_club_select_term_textview);

        Cursor clubs = getActivity().getContentResolver().query(ClubsContract.RegistrationEntry.builRegistrationUriWithClub(clubId), null, null, null, null);


        Random mRandom = new Random();

        String users = getActivity().getString(R.string.format_users, clubs.getCount());
        String term = getActivity().getString(R.string.format_terms, mRandom.nextInt(termps)+1);


        nameView.setText(name);
        usersView.setText(users);
        termView.setText(term);

        String color = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR));
        header.setBackgroundColor(Color.parseColor(color));

        if(icon_uri==null || icon_uri.equals("")) {
            //headerIcon.setImageResource(R.drawable.ic_action_about);
            headerIcon.setImageBitmap(null);
        }
        else headerIcon.setImageURI(Uri.parse(icon_uri));
    }

    public void deleteAllRecords() {
        Context mContext = getActivity();
        mContext.getContentResolver().delete(
                ClubsContract.AssistEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ClubsContract.RegistrationEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ClubsContract.UserEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ClubsContract.ClubEntry.CONTENT_URI,
                null,
                null
        );


    }

    public void addFakeData() {
        // Test com.yknx.sunshineapp.data we're going to insert into the DB to see if it works.
        deleteAllRecords();
        ContentResolver mContentResolver = getActivity().getContentResolver();

        ContentValues clubValues = Utility.createFakeClubValues();
        long clubRowId;
        clubRowId = ContentUris.parseId(mContentResolver.insert(ClubsContract.ClubEntry.CONTENT_URI, clubValues));


        Log.d(LOG_TAG, "New club id: " + clubRowId);

        clubValues = Utility.createFakeClubValues2();
        clubRowId = ContentUris.parseId(mContentResolver.insert(ClubsContract.ClubEntry.CONTENT_URI, clubValues));


        Log.d(LOG_TAG, "New club id: " + clubRowId);



        ContentValues firstUserValues = Utility.createFakeUserValues1();
        long firstUserRowId;
        firstUserRowId = ContentUris.parseId(mContentResolver.insert(ClubsContract.UserEntry.CONTENT_URI, firstUserValues));


        Log.d(LOG_TAG, "New User Row id: " + firstUserRowId);

        ContentValues secondUserValues = Utility.createFakeUserValues2();
        long secondUserRowId;
        secondUserRowId = ContentUris.parseId(mContentResolver.insert(ClubsContract.UserEntry.CONTENT_URI, secondUserValues));


        Log.d(LOG_TAG, "New User Row id: " + secondUserRowId);



        ContentValues registrationValues = Utility.createRegistration(clubRowId, firstUserRowId);
        long registrationRowId1;
        registrationRowId1 = ContentUris.parseId(mContentResolver.insert(ClubsContract.RegistrationEntry.CONTENT_URI, registrationValues));


        Log.d(LOG_TAG, "New registration id: " + registrationRowId1);

        registrationValues = Utility.createRegistration(clubRowId, secondUserRowId);

        long registrationRowId2;
        registrationRowId2 = ContentUris.parseId(mContentResolver.insert(ClubsContract.RegistrationEntry.CONTENT_URI, registrationValues));


        Log.d(LOG_TAG, "New registration id: " + registrationRowId2);








        ContentValues assistValues = Utility.createFakeAssist(registrationRowId1, 1);
        long assistRowId1;
        assistRowId1 = ContentUris.parseId(mContentResolver.insert(ClubsContract.AssistEntry.CONTENT_URI, assistValues));
        Log.d(LOG_TAG, "New assist id: " + assistRowId1);

        assistValues = Utility.createFakeAssist(registrationRowId2, 2);

        long assistRowId2;
        assistRowId2 = ContentUris.parseId(mContentResolver.insert(ClubsContract.AssistEntry.CONTENT_URI, assistValues));
        Log.d(LOG_TAG, "New assist id: " + assistRowId2);




    }

}