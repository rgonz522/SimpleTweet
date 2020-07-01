package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    ImageView ivTweetPic;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvCreatedat;
    ImageButton ibreply;

    Toolbar compose_bar;

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

        setSupportActionBar(compose_bar);

        Tweet tweet = (Tweet)  Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screen_name);
        tvCreatedat.setText(tweet.createdAt);
        Glide.with(this).load(tweet.user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs * 2)).into(ivProfileImage);

        if (tweet.img_url != null) {
            Glide.with(this).load(tweet.img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivTweetPic);
        } else {
            ivTweetPic.setVisibility(View.GONE);
        }

        ibreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailsActivity.this, "Compose", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(TweetDetailsActivity.this, ComposeActivity.class);
                intent.putExtra("replyScreenName", tvScreenName.getText().toString());
                (TweetDetailsActivity.this).startActivityForResult(intent, TimeLineActivity.REQUEST_CODE);

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
            Toast.makeText(this, "Compose", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, TimeLineActivity.REQUEST_CODE);
        }


        return super.onOptionsItemSelected(item);
    }
}