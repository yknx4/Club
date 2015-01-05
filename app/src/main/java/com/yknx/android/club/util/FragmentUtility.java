package com.yknx.android.club.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;

import com.yknx.android.club.Utility;

/**
 * Created by Yknx on 04/01/2015.
 */
public class FragmentUtility {

private static final String LOG_TAG = FragmentUtility.class.getSimpleName();
    public static void replaceFragment(int id, Fragment fragment, Context source) {
        FragmentManager fm = ((Activity) source).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.commit();

    }

    public static void deleteFragment(Fragment fragment, Context source) {
        FragmentManager fm = ((Activity) source).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
        Log.d(LOG_TAG, "Fragment destroyed.");
    }
}
