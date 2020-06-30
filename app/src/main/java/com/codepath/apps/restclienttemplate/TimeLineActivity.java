package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimeLineActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE = 28;
    TwitterClient client;
    RecyclerView rvTweets;

    List<Tweet> tweets;
    TweetsAdapter adapter;

    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    public static final String TAG = "TimeLineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);


        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Log.i(TAG, "onRefresh: fetching new data");
                populateHomeTimeLine();

            }
        });
        //Find the recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        //init the list of tweets
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this,tweets);
        //Recycler view set up: layout manager and adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };

        rvTweets.addOnScrollListener(scrollListener);
        populateHomeTimeLine();
    }

    private void loadMoreData()
    {
        client.getNextPageOfTweets(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.i(TAG, "onSuccess:  for loadmore data " + json.toString());
                JSONArray jsonArray = json.jsonArray;

                try {
                    List<Tweet> tweets = Tweet.fromJSONArray(jsonArray);
                    adapter.addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
            {
                Log.e(TAG, "onFailure: " + throwable );
            }
        }, tweets.get(tweets.size()-1).id);
    }

    private void populateHomeTimeLine()
    {
        client.getHomeTimeLine(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.i(TAG, "onSuccess: " + json.toString());
                JSONArray jsonArray = json.jsonArray;

                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJSONArray(jsonArray));
                    swipeContainer.setRefreshing(false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose)
        {
            Toast.makeText(this, "Compose", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            //get data from intent (tweet object)
          Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //update recyclerview
            //modify data source
            tweets.add(0, tweet);
            //update adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}