package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.yknx.android.club.R;
import com.yknx.android.club.adapters.UserRowAdapter;
import com.yknx.android.club.callbacks.UserRowAdapterCallbacks;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.DividerItemDecoration;

public class FragmentUserList extends Fragment implements UserRowAdapterCallbacks{

private static final String LOG_TAG = FragmentUserList.class.getSimpleName();

    private RecyclerView userRecyclerview;
    private UserRowAdapter mAdapter;
    private Club mClub;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View finalView = inflater.inflate(R.layout.fragment_user_list, null);

        return finalView;
    }


    public void filter(CharSequence cs){
        Log.d(LOG_TAG,"Filtering with: "+cs);
        if(mAdapter!=null)mAdapter.getFilter().filter(cs);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRecyclerview = (RecyclerView) view.findViewById(R.id.user_recyclerview);
        userRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        userRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        userRecyclerview.setHasFixedSize(true);

        userRecyclerview.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new UserRowAdapter(getActivity(),mClub);
        mAdapter.setUserRowAdapterCallbacks(this);
        userRecyclerview.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setUserRowAdapterCallbacks(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.setUserRowAdapterCallbacks(null);
    }

    public void setClub(Club club) {
        Log.d(LOG_TAG,"Club set: "+club.name);
        this.mClub = club;
    }



    @Override
    public void onUserClick(User user,int position) {
        Log.d(LOG_TAG,"Click on "+user.getName()+" on position "+position+".");
    }

    @Override
    public void onAttendanceAdd(Long userId) {
        Log.d(LOG_TAG,"Attendance added to "+userId+" on club "+mClub.name+".");
    }
}
