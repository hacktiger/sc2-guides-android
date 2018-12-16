package com.sc2guide.sc2_guides_android.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {
    private String name;
    private String email;

    // Default cons for calls to
    // DataSnapshot.getValue(User.class)
    public UserModel () {

    }

    public UserModel (String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
