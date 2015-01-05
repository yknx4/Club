package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;
import com.yknx.android.club.adapters.UserRowAdapter;
import com.yknx.android.club.callbacks.UserRowAdapterCallbacks;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.DividerItemDecoration;

public class FragmentUserList extends Fragment {

private static final String LOG_TAG = FragmentUserList.class.getSimpleName();

    private RecyclerView userRecyclerview;
    private UserRowAdapter mAdapter;
    private Club mClub;
    private UserRowAdapterCallbacks adapterCallback;
    String lastFilter="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View finalView = inflater.inflate(R.layout.fragment_user_list, null);

        return finalView;
    }


    public void filter(CharSequence cs){
        Log.d(LOG_TAG,"Filtering with: "+cs);
        if(mAdapter!=null)mAdapter.getFilter().filter(cs);
        lastFilter=cs.toString();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(LOG_TAG, "onViewCreated");

        userRecyclerview = (RecyclerView) view.findViewById(R.id.user_recyclerview);
        userRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        userRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        userRecyclerview.setHasFixedSize(true);

        userRecyclerview.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new UserRowAdapter(getActivity(),mClub);
        userRecyclerview.setAdapter(mAdapter);
        if(adapterCallback!=null) mAdapter.setUserRowAdapterCallbacks(adapterCallback);




    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if(mAdapter!=null)mAdapter.getFilter().filter(lastFilter);
        if(adapterCallback!=null) mAdapter.setUserRowAdapterCallbacks(adapterCallback);

    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.setUserRowAdapterCallbacks(null);
        userRecyclerview.swapAdapter(null,true);
    }

    public void setClub(Club club) {
        Log.d(LOG_TAG,"Club set: "+club.name);
        this.mClub = club;
    }


    public void setAdapterCallback(UserRowAdapterCallbacks adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    public UserRowAdapterCallbacks getAdapterCallback() {
        return adapterCallback;
    }
}
