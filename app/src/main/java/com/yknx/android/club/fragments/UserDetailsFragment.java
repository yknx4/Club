package com.yknx.android.club.fragments;



import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.yknx.android.club.R;
import com.yknx.android.club.callbacks.FragmentUserDetailsCallbacks;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.DividerItemDecoration;

public class UserDetailsFragment extends Fragment {

private static final String LOG_TAG = UserDetailsFragment.class.getSimpleName();

    private TextView userDetailName;
    private TextView userDetailEmail;
    private TextView userDetailCampus;
    private RecyclerView userDetailRecyclerview;
    private ImageButton userDetailCloseButton;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDetailName = (TextView) view.findViewById(R.id.user_detail_name);
        userDetailEmail = (TextView) view.findViewById(R.id.user_detail_email);
        userDetailCampus = (TextView) view.findViewById(R.id.user_detail_campus);
        userDetailCloseButton = (ImageButton) view.findViewById(R.id.user_detail_close);
        userDetailRecyclerview = (RecyclerView) view.findViewById(R.id.user_detail_recyclerview);

        userDetailRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        userDetailRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        userDetailRecyclerview.setHasFixedSize(true);

        userDetailRecyclerview.setItemAnimator(new DefaultItemAnimator());


        userDetailCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentUserDetailsCallbacks!=null) fragmentUserDetailsCallbacks.onCloseClick();
            }
        });

        userDetailName.setText(user.getName());
        userDetailEmail.setText(user.getEmail());
        userDetailCampus.setText(user.getCampus());

        if(userDetailEmail.getText().equals("")) userDetailEmail.setVisibility(View.GONE);
        if(userDetailCampus.getText().equals("")) userDetailCampus.setVisibility(View.GONE);

        Log.d(LOG_TAG, "View Created");


    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}
