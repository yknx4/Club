package com.yknx.android.club.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;
import com.yknx.android.club.adapters.ClubAdapter;
import com.yknx.android.club.callbacks.NavigationDrawerCallbacks;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.fragments.ClubsNavigationDrawerFragment;
import com.yknx.android.club.fragments.DetailAssistFragment;
import com.yknx.android.club.fragments.FragmentAttendance;
import com.yknx.android.club.listeners.OnBackPressedListener;
import com.yknx.android.club.model.Club;

/**
 * Created by Yknx on 01/01/2015.
 */


/**
 * TODO: FIX first time title not loading
 */
public class ClubDataActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private static final String LOG_TAG = ClubDataActivity.class.getSimpleName();

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    private boolean mIsSaved = false;
    private static final String CLUB_ID = "club_id";


    private Club mClub;
    private Toolbar mToolbar;
    private ClubsNavigationDrawerFragment mClubsNavigationDrawerFragment;
    private long mClubId;
    private boolean mActivityCreated = false;

    private void setClubFromIntent() {
        Intent opt = getIntent();
        if (opt != null) {
            mClubId = opt.getLongExtra(DetailAssistFragment.ARG_CLUB, ClubAdapter.INVALID_CLUB_ID);
        } else {
            this.finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mIsSaved = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        if (mIsSaved) {
            mClubId = savedInstanceState.getLong(CLUB_ID);
        } else
            setClubFromIntent();
        mClub = ClubsProvider.getClub(this, mClubId);
        setContentView(R.layout.activity_main_topdrawer);
        //Get club data
        mActivityCreated = true;

        //Setup Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setToolbarFromClub();
        setThemeFromClub();
        mClubsNavigationDrawerFragment = (ClubsNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
        mClubsNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar, mClubId);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsSaved = true;
        outState.putLong(CLUB_ID, mClubId);

    }

    private void setThemeFromClub() {
        mToolbar.setBackgroundColor(Color.parseColor(mClub.color));
        Utility.setActionBarColorFromClubColor(mClub, this);

    }

    private void setToolbarFromClub() {
        if (mActivityCreated) {
            mToolbar.setTitle(mClub.name);
//            mToolbar.setLogo(mClub.getIcon(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private boolean mFirstCallNavDraw = true;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d(LOG_TAG, "onNavigationDrawerItemSelected");
        if (mFirstCallNavDraw) {
            mFirstCallNavDraw = false;
            return;
        }
        switch (position) {
            case 0: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                FragmentAttendance fa = new FragmentAttendance();
                Bundle args = Utility.putClub(mClubId,null);
                fa.setArguments(args);
                fa.setClub(mClub);
                ft.replace(R.id.container, fa);
                ft.commit();
                setToolbarFromClub();
                break;
            }
        }
//        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mClubsNavigationDrawerFragment.isDrawerOpen())
            mClubsNavigationDrawerFragment.closeDrawer();
        else if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    public void onBackPressed(boolean b) {
        if(b){
            if (mClubsNavigationDrawerFragment.isDrawerOpen())
                mClubsNavigationDrawerFragment.closeDrawer();
            else
            super.onBackPressed();
        }
        else {
            onBackPressed();
        }
    }
}
