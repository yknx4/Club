package com.yknx.android.club.controller;

import android.database.Cursor;

import com.yknx.android.club.data.ClubsContract;
import com.yknx.android.club.model.User;

/**
 * Created by Yknx on 03/01/2015.
 */
public class UserController {

    public static User getUserFromCursor(Cursor c){
        User result = new User();
        String name = c.getString(c.getColumnIndex(ClubsContract.UserEntry.COLUMN_USER_NAME));
        String account = c.getString(c.getColumnIndex(ClubsContract.UserEntry.COLUMN_USER_ACCOUNT));
        Long id = c.getLong(c.getColumnIndex(ClubsContract.UserEntry._ID));
        result.setName(name);
        result.setAccount(account);
        result.setId(id);
        return result;
    }
}
