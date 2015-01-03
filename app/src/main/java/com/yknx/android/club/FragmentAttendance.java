package com.yknx.android.club;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class FragmentAttendance extends Fragment {

private static final String LOG_TAG = FragmentAttendance.class.getSimpleName();

    private FrameLayout topContainer;
    private LinearLayout digitsContainer;
    private FrameLayout bottomContainer;
    private EditText digitsEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_attendance, null);
    }
    Fragment currentUserList;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topContainer = (FrameLayout) view.findViewById(R.id.top_container);
        digitsContainer = (LinearLayout) view.findViewById(R.id.digits_container);
        bottomContainer = (FrameLayout) view.findViewById(R.id.bottom_container);
        digitsEditText = (EditText) getView().findViewById(R.id.digits);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        currentUserList = new FragmentUserList();
        ft.replace(R.id.top_container, currentUserList);
        Log.d(LOG_TAG, "View Created");
        ft.commit();
         customTextWatcher = new CustomTextWatcher();
        customTextWatcher.setParent(currentUserList);
        digitsEditText.addTextChangedListener(customTextWatcher);




    }
    CustomTextWatcher customTextWatcher;
    private EditText getDigits(){
        return (EditText) getView().findViewById(R.id.digits);
    }


    private class CustomTextWatcher implements TextWatcher{
        public Fragment getParent() {
            return parent;
        }

        public void setParent(Fragment parent) {
            this.parent = (FragmentUserList) parent;
        }

        FragmentUserList parent;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(isValid()) {
                parent.filter(s);
            }
        }

        private boolean isValid() {
            return parent!=null;
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
