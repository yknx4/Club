package com.yknx.android.club.adapters;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yknx.android.club.R;
import com.yknx.android.club.Tasks.GetAttendancesTask;
import com.yknx.android.club.controller.UserController;
import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.data.ClubsProvider;
import com.yknx.android.club.model.Club;
import com.yknx.android.club.model.User;
import com.yknx.android.club.util.Preferences;

import java.util.ArrayList;
import java.util.List;

public class UserRowAdapter extends RecyclerView.Adapter<UserRowAdapter.ViewHolder> implements Filterable, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = UserRowAdapter.class.getSimpleName();
    private final int LOADER_USERS = 1;
    private List<User> objects = new ArrayList<>();


    private Context context;
    private LayoutInflater layoutInflater;
    private LoaderManager mLoaderManager;
    private Club mClub;


    public UserRowAdapter(Context context, Club club) {
        this.mClub = club;
        Log.d(LOG_TAG,"Club set: "+club.name);
        Log.d(LOG_TAG, "UserRowAdapter started.");

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        mLoaderManager = ((Activity) context).getLoaderManager();
        mLoaderManager.initLoader(LOADER_USERS, null, this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, null);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row, parent, false);
        ViewHolder res = new ViewHolder(v);
        return res;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initializeViews(objects.get(position), holder);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != objects ? objects.size() : 0);
    }


    private void initializeViews(User object, final ViewHolder holder) {
        holder.userRowAccountTextview.setText(object.getAccount());
        holder.userRowNameTextview.setText(object.getName());
        if (mClub == null) {
            holder.userRowAttendanceTextview.setVisibility(View.INVISIBLE);
        } else {
            int term = Preferences.getSelectedTerm(context, mClub.id);
            int registration = ClubsProvider.getRegistration(context, mClub.id, object.getId());
            if (registration == ClubsContract.RegistrationEntry.NO_REGISTRATION) {
//                holder.userRowAttendanceTextview.setText("0");
                holder.userRowAttendanceTextview.setVisibility(View.INVISIBLE);
            } else {
                GetAttendancesTask getAttendancesTask = new GetAttendancesTask(context, registration, term) {
                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        super.onPostExecute(cursor);
                        ViewHolder toSet = holder;
                        toSet.userRowAttendanceTextview.setText(String.valueOf(cursor.getCount()));
                    }
                };
                getAttendancesTask.execute();
            }
        }
        //TODO implement
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            Cursor filteredDataCursor = context.getContentResolver().query(
                    ClubsContract.UserEntry.CONTENT_URI, null,
                    ClubsContract.UserEntry.COLUMN_USER_ACCOUNT + " like ?",
                    new String[]{"%" + constraint + "%"},
                    ClubsContract.UserEntry.COLUMN_USER_NAME + " ASC");
            filterResults.values = userListFromCursor(filteredDataCursor);
            filterResults.count = filteredDataCursor.getCount();
            Log.v(LOG_TAG, " did filter. (" + filteredDataCursor.getCount() + ")");
            filteredDataCursor.close();
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            objects = (List<User>) results.values;
            notifyDataSetChanged();
            Log.v(LOG_TAG, "Published results.");
        }
    };

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Loading data.");
        switch (id) {
            case LOADER_USERS:
                return new CursorLoader(context, ClubsContract.UserEntry.CONTENT_URI, null, null, null, ClubsContract.UserEntry.COLUMN_USER_NAME + " ASC");
        }
        return null;
    }

    private List<User> userListFromCursor(Cursor c) {
        List<User> result = new ArrayList<>();

        while (c.moveToNext()) {
            result.add(UserController.getUserFromCursor(c));
        }
        return result;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "Data loaded.");
        switch (loader.getId()) {
            case LOADER_USERS: {
                objects = userListFromCursor(data);
                notifyDataSetChanged();
                break;
            }
        }


        if (!data.isClosed()) data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userRowIcon;
        private TextView userRowNameTextview;
        private TextView userRowAccountTextview;
        private TextView userRowAttendanceTextview;
        private ImageButton userRowAddButton;

        public ViewHolder(View v) {
            super(v);
            userRowIcon = (ImageView) v.findViewById(R.id.user_row_icon);
            userRowNameTextview = (TextView) v.findViewById(R.id.user_row_name_textview);
            userRowAccountTextview = (TextView) v.findViewById(R.id.user_row_account_textview);
            userRowAttendanceTextview = (TextView) v.findViewById(R.id.user_row_attendances_textview);
            userRowAddButton = (ImageButton) v.findViewById(R.id.user_row_add_button);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
