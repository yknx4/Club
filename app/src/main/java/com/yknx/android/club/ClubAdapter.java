package com.yknx.android.club;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yknx.android.club.data.ClubsContract;

/**
 * Created by Yknx on 08/08/2014.
 */
public class ClubAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY =0;
    private final int VIEW_TYPE_DEFAULT =1;


    public ClubAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
        //return position==0?VIEW_TYPE_TODAY:VIEW_TYPE_DEFAULT;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType){
            case VIEW_TYPE_TODAY:
                layoutId=R.layout.club_list_item;
                break;
            case VIEW_TYPE_DEFAULT:
                layoutId=R.layout.club_list_item;
                break;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        // Read weather icon ID from cursor
        String icon_uri = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_ICON_URI));
        String name = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_NAME));
        String color = cursor.getString(cursor.getColumnIndex(ClubsContract.ClubEntry.COLUMN_CLUB_COLOR));
        long clubId = cursor.getLong(cursor.getColumnIndex(ClubsContract.ClubEntry._ID));

        Cursor clubs = mContext.getContentResolver().query(ClubsContract.RegistrationEntry.builRegistrationUriWithClub(clubId),null,null,null,null);


        String users = clubs.getCount()+"";


        if(icon_uri==null || icon_uri.equals("")) {
            viewHolder.iconView.setImageResource(R.drawable.default_icon);
            viewHolder.iconView.setColorFilter(Color.parseColor(color));
        }
        else viewHolder.iconView.setImageURI(Uri.parse(icon_uri));
        viewHolder.nameView.setText(name);
        viewHolder.usersView.setText(users);





    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView nameView;
        public final TextView usersView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.club_list_item_icon);
            nameView = (TextView) view.findViewById(R.id.club_list_item_name_textview);
            usersView = (TextView) view.findViewById(R.id.club_list_item_users_textview);
        }
    }

}