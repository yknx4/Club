package com.yknx.android.club.fragments;


import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;
import com.yknx.android.club.callbacks.FragmentUserDetailsCallbacks;
import com.yknx.android.club.callbacks.UserRowAdapterCallbacks;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.FragmentUtility;
import com.yknx.android.club.util.Preferences;

public class FragmentAttendance extends Fragment implements UserRowAdapterCallbacks, FragmentUserDetailsCallbacks {

    private static final String LOG_TAG = FragmentAttendance.class.getSimpleName();

    private FrameLayout topContainer;
    private CardView cardContainer;
    private LinearLayout digitsContainer;
    private FrameLayout bottomContainer;
    private EditText digitsEditText;
    private Club mClub;


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


    private void setCardContainerHeight(int height) {
        ViewGroup.LayoutParams params = cardContainer.getLayoutParams();
        params.height = height;
        cardContainer.setLayoutParams(params);
    }

    private void createUserList() {
        Log.d(LOG_TAG,"Creating user list.");
        setCardContainerHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        topContainer.setVisibility(View.VISIBLE);
        FragmentUserList fragmentUserList = new FragmentUserList();
        fragmentUserList.setClub(mClub);
        fragmentUserList.setAdapterCallback(this);
        currentUserList = fragmentUserList;
        FragmentUtility.replaceFragment(R.id.top_container, currentUserList, getActivity());
        customTextWatcher.setParent((FragmentUserList) currentUserList);
        ((FragmentUserList) currentUserList).filter(digitsEditText.getText());
    }

    @Override
    public void onCloseClick() {
        Log.d(LOG_TAG,"Clicked close");
        mLoadingUserData = false;

        if(currentUserList==null)createUserList();
        if(currentUserInfo!=null)deleteUserInfo();
        if (digitsEditText.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        digitsEditText.append("");
        digitsEditText.setSelection(digitsEditText.getText().length(),digitsEditText.getText().length());
        Utility.showKeyboard(getActivity());


    }



    private void createUserInfo(User user) {
        Log.d(LOG_TAG, "Creating user info.");
        bottomContainer.setVisibility(View.VISIBLE);
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        userDetailsFragment.setFragmentUserDetailsCallbacks(this);
        userDetailsFragment.setClub(mClub);
        userDetailsFragment.setUser(user);
        currentUserInfo = userDetailsFragment;
        FragmentUtility.replaceFragment(R.id.bottom_container, currentUserInfo, getActivity());
        setDigitEditTextStatus(false);
        digitsEditText.setText(user.getAccount());


    }

    private void setDigitEditTextStatus(boolean state) {
       digitsEditText.setFocusable(state);
        digitsEditText.setFocusableInTouchMode(state);
        digitsEditText.setLongClickable(state);
    }

    private void deleteUserInfo() {
        bottomContainer.setVisibility(View.GONE);
        Log.d(LOG_TAG, "Deleting user info.");
        FragmentUtility.deleteFragment(currentUserInfo, getActivity());
        currentUserInfo = null;
        setDigitEditTextStatus(true);
    }

    private void deleteUserList() {
        topContainer.setVisibility(View.GONE);
        setCardContainerHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        Log.d(LOG_TAG, "Deleting user list.");
        customTextWatcher.setParent(null);
        FragmentUtility.deleteFragment(currentUserList, getActivity());
        currentUserList = null;

    }


    private boolean mLoadingUserData = false;

    @Override
    public void onUserClick(User user, int position) {
        Log.d(LOG_TAG, "Click on " + user.getName() + " on position " + position + ".");
        mLoadingUserData = true;
        Utility.hideKeyboard(getActivity());
        if (currentUserInfo == null) createUserInfo(user);
        if (currentUserList != null) deleteUserList();

    }

    @Override
    public void onAttendanceAdd(Long userId) {
        String attendanceMessage = "Attendance added to " + userId + " on club " + mClub.name + ".";
        Log.d(LOG_TAG, attendanceMessage);
        long registration = ClubsProvider.getRegistration(getActivity(), mClub.id, userId);
        if (registration == ClubsContract.RegistrationEntry.NO_REGISTRATION) {
            ContentValues registrationValues = Utility.createRegistration(mClub.id, userId);
            registration = ContentUris.parseId(getActivity().getContentResolver().insert(ClubsContract.RegistrationEntry.CONTENT_URI, registrationValues));
        }
        ContentValues assist = Utility.createAssist(registration, Preferences.getSelectedTerm(getActivity(), mClub.id));
        long assistRowId = ContentUris.parseId(getActivity().getContentResolver().insert(ClubsContract.AttendanceEntry.CONTENT_URI, assist));
        if (assistRowId != -1) {
            Toast.makeText(getActivity(), attendanceMessage, Toast.LENGTH_SHORT).show();
            digitsEditText.setText("");
        }
    }


    CustomTextWatcher customTextWatcher;

    private EditText getDigits() {
        return (EditText) getView().findViewById(R.id.digits);
    }

    public void setClub(Club club) {
        Log.d(LOG_TAG, "Club set: " + club.name);
        this.mClub = club;
    }

    public Club getClub() {
        return mClub;
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
                parent.filter(s);
            } else {
                if (!mLoadingUserData) {
                    if (currentUserList == null) {
                        createUserList();
                        Log.d(LOG_TAG, "Created");
                    }
                    if (currentUserInfo != null) {
                        deleteUserInfo();
                    }
                }
            }
        }

        private boolean isValid() {
            return parent != null && digitsEditText.isFocusable();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    }

}
