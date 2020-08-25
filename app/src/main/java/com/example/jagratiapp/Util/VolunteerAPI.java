package com.example.jagratiapp.Util;

import android.app.Application;

public class VolunteerAPI extends Application {

    private String name;
    private String id;
    private static VolunteerAPI instance;

    public static VolunteerAPI getInstance(){
        if (instance != null)
            instance = new VolunteerAPI();
        return instance;
    }


    public VolunteerAPI(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
