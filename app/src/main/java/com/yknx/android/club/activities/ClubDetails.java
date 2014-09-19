package com.yknx.android.club.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yknx.android.club.R;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.fragments.DetailAssistFragment;
import com.yknx.android.club.fragments.DetailRegisterFragment;
import com.yknx.android.club.fragments.NavigationDrawerFragment;
import com.yknx.android.club.fragments.UtilityUserAssistsFragment;
import com.yknx.android.club.model.Club;


public class ClubDetails extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DetailAssistFragment.OnFragmentInteractionListener, UtilityUserAssistsFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private long mClub = 0;
    private Club cClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        setClubFromIntent();
        cClub = ClubsProvider.getClub(this, mClub);
        setTitle(cClub.name);
        mNavigationDrawerFragment.setClub(mClub);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }
    private void setClubFromIntent(){
        Intent opt = getIntent();
        if(opt !=null){
            mClub = opt.getLongExtra(DetailAssistFragment.ARG_CLUB,0);
        }
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        setClubFromIntent();
        switch(position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DetailAssistFragment.newInstance("",mClub))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DetailRegisterFragment.newInstance("", ""))
                        .commit();
                break;

        }


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        if(cClub!=null){
            actionBar.setTitle(cClub.name);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(cClub.color)));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.club_details, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */


}
