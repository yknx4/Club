package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yknx.android.club.adapters.AttendanceRowAdapter;
import com.yknx.android.club.R;
import com.yknx.android.club.callbacks.FragmentUserDetailsCallbacks;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.DividerItemDecoration;
import com.yknx.android.club.util.Preferences;

public class UserDetailsFragment extends Fragment {

    private static final String LOG_TAG = UserDetailsFragment.class.getSimpleName();
    private static final String CLUB_ID = "club_id";
    private static final java.lang.String USER_ID = "user_id";


    //TODO: get registration and term!
    private TextView userDetailName;
    private TextView userDetailEmail;
    private TextView userDetailCampus;
    private RecyclerView userDetailRecyclerview;
    private ImageButton userDetailCloseButton;
    private boolean mIsSaved;

    public FragmentUserDetailsCallbacks getFragmentUserDetailsCallbacks() {
        return fragmentUserDetailsCallbacks;
    }

    public void setFragmentUserDetailsCallbacks(FragmentUserDetailsCallbacks fragmentUserDetailsCallbacks) {
        this.fragmentUserDetailsCallbacks = fragmentUserDetailsCallbacks;
    }

    private FragmentUserDetailsCallbacks fragmentUserDetailsCallbacks;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
    private Club club;
    private AttendanceRowAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if(club == null){
            Log.d(LOG_TAG,"Club shouldn't be null");
             if(args != null){
                club = ClubsProvider.getClub(getActivity(),args.getLong(CLUB_ID));
            }
            else {
                Log.d(LOG_TAG,"Something is realy wrong.!");
            }
        }

        if(user == null){
            Log.d(LOG_TAG,"User shouldn't be null");
            if(args != null){
                user = ClubsProvider.getUser(getActivity(), args.getLong(USER_ID));
            }
            else {
                Log.d(LOG_TAG,"Something is realy wrong.!");
            }
        }

        mAdapter = new AttendanceRowAdapter(getActivity(), ClubsProvider.getRegistration(getActivity(), club.id, user.getId()), Preferences.getSelectedTerm(getActivity(), club.id));

        View v = inflater.inflate(R.layout.fragment_user_details, null);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDetailName = (TextView) view.findViewById(R.id.user_detail_name);
        userDetailEmail = (TextView) view.findViewById(R.id.user_detail_email);
        userDetailCampus = (TextView) view.findViewById(R.id.user_detail_campus);
        userDetailCloseButton = (ImageButton) view.findViewById(R.id.user_detail_close);
        userDetailRecyclerview = (RecyclerView) view.findViewById(R.id.user_detail_recyclerview);


        userDetailRecyclerview.setHasFixedSize(true);


        userDetailRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        userDetailRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));


        userDetailRecyclerview.setItemAnimator(new DefaultItemAnimator());

        userDetailRecyclerview.setAdapter(mAdapter);
        userDetailCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentUserDetailsCallbacks != null)
                    fragmentUserDetailsCallbacks.onCloseClick();
            }
        });

        userDetailName.setText(user.getName());
        userDetailEmail.setText(user.getEmail());
        userDetailCampus.setText(user.getCampus());

        if (userDetailEmail.getText().equals("")) userDetailEmail.setVisibility(View.GONE);
        if (userDetailCampus.getText().equals("")) userDetailCampus.setVisibility(View.GONE);

        Log.d(LOG_TAG, "View Created");
        Log.d(LOG_TAG, "mAdapter info:\n" +
                "ID: " + mAdapter.toString() + "\n" +
                "Count: " + mAdapter.getItemCount() + "\n" +
                "");


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CLUB_ID,club.id);
        mIsSaved = true;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
