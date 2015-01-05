package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

import com.yknx.android.club.R;


public class DummyFragment extends Fragment {

    private boolean overrideLayout = false;

    public void setLayout(int layout) {

        overrideLayout=true;
        this.layout = layout;
    }

    int layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(overrideLayout) return inflater.inflate(layout, null);
        return inflater.inflate(R.layout.fragment_dummy, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
