package com.yknx.android.club.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.data.ClubsProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailAssistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailAssistFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DetailAssistFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ACCOUNT = "account";
    public static final String ARG_CLUB = "club";
    private static final int LOADER_ID = 7;
    private static final String LOG_TAG = DetailAssistFragment.class.getSimpleName();
    private long mClub;
    private int mTerm=1;
    private DetailAssistFragment self;
    // TODO: Rename and change types of parameters
    private String mAccount = "";
    private String getAccount(){
        if(mAccount.isEmpty()) return "0";
        else  return mAccount;
    }

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param account Parameter 1.
     * @return A new instance of fragment DetailAssistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailAssistFragment newInstance(String account, long club) {
        DetailAssistFragment fragment = new DetailAssistFragment();
        if (account.isEmpty()) account="0";
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT, account);
        args.putLong(ARG_CLUB,club);
        fragment.setArguments(args);
        return fragment;
    }
    public DetailAssistFragment() {
        // Required empty public constructor
        self = this;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
        mLoaderManager = getActivity().getSupportLoaderManager();
        if (getArguments() != null) {
            mAccount = getArguments().getString(ARG_ACCOUNT);
            mClub = getArguments().getLong(ARG_CLUB);

            mLoaderManager.initLoader(LOADER_ID, null, this);
        }
        if(savedInstanceState!=null) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            fm.beginTransaction();
            Fragment fragOne = new UtilityUserAssistsFragment();
            Bundle arguments = new Bundle();
            arguments.putBoolean("shouldYouCreateAChildFragment", true);
            fragOne.setArguments(arguments);
            ft.add(R.id.fragment_detail_usercontainer, fragOne);
            ft.commit();
        }
        mContentResolver = getActivity().getContentResolver();



    }
    View currentView;
    EditText account;
    ImageButton addAssistButton;
    long userId = -1;

    LoaderManager mLoaderManager= null;
    ContentResolver mContentResolver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mView =inflater.inflate(R.layout.fragment_detail_assist, container, false);
        currentView = mView;
        account =(EditText) currentView.findViewById(R.id.fragment_details_assist_account);
        addAssistButton = (ImageButton) currentView.findViewById(R.id.fragment_detail_assist_addassist);

        addAssistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId!=-1){
                    ContentValues assist = Utility.createAssist(mRegistration,mTerm);
                    long assistRowId = ContentUris.parseId(mContentResolver.insert(ClubsContract.AssistEntry.CONTENT_URI, assist));
                    if(assistRowId!=-1){
                        if(currentFragment!=null)fm.beginTransaction().remove(currentFragment).commit();
                        Toast.makeText(getActivity(),"Assist added to "+mAccount+".",Toast.LENGTH_SHORT).show();
                        account.setText("");
                    }
                };
            }
        });

        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                /*Bundle args = new Bundle();
                args.putString(ClubsContract.UserEntry.COLUMN_USER_ACCOUNT,charSequence.toString());*/
                mAccount = charSequence.toString();
                if(mLoaderManager.getLoader(LOADER_ID)!=null)
                    mLoaderManager.restartLoader(LOADER_ID,null,self);
                else
                    mLoaderManager.initLoader(LOADER_ID,null,self);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return mView;
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
    FragmentManager fm ;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String tAccount = getAccount();
        Uri query = ClubsContract.UserEntry.builUserUriWithAccount(tAccount);
        userId=-1;

        if(currentFragment!=null)fm.beginTransaction().remove(currentFragment).commit();
       // Log.d(LOG_TAG,"Query for user "+tAccount);
        //Toast.makeText(getActivity(),"Query for user "+tAccount,Toast.LENGTH_SHORT).show();
        return new CursorLoader(getActivity(), query, null, null, null, null);
    }
    private Fragment currentFragment;
private int mRegistration;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() < 1 ) {
            //Log.d(LOG_TAG,"No account found");
            return;
        }
        if(!data.moveToFirst()) return;
        userId = data.getLong(data.getColumnIndex(ClubsContract.UserEntry._ID));
        mRegistration = ClubsProvider.getRegistration(getActivity(),mClub,userId);

        if(mRegistration!= ClubsContract.RegistrationEntry.NO_REGISTRATION){
            Toast.makeText(getActivity(),userId+" is in club "+mClub,Toast.LENGTH_SHORT).show();

            UtilityUserAssistsFragment fragToLoad = UtilityUserAssistsFragment.newInstance(mRegistration,userId,mClub);
            currentFragment = fragToLoad;


            FragmentTransaction ft = fm.beginTransaction();
            fm.beginTransaction();
            ft.replace(R.id.fragment_detail_usercontainer, fragToLoad);

            ft.commit();


        }else {
            //TODO: Something if user exists but isn't registered
            Toast.makeText(getActivity(),userId+" is not in club "+mClub,Toast.LENGTH_SHORT).show();

        }


        /*UtilityUserViewFragment fragToLoad = UtilityUserViewFragment.newInstance(),mClub);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fm.beginTransaction();
        ft.replace(R.id.fragment_detail_usercontainer, fragToLoad);

        ft.commit();
*/
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


}
