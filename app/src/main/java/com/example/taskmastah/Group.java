package com.example.taskmastah;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.List;

@IgnoreExtraProperties
public class Group {
    public String name;
    public String owner;

    public Group() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Group(String name, String owner){
        this.name = name;
        this.owner = owner;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
