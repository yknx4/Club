package com.yknx.android.club.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
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
import android.widget.ListView;

import com.yknx.android.club.activities.ClubDataActivity;
import com.yknx.android.club.adapters.ClubAdapter;
import com.yknx.android.club.activities.ClubDetails;
import com.yknx.android.club.R;
import com.yknx.android.club.activities.SaveCreate_Club;
import com.yknx.android.club.Utility;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.util.Preferences;

/**
 * Created by Yknx on 08/08/2014.
 */
public class ClubSelectFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LISTVIEW_POSITION = "lv_position";
    private static final int CLUB_LOADER = 0;
    private final String LOG_TAG = ClubSelectFragment.class.getSimpleName();
    ClubAdapter mClubAdapter;
    private int mListViewPosition = ListView.INVALID_POSITION;
    private ListView listView;
    private View mainView;
    private Cursor mainData;
    ImageButton openClubButton;

    public ClubSelectFragment() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String sortOrder = ClubsContract.ClubEntry.COLUMN_CLUB_NAME + " ASC ";
        return new CursorLoader(getActivity(), ClubsContract.ClubEntry.CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mainData = data;
        mClubAdapter.swapCursor(data);
        if (data.getCount() == 0) {
            openClubButton.setVisibility(View.INVISIBLE);
            //TODO: Add handler when Data = 0
        } else {
            Long lastClub = Preferences.getLastSelectedClub(getActivity());
            int lastPosition = Preferences.getLastSelectedPosition(getActivity());

            Long currClub = mClubAdapter.getClubId(lastPosition);
            if (lastClub == currClub) {
                setClub(mainView,mListViewPosition);
            }else{
                mListViewPosition = ListView.INVALID_POSITION;
                Preferences.saveSharedSettingLong(getActivity(),Preferences.PREF_LAST_SELECTED_CLUB, ClubAdapter.INVALID_CLUB_ID);
                Preferences.saveSharedSettingInt(getActivity(),Preferences.PREF_LAST_SELECTED_POSITION,ListView.INVALID_POSITION);
                openClubButton.setVisibility(View.INVISIBLE);
            }
            if (lastPosition==ListView.INVALID_POSITION) openClubButton.setVisibility(View.INVISIBLE);
        }


    }

    private void setClub(View view, int clubPosition){
        listView.setSelection(clubPosition);
        setHeader(view, clubPosition);
        openClubButton.setEnabled(true);
        openClubButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClubAdapter.swapCursor(null);
        if(!mainData.isClosed())mainData.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mListViewPosition != ListView.INVALID_POSITION) {
            outState.putInt(LISTVIEW_POSITION, mListViewPosition);
        }
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!mainData.isClosed()) mainData.close();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CLUB_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mLocation != null && !mLocation.equals(Utility.getPreferredLocation(getActivity()))) {
        getLoaderManager().restartLoader(CLUB_LOADER, null, this);
        //}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClubAdapter = new ClubAdapter(
                getActivity(),
                null,
                0
        );
    }


    private long getCurrentClubID() {
        if (mClubAdapter == null) return ListView.INVALID_POSITION;
        return mClubAdapter.getClubId(mListViewPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (savedInstanceState != null && savedInstanceState.containsKey(LISTVIEW_POSITION))
//            mListViewPosition = savedInstanceState.getInt(LISTVIEW_POSITION);
        mListViewPosition = Preferences.getLastSelectedPosition(getActivity());

        final View rootView = inflater.inflate(R.layout.fragment_club_select, container, false);


        final ImageButton phdSet = (ImageButton) rootView.findViewById(R.id.fragment_club_settings_button);
        final ImageButton addClubButton = (ImageButton) rootView.findViewById(R.id.fragment_club_add_club_imagebutton);
        phdSet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addFakeData();
                return false;
            }
        });
        phdSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openClubSettings = new Intent(getActivity(), SaveCreate_Club.class);
                openClubSettings.putExtra(ClubsContract.ClubEntry._ID, getCurrentClubID());
                startActivity(openClubSettings);
            }
        });
        addClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment createClubDialog = new AddClubDialogFragment();
                createClubDialog.show(getFragmentManager(), "create_club");


            }
        });

        openClubButton = (ImageButton) rootView.findViewById(R.id.imagebutton_openclub);


        openClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity currentActivity = getActivity();


                Intent openClubDetails = new Intent(currentActivity, ClubDataActivity.class);
                openClubDetails.putExtra(DetailAssistFragment.ARG_CLUB, getCurrentClubID());
                //startActivity(openClubDetails);

//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        currentActivity, view, "test");

               //ActivityCompat.startActivity(currentActivity, openClubDetails,options.toBundle());
                ActivityCompat.startActivity(currentActivity,openClubDetails,null);

            }
        });

        if (mListViewPosition == ListView.INVALID_POSITION) {
            openClubButton.setEnabled(false);
        }

        listView = (ListView) rootView.findViewById(R.id.listview_clubs);
        listView.setAdapter(mClubAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListViewPosition = position;
                Preferences.saveSharedSettingInt(getActivity(),Preferences.PREF_LAST_SELECTED_POSITION,position);
                Preferences.saveSharedSettingLong(getActivity(),Preferences.PREF_LAST_SELECTED_CLUB,mClubAdapter.getClubId(position));
                setClub(rootView,position);
            }
        });


//        if (savedInstanceState != null && savedInstanceState.containsKey(LISTVIEW_POSITION)) {
//            mListViewPosition = savedInstanceState.getInt(LISTVIEW_POSITION);
//
//        } else {
//            mListViewPosition = 0;
//        }
        mainView = rootView;
        return rootView;
    }

    private void setHeader(View rootView, int position) {

        Cursor cursor = mClubAdapter.getCursor();
        Utility.setHeader(rootView, position, cursor, getActivity());

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