package com.yknx.android.club.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;
import com.yknx.android.club.adapters.ClubAdapter;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.fragments.ClubsNavigationDrawerFragment;
import com.yknx.android.club.fragments.DetailAssistFragment;
import com.yknx.android.club.fragments.FragmentAttendance;
import com.yknx.android.club.fragments.NavigationDrawerCallbacks;
import com.yknx.android.club.model.Club;

/**
 * Created by Yknx on 01/01/2015.
 */


/**
 * TODO: FIX first time title not loading
 */
public class ClubDataActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        //Get club data
        setClubFromIntent();
        mClub = ClubsProvider.getClub(this, mClubId);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {


        switch (position) {
            case 0: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, new FragmentAttendance());
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
        else
            super.onBackPressed();
    }
}
