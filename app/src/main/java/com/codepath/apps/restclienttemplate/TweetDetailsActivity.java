package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    ImageView ivTweetPic;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvCreatedat;
    ImageButton ibreply;
    ImageButton ibfavorite;

    Toolbar compose_bar;

    TwitterClient client;

    public static final String TAG = "TweetDetailsActivity";
    public static final int ROUNDED_RADIUs = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvCreatedat = findViewById(R.id.tvCreatedAt);
        ivTweetPic = findViewById(R.id.ivTweetPic);
        ibreply = findViewById(R.id.ibReply);
        compose_bar     = (Toolbar)findViewById(R.id.compose_bar);
        ibfavorite  = findViewById(R.id.ibFavorite);

        tvScreenName.setText(null);
        setSupportActionBar(compose_bar);

        client = new TwitterClient(this);
        final Tweet tweet = (Tweet)  Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screen_name);
        tvCreatedat.setText(tweet.createdAt);
        Glide.with(this).load(tweet.user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs * 2)).into(ivProfileImage);

        if (tweet.img_url != null) {
            Glide.with(this).load(tweet.img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivTweetPic);
        } else {
            ivTweetPic.setVisibility(View.GONE);
        }

        ibreply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showComposeDialog(tvScreenName.getText().toString());

            }
        });

        ibfavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                favoringTweet(tweet.id, false);
                Log.i("Tweet details", "onClick: " + "liking tweet");
                Log.i("tweet details", "onClick: " + "twwt id: " + tweet.userId);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.compose)
        {
          showComposeDialog("");
        }


        return super.onOptionsItemSelected(item);
    }
    private void showComposeDialog(String replyName) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(replyName, "String 2");
        composeFragment.show(fm, "fragment_edit_name");
    }



    public void favoringTweet(long tweet_id, boolean liking_or_unliking)
    {

        client.favoringTweet(liking_or_unliking, tweet_id, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Headers headers, JSON json)
        {
            Log.i(TAG, "onSuccess:  to publish tweet" );
        try {
            Tweet tweet = Tweet.fromJSONObject(json.jsonObject);
            Log.i(TAG, "Published tweet says" + tweet);

            Log.i(TAG, "onSuccess:  reply" + tweet.body) ;

        } catch (JSONException e) {
        e.printStackTrace();
        }
        }

        @Override
        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
        {
            Log.d(TAG, "onFailure: to publish tweet ", throwable);
        }
        }
        );
    }
}