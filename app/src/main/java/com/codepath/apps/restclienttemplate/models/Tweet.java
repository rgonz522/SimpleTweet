package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id",childColumns = "userId"))
public class Tweet
{
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public long id;

    @ColumnInfo
    public long userId;

    @Ignore
    public User user;

    public String img_url;

    public static Tweet fromJSONObject(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();

            tweet.body = json.getString("text");
            tweet.createdAt = getRelativeTimeAgo(json.getString("created_at"));
            tweet.user = User.fromJSONOBJECT(json.getJSONObject("user"));

            tweet.id = json.getLong("id");
            tweet.img_url = getImageUrlFromJson(json , tweet);
            Log.i("Tweet", "fromJSONObject: " + tweet.img_url);
            tweet.userId = tweet.user.id;

            return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
            List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
                tweets.add(fromJSONObject(jsonArray.getJSONObject(i)));

        }
        return tweets;
    }

    public static String getImageUrlFromJson(JSONObject jsonObject, Tweet tweet) throws JSONException {


        Log.i("Tweet", "getImageUrlFromJson: " + jsonObject.toString());



            JSONObject entities = jsonObject.getJSONObject("entities");
            Log.i("Tweet", "getImageUrlFromJson: entities " + entities.toString());
            boolean hasMedia = entities.has("media");
            if(hasMedia)
            {
                String mediaURL = entities.getJSONArray("media").getJSONObject(0).getString("media_url_https");
                tweet.img_url = mediaURL;
            } else
                {
                tweet.img_url = null;
                }
            Log.i("Tweet", "getImageUrlFromJson: " + hasMedia);

            Log.i("Tweet", "getImageUrlFromJson: " + tweet.img_url);





        return tweet.img_url;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}
