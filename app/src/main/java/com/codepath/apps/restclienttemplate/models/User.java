package com.codepath.apps.restclienttemplate.models;


import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

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

    public int followers;

    public int following;

    public String banner_url;

    public static User fromJSONOBJECT(JSONObject json) throws JSONException {
        User user = new User();

        user.id = json.getLong("id");
        user.name = json.getString("name");
        user.screen_name = json.getString("screen_name");
        user.profile_img_url = json.getString("profile_image_url_https");
        user.followers = json.getInt("followers_count");
        user.following = json.getInt("friends_count");



        return user;
    }


    public String setBannerUrl(JSONObject jsonObject) throws JSONException
    {
         banner_url = jsonObject.getString("profile_banner_url");
         return banner_url;
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

    public static List<User> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            users.add(fromJSONOBJECT(jsonArray.getJSONObject(i)));

        }
        return users;
    }

}
