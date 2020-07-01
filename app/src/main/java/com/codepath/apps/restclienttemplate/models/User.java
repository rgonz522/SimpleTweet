package com.codepath.apps.restclienttemplate.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
@Entity
public class User
{
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String screen_name;
    @ColumnInfo
    public String profile_img_url;



    public static User fromJSONOBJECT(JSONObject json) throws JSONException {
        User user = new User();

       user.id = json.getLong("id");
        user.name = json.getString("name");
        user.screen_name = json.getString("screen_name");
        user.profile_img_url = json.getString("profile_image_url_https");


        return user;
    }

    public String getName() {
        return name;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }



}
