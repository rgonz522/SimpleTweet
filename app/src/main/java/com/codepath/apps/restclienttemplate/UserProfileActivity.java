package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvscreenname;
    TextView tvrealname;
    TextView tvfollowers;
    TextView tvfollowing;
    ImageView ivProfilePic;

    ImageView ivBanner;

    final int ROUNDED_RADIUS = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Log.i("UserProfileActivity", "onCreate: ");

        tvscreenname =  (TextView) findViewById(R.id.tvScreenName);
        tvrealname =    (TextView) findViewById(R.id.tvRealName);
        tvfollowers =   (TextView) findViewById(R.id.tvFollowers);
        tvfollowing =   (TextView) findViewById(R.id.tvFollowing);
        ivProfilePic =  (ImageView) findViewById(R.id.ivProfileImage);
        ivBanner =      (ImageView) findViewById(R.id.ivBanner);

        final User user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));
        Log.i("userProfile", "onCreate: " + tvscreenname);

        if(user != null)
        {
            tvscreenname.setText("@" + user.screen_name);
            tvrealname.setText(user.name);
            tvfollowers.setText(user.followers + " \tfollowers");
            tvfollowing.setText(user.following + " \tfollowing");
            Glide.with(this).load(user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUS)).into(ivProfilePic);
            Glide.with(this).load(user.banner_url).into(ivBanner);

        }

        tvfollowers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(UserProfileActivity.this, UserListActivity.class);
                intent.putExtra("follower_list",true );
                intent.putExtra("user_id", user.id);
                startActivity(intent);
            }
        });
        tvfollowing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(UserProfileActivity.this, UserListActivity.class);
                intent.putExtra("follower_list",false );
                intent.putExtra("user_id", user.id);
                startActivity(intent);
            }
        });


    }
}