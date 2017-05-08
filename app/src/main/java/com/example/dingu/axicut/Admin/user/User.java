package com.example.dingu.axicut.Admin.user;

import com.example.dingu.axicut.UserMode;

/**
 * Created by grey-hat on 7/5/17.
 */

public class User {
    public User(){}
    private String email;
    private String name;
    private UserMode userMode;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }


}
