package com.example.pettern_ver01;

import java.util.Date;


public class User {
    String user_email;
    String user_name;
    Date sessionExpiryDate;

    public void setUsername(String user_email) {
        this.user_email = user_email;
    }

    public void setFullName(String user_name) {
        this.user_name = user_name;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
