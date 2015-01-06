package com.yknx.android.club.listeners;

import android.app.Activity;
import android.app.FragmentManager;


public class BaseBackPressedListener implements OnBackPressedListener {
    private final Activity activity;

    public BaseBackPressedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
        activity.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
}