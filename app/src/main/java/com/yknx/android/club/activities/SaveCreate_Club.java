package com.yknx.android.club.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.yknx.android.club.R;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.fragments.EditClubFragment;

public class SaveCreate_Club extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent args = getIntent();
        setContentView(R.layout.activity_save_create__club);
        if (savedInstanceState == null) {
            EditClubFragment frag = null;
            long clubId = args.getLongExtra(ClubsContract.ClubEntry._ID,-1);
            if(clubId==-1) this.finish();
            else {
                frag = EditClubFragment.getClubFragment(clubId);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_create__club, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
