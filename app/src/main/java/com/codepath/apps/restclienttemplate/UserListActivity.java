package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class UserListActivity extends AppCompatActivity {
    TwitterClient client;
    RecyclerView rvUsers;

    Toolbar compose_bar;
    Toolbar navigation_bar;

    User current_user;

    List<User> users;
    UserAdapter adapter;

    boolean follower_list;
    long user_id;

    public static final int ROUNDED_RADIUs = 50;

    public static final String TAG = "UserListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        user_id = getIntent().getLongExtra("user_id", 0);
        follower_list = getIntent().getBooleanExtra("follower_list", true);

        navigation_bar = (Toolbar) findViewById(R.id.navigation_bar);
        compose_bar     = (Toolbar)findViewById(R.id.compose_bar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null


        setSupportActionBar(compose_bar);




        client = TwitterApp.getRestClient(this);


        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);

        users = new ArrayList<>();
        adapter = new UserAdapter(this,users);
        //Recycler view set up: layout manager and adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);
        rvUsers.setAdapter(adapter);

        if(follower_list)
        {
            populateUserFollowerLists();
        } else
            {
                populateUserFollowingLists();
        }

    }


    private void populateUserFollowerLists()
    {
        client.getListFollowers(user_id,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.i(TAG, "onSuccess: " + json.toString());
                JSONObject jsonObject = json.jsonObject;


                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");

                        adapter.clear();
                        adapter.addAll(User.fromJSONArray(jsonArray));

                } catch (JSONException e) {
                    Log.e(TAG, "JSon Array exception",e );

                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
            {
                Log.e(TAG, "onFailure: " + response, throwable);
            }
        });


    }
    private void populateUserFollowingLists()
    {
        client.getListFollowing(user_id, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.i(TAG, "onSuccess: " + json.toString());
                JSONObject jsonObject = json.jsonObject;


                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("users");

                    adapter.clear();
                    adapter.addAll(User.fromJSONArray(jsonArray));
                } catch (JSONException e) {
                    Log.e(TAG, "JSon exception",e );

                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
            {
                Log.e(TAG, "onFailure: " + response, throwable);
            }
        });


    }
}