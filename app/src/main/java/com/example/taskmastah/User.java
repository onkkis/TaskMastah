package com.example.taskmastah;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    public String email;
    public ArrayList<Group> groups;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public String getEncodedEmail() { return email.replace(".", ","); }
    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

}

