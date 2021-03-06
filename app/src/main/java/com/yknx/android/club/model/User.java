package com.yknx.android.club.model;

/**
 * Created by Yknx on 24/08/2014.
 */
public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    private String name;
    private String account;
    private String email;
    private String campus;
    private long id;

    public int getAttendances() {
        return attendances;
    }

    public void setAttendances(int attendances) {
        this.attendances = attendances;
    }

    private int attendances;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}
