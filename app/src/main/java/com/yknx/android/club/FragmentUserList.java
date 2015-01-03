package com.yknx.android.club;


import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.yknx.android.club.util.DividerItemDecoration;

public class FragmentUserList extends Fragment {

private static final String LOG_TAG = FragmentUserList.class.getSimpleName();

    private RecyclerView userRecyclerview;
    private UserRowAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View finalView = inflater.inflate(R.layout.fragment_user_list, null);

        return finalView;
    }


    public void filter(CharSequence cs){
        Log.d(LOG_TAG,"Filtering with: "+cs);
        mAdapter.getFilter().filter(cs);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userRecyclerview = (RecyclerView) view.findViewById(R.id.user_recyclerview);
        userRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        userRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        userRecyclerview.setHasFixedSize(true);

        userRecyclerview.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new UserRowAdapter(getActivity());
        userRecyclerview.setAdapter(mAdapter);

    }

}
