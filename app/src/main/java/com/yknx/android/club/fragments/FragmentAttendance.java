package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.EditText;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;

public class FragmentAttendance extends Fragment {

    private static final String LOG_TAG = FragmentAttendance.class.getSimpleName();

    private FrameLayout topContainer;
    private CardView cardContainer;
    private LinearLayout digitsContainer;
    private FrameLayout bottomContainer;
    private EditText digitsEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_attendance, null);
    }

    Fragment currentUserList;
    Fragment currentUserInfo;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topContainer = (FrameLayout) view.findViewById(R.id.top_container);
        digitsContainer = (LinearLayout) view.findViewById(R.id.digits_container);
        bottomContainer = (FrameLayout) view.findViewById(R.id.bottom_container);
        digitsEditText = (EditText) getView().findViewById(R.id.digits);
        cardContainer = (CardView) getView().findViewById(R.id.card_container);
        customTextWatcher = new CustomTextWatcher();
        digitsEditText.addTextChangedListener(customTextWatcher);
        createUserList();
    }


    private void setCardContainerHeight(int height){
        ViewGroup.LayoutParams params=cardContainer.getLayoutParams();
        params.height= height;
        cardContainer.setLayoutParams(params);
    }

    private void createUserList() {
        setCardContainerHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        topContainer.setVisibility(View.VISIBLE);
        currentUserList = new FragmentUserList();
        Utility.replaceFragment(R.id.top_container, currentUserList, getActivity());
        customTextWatcher.setParent((FragmentUserList) currentUserList);
    }

    private void createUserInfo(){
        bottomContainer.setVisibility(View.VISIBLE);
        currentUserInfo = new DummyFragment();
        Utility.replaceFragment(R.id.bottom_container,currentUserInfo,getActivity());
    }
    private void deleteUserInfo() {
        bottomContainer.setVisibility(View.GONE);
        Log.d(LOG_TAG, "Deleting user info.");
        Utility.deleteFragment(currentUserInfo,getActivity());
        currentUserInfo = null;

    }

    private void deleteUserList() {
        topContainer.setVisibility(View.GONE);
        setCardContainerHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d(LOG_TAG, "Deleting user list.");
        customTextWatcher.setParent(null);
        Utility.deleteFragment(currentUserList,getActivity());
        currentUserList = null;

    }


    CustomTextWatcher customTextWatcher;

    private EditText getDigits() {
        return (EditText) getView().findViewById(R.id.digits);
    }


    private class CustomTextWatcher implements TextWatcher {
        CustomTextWatcher() {
            super();

        }

        public FragmentUserList getParent() {
            return parent;
        }

        public void setParent(FragmentUserList parent) {
            this.parent = parent;
        }

        FragmentUserList parent;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isValid()) {

                Log.d(LOG_TAG, "Text: " + s);
                if (s.toString().equals("9999")) {
                    if (currentUserList != null) deleteUserList();
                    if(currentUserInfo==null) createUserInfo();
                } else {
                    parent.filter(s);
                }

            }else{
                if (currentUserList == null) {

                    createUserList();
                    Log.d(LOG_TAG, "Created");
                }
                if(currentUserInfo!=null){
                    deleteUserInfo();
                }
            }
        }

        private boolean isValid() {
            return parent != null;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
