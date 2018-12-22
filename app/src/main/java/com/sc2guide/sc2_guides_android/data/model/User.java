package com.sc2guide.sc2_guides_android.data.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {
    private String name;
    private String email;

    // Default cons for calls to
    // DataSnapshot.getValue(User.class)
    public User () {

    }

    public User (String name, String email){
        if(validateName(name) && validateEmail(email)) {
            this.name = name;
            this.email = email;
        } else {
            throw new IllegalArgumentException("Failed created User object");
        }
    }

    private boolean validateName(String name) {
        return (!name.isEmpty() || name.length() > 8);
    }

    private boolean validateEmail(String email) {
        return (!email.isEmpty());
    }

    private boolean repOk() {
        return validateEmail(email) && validateName(name);
    }

    public String toString() {
        return "< " + "Email: " + email + ", Name: " + name + " >";
    }

    public String getName() { return name; }

    public void setName(String name) {
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Set name is not validated");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (validateName(name)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Set email is not validated");
        }
    }
}
