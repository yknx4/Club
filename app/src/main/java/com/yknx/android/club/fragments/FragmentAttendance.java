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
import com.yknx.android.club.activities.ClubDataActivity;
import com.yknx.android.club.callbacks.FragmentUserDetailsCallbacks;
import com.yknx.android.club.callbacks.UserRowAdapterCallbacks;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.ClubsContract;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.FragmentUtility;
import com.yknx.android.club.util.Preferences;

public class FragmentAttendance extends Fragment implements UserRowAdapterCallbacks, FragmentUserDetailsCallbacks {

    private static final String LOG_TAG = FragmentAttendance.class.getSimpleName();
    private static final String CLUB_ID = "club_id";
    private static final String USER_LIST_LOADED = "user_list_loaded";
    private static final String EDIT_TEXT_CONTENT = "edit_text_content";
    private static final String USER_ID = "user_id";


    private FrameLayout mTopContainer;
    private CardView mCardContainer;
    private LinearLayout mDigitsContainer;
    private FrameLayout mBottomContainer;
    private EditText mDigitsEditText;
    private Club mClub;
    private boolean mIsSaved = false;

    private class OnBackPressedListener implements com.yknx.android.club.listeners.OnBackPressedListener {



        @Override
        public void doBack() {
            if (mLoadingUserData) {
                onCloseClick();

            } else {
                Log.d(LOG_TAG, "Standard fragment back.");
                ((ClubDataActivity)getActivity()).onBackPressed(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "Saving fragment state.");
        mIsSaved = true;
        outState.putLong(CLUB_ID, mClub.id);
        outState.putBoolean(USER_LIST_LOADED, mCurrentUser == null);
        outState.putString(EDIT_TEXT_CONTENT, mDigitsEditText.getText().toString());
        if (currentUserInfo != null) {
            outState.putLong(USER_ID, mCurrentUser.getId());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsSaved = savedInstanceState != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mClub == null) {
            Bundle args = getArguments();
            Log.d(LOG_TAG, "Club shouldn't be null");
            if (mIsSaved) {
                mClub = ClubsProvider.getClub(getActivity(), savedInstanceState.getLong(CLUB_ID));
            } else if (args != null) {
                mClub = ClubsProvider.getClub(getActivity(), args.getLong(CLUB_ID));
            } else {
                Log.d(LOG_TAG, "Something is realy wrong.!");
            }
        }

        ((ClubDataActivity) getActivity()).setOnBackPressedListener(new OnBackPressedListener());
        return inflater.inflate(R.layout.fragment_attendance, null);
    }

    Fragment currentUserList;
    Fragment currentUserInfo;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
        mDigitsContainer = (LinearLayout) view.findViewById(R.id.digits_container);
        mBottomContainer = (FrameLayout) view.findViewById(R.id.bottom_container);
        mDigitsEditText = (EditText) getView().findViewById(R.id.digits);
        mCardContainer = (CardView) getView().findViewById(R.id.card_container);
        customTextWatcher = new CustomTextWatcher();

        mDigitsEditText.addTextChangedListener(customTextWatcher);

        if (mIsSaved) {
            boolean userList = savedInstanceState.getBoolean(USER_LIST_LOADED);
            Log.d(LOG_TAG, "Restoring saved fragment with" + (userList ? " user list." : " user details."));
            if (userList) {
                deleteUserInfo();
                createUserList();
                mDigitsEditText.setText(savedInstanceState.getString(EDIT_TEXT_CONTENT));
            } else {
                mLoadingUserData = true;
                mCurrentUser = ClubsProvider.getUser(getActivity(), savedInstanceState.getLong(USER_ID));
                deleteUserList();
                createUserDetails(mCurrentUser);
            }

        } else {
            createUserList();
        }


    }


    private void setCardContainerHeight(int height) {
        ViewGroup.LayoutParams params = mCardContainer.getLayoutParams();
        params.height = height;
        mCardContainer.setLayoutParams(params);
    }

    private void createUserList() {
        Log.d(LOG_TAG, "Creating user list.");
        setCardContainerHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mTopContainer.setVisibility(View.VISIBLE);
        mBottomContainer.setVisibility(View.GONE);
        createUserListFragment();
        FragmentUtility.replaceFragment(R.id.top_container, currentUserList, getActivity());
        customTextWatcher.setParent((FragmentUserList) currentUserList);
        ((FragmentUserList) currentUserList).filter(mDigitsEditText.getText());
    }

    private void createUserListFragment() {
        FragmentUserList fragmentUserList = new FragmentUserList();
        Bundle args = Utility.putClub(mClub, null);
        fragmentUserList.setClub(mClub);
        fragmentUserList.setArguments(args);
        fragmentUserList.setAdapterCallback(this);
        currentUserList = fragmentUserList;
    }

    @Override
    public void onCloseClick() {
        Log.d(LOG_TAG, "Clicked close");
        mLoadingUserData = false;

        if (currentUserList == null) createUserList();
        if (currentUserInfo != null) deleteUserInfo();
        if (mDigitsEditText.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        mDigitsEditText.append("");
        mDigitsEditText.setSelection(mDigitsEditText.getText().length(), mDigitsEditText.getText().length());
        Utility.showKeyboard(getActivity());


    }


    User mCurrentUser = null;

    private void createUserDetails(User user) {
        Log.d(LOG_TAG, "Creating user info.");
        mCurrentUser = user;
        setCardContainerHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mTopContainer.setVisibility(View.GONE);
        mBottomContainer.setVisibility(View.VISIBLE);
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        userDetailsFragment.setFragmentUserDetailsCallbacks(this);
        Bundle args = Utility.putClub(mClub, null);
        args = Utility.putUser(user, args);
        userDetailsFragment.setArguments(args);
        userDetailsFragment.setClub(mClub);
        userDetailsFragment.setUser(user);
        currentUserInfo = userDetailsFragment;
        FragmentUtility.replaceFragment(R.id.bottom_container, currentUserInfo, getActivity());
        setDigitEditTextStatus(false);
        setDigitsEditTextContent(user.getAccount(), false);


    }

    private void setDigitEditTextStatus(boolean state) {
        mDigitsEditText.setFocusable(state);
        mDigitsEditText.setFocusableInTouchMode(state);
        mDigitsEditText.setLongClickable(state);
    }

    private void deleteUserInfo() {
        Log.d(LOG_TAG, "Deleting user info.");
        FragmentUtility.deleteFragment(currentUserInfo, getActivity());
        currentUserInfo = null;
        mCurrentUser = null;
        setDigitEditTextStatus(true);
    }

    private void deleteUserList() {
        mTopContainer.setVisibility(View.GONE);
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
        if (currentUserInfo == null) createUserDetails(user);
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
            mDigitsEditText.setText("");
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
                        Log.d(LOG_TAG, "createUserList() <- onTextChanged");
                        createUserList();

                    }
                    if (currentUserInfo != null) {
                        deleteUserInfo();
                    }
                }
            }
        }

        private boolean isValid() {
            return parent != null && mDigitsEditText.isFocusable();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    }


    private void setDigitsEditTextContent(String content, boolean triggerOnEdit) {
        if (!triggerOnEdit) {
            mDigitsEditText.removeTextChangedListener(customTextWatcher);
        }
        mDigitsEditText.setText(content);
        if (!triggerOnEdit) {
            mDigitsEditText.addTextChangedListener(customTextWatcher);
        }
    }
}
