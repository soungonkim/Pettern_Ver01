package com.example.pettern_ver01;

import java.util.Date;


public class User {
    String user_id;
    String username;
    String fullName;
    Date sessionExpiryDate;

   // public void setUser_Id(String user_id) {
 //       this.user_id = user_id;
  //  }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

   // public String getUser_Id() {
   //     return user_id;
   // }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}