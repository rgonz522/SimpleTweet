package com.codepath.apps.restclienttemplate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import com.codepath.apps.restclienttemplate.models.User;
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
    ImageButton ibretweet;

    Toolbar compose_bar;

    TwitterClient client;
    TweetInteractions interactions;

    public static final String TAG = "TweetDetailsActivity";
    public static final int ROUNDED_RADIUs = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        interactions = new TweetInteractions(this);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvCreatedat = findViewById(R.id.tvCreatedAt);
        ivTweetPic = findViewById(R.id.ivTweetPic);
        ibreply = findViewById(R.id.ibReply);
        compose_bar = (Toolbar) findViewById(R.id.compose_bar);
        ibfavorite = findViewById(R.id.ibFavorite);

        ibretweet = findViewById(R.id.ibRetweet);

        tvScreenName.setText(null);
        setSupportActionBar(compose_bar);

        final Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));



       //



        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screen_name);
        tvCreatedat.setText(tweet.createdAt);
        Glide.with(this).load(tweet.user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs * 2)).into(ivProfileImage);

        if (tweet.img_url != null) {
            Glide.with(this).load(tweet.img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivTweetPic);
        } else {
            ivTweetPic.setVisibility(View.GONE);
        }
        Log.i(TAG, "onClick: " + tweet.user.screen_name);
        ivProfileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                 Intent intent = new Intent(TweetDetailsActivity.this, UserProfileActivity.class);
                 intent.putExtra(User.class.getSimpleName(), Parcels.wrap(tweet.user));
                 startActivity(intent);
            }
        });

        ibreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog(tvScreenName.getText().toString());

            }
        });

        ibfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!tweet.liked_by_current_user) {
                   interactions.favoringTweet(tweet.id);
                   tweet.liked_by_current_user = true;

               } else
                   {   interactions.unfavoringTweet(tweet.id);
                       tweet.liked_by_current_user = false;


               }
            }
        });

        ibretweet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                interactions.retweetingTweet(tweet.id);

                Log.i("Tweet details", "onClick: " + "retweeting tweet");
                Log.i("tweet details", "onClick: " + "twwt id: " + tweet.userId);

            }
        });

        updateImageButtonsColor(tweet);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));


        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.compose) {
            showComposeDialog("");
        }


        return super.onOptionsItemSelected(item);
    }

    private void showComposeDialog(String replyName) {
        FragmentManager fm = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance(replyName);
        composeFragment.show(fm, "fragment_edit_name");
    }


    public void updateImageButtonsColor(Tweet tweet)
    {
        if(tweet.liked_by_current_user) {ibfavorite.setImageResource(R.drawable.ic_vector_heart);}
        else{ibfavorite.setImageResource(R.drawable.ic_vector_heart_stroke);}

        if(tweet.liked_by_current_user) {ibretweet.setImageResource(R.drawable.ic_vector_retweet);}
        else{ibretweet.setImageResource(R.drawable.ic_vector_retweet_stroke);}
    }

}