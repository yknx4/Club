package com.yknx.android.club.callbacks;

import com.yknx.android.club.model.User;

/**
 * Created by Yknx on 04/01/2015.
 */
public interface UserRowAdapterCallbacks {
    void onUserClick(User user, int position);
    void onAttendanceAdd(Long userId);
}
