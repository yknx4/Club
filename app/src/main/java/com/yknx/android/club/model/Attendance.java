package com.yknx.android.club.model;

import java.util.Date;

/**
 * Created by Yknx on 05/01/2015.
 */
public class Attendance {
    private long mRegistrationId;
    private int mTerm;
    private Date mDate;
    private long id;

    public long getRegistrationId() {
        return mRegistrationId;
    }

    public void setRegistrationId(long registrationId) {
        this.mRegistrationId = registrationId;
    }

    public int getTerm() {
        return mTerm;
    }

    public void setTerm(int term) {
        this.mTerm = term;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
