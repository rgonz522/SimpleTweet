package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import okhttp3.Headers;

public class TweetInteractions
{
    public static final String TAG = "TweetInteractions";

    TwitterClient client;


    public  TweetInteractions(Context context)
    {
        client = new TwitterClient(context);


    }


    public void favoringTweet(long tweet_id) {

        client.favoringTweet(tweet_id, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess:  to favor tweet");
                        try {
                            Tweet tweet = Tweet.fromJSONObject(json.jsonObject);
                            Log.i(TAG, "Published tweet says" + tweet);
                            Log.i(TAG, "onSuccess:  reply" + tweet.body);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure: to favor tweet ", throwable);
                    }
                }
        );
    }
    public void unfavoringTweet(long tweet_id) {

        client.unfavoringTweet(tweet_id, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess:  to favor tweet");
                        try {
                            Tweet tweet = Tweet.fromJSONObject(json.jsonObject);
                            Log.i(TAG, "Published tweet says" + tweet);
                            Log.i(TAG, "onSuccess:  reply" + tweet.body);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure: to favor tweet ", throwable);
                    }
                }
        );
    }
    public void retweetingTweet(long tweet_id)
    {
        client.publishRetweet(tweet_id, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess:  to retweet tweet");
                        try {
                            Tweet tweet = Tweet.fromJSONObject(json.jsonObject);
                            Log.i(TAG, "Published retweet says" + tweet);
                            Log.i(TAG, "onSuccess:  retweet" + tweet.body);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, "onFailure: to favor tweet ", throwable);
                    }
                }
        );
    }
}
