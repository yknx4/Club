package com.yknx.android.club.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.yknx.android.club.R;
import com.yknx.android.club.Utility;

/**
 * Created by Yknx on 10/08/2014.
 */
public class Club{
    public long id;
    public String name;
    public String color;
    public Uri icon;
    public int terms;
    public int atLeast;
    public Club(){}

    public Drawable getIcon(Context context){
        Drawable res = context.getDrawable(R.drawable.default_icon);
        res.setColorFilter(Color.parseColor(color), PorterDuff.Mode.ADD);
        if (icon != null && !icon.equals("")) {
            res = Utility.getIcon(id);
        }
        return res;
    }
}