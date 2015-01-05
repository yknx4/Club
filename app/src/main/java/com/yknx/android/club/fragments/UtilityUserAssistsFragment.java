package com.yknx.android.club.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.yknx.android.club.R;
import com.yknx.android.club.Tasks.GetAttendancesTask;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UtilityUserAssistsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UtilityUserAssistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UtilityUserAssistsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_USERID = "userid";
    public static final String ARG_CLUBID = "clubid";
    public static final String ARG_REGID = "regid";
    private static final int ULOADER_ID = 1;
    private static final String LOG_TAG = UtilityUserAssistsFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private long mUserId;
    private User mResult;
    private long mClub;
    private long mRegId;
    private View mCurrentView;

    private SimpleCursorAdapter assistDataAdapter = null;
    private ListView assistListView = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId Parameter 1.
     * @return A new instance of fragment UtilityUserViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UtilityUserAssistsFragment newInstance(long regId,long userId, long clubId) {
        UtilityUserAssistsFragment fragment = new UtilityUserAssistsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USERID, userId);
        args.putLong(ARG_CLUBID,clubId);
        args.putLong(ARG_REGID, regId);
        fragment.setArguments(args);
        return fragment;
    }
    public UtilityUserAssistsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(ULOADER_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USERID);
            mClub = getArguments().getLong(ARG_CLUBID);
            mRegId = getArguments().getLong(ARG_REGID);
        }
        getLoaderManager().initLoader(ULOADER_ID,null,this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_utility_userview, container, false);
        mCurrentView = rootView;
        assistListView = (ListView) mCurrentView.findViewById(R.id.userview_assists_listview);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    Cursor mUserAssists;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri query = ClubsContract.UserEntry.buildUserUri(mUserId);
        return new CursorLoader(getActivity(), query, null, null, null, null);
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 1) {
            return;
        }
        if(!data.moveToFirst()) return;
        if(mCurrentView != null){
            TextView nameView = (TextView) mCurrentView.findViewById(R.id.userview_name_textview);
            String name = data.getString(data.getColumnIndex(ClubsContract.UserEntry.COLUMN_USER_NAME));
            nameView.setText(name);
            GetAssistsTask tsk = new GetAssistsTask(getActivity(),mRegId,0);
            tsk.execute();



        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        return;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class GetAssistsTask extends GetAttendancesTask{


        public GetAssistsTask(Context context, long mRegistrationId, int mTerm) {
            super(context, mRegistrationId, mTerm);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mUserAssists = cursor;
            TextView userAssistCount = (TextView) mCurrentView.findViewById(R.id.userview_assist_count_textview);
            final String[] columns = new String[]{
                    ClubsContract.AssistEntry.COLUMN_ASSIST_DATE
            };

            final int[] to = new int[]{
                    R.id.list_item_assistitem_date
            };
            assistDataAdapter = new SimpleCursorAdapter(
                    getActivity(),
                    R.layout.list_item_assistitem,
                    mUserAssists,
                    columns,
                    to,
                    0
                    );
            assistDataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int i) {

                    switch (view.getId()){
                        case R.id.list_item_assistitem_date:{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                            Date assDate = ClubsContract.getDateFromDb(cursor.getString(cursor.getColumnIndex(ClubsContract.AssistEntry.COLUMN_ASSIST_DATE)));
                            //g.d(LOG_TAG,cursor.getString(COLUMN_DATE));
                            String newDateString = new SimpleDateFormat("EEEE, d/MMMM/yyyy").format(assDate);

                            TextView tv = (TextView) view;
                            tv.setText(newDateString);
                            return true;
                        }
                        default:
                            return false;
                    }

                }
            });
            userAssistCount.setText(""+mUserAssists.getCount());
            assistListView.setAdapter(assistDataAdapter);
        }


    }



}
