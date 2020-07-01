package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>
{

    Context context;
    List<Tweet> tweets;


    Layout tweet_layout;

    public static final int ROUNDED_RADIUs = 50;

    //pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets)
    {
        this.context = context;
        this.tweets = tweets;

    }



    @NonNull
    @Override // for each row we're going inflate layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
    }

    //bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
            // get data @ position
          Tweet tweet = tweets.get(position);
            //bind the tweet with the view holder
           holder.bind(tweet);
    }

    @Override
    public int getItemCount()
    {
        return tweets.size();
    }
    //clean elements of the recyclers
    public void clear()
    {
        tweets.clear();
        notifyDataSetChanged();
    }
    //update the the recycler
    public void addAll(List<Tweet> tweetList)
    {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }





    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfileImage;
        ImageView ivTweetPic;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvCreatedat;
        ImageButton ibreply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCreatedat = itemView.findViewById(R.id.tvCreatedAt);
            ivTweetPic = itemView.findViewById(R.id.ivTweetPic);
            ibreply = itemView.findViewById(R.id.ibReply);

            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screen_name);
            tvCreatedat.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs * 2)).into(ivProfileImage);

            if (tweet.img_url != null) {
                Glide.with(context).load(tweet.img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivTweetPic);
            } else {
                ivTweetPic.setVisibility(View.GONE);
            }

            ibreply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Compose", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("replyScreenName", tvScreenName.getText().toString());
                    ((Activity) context).startActivityForResult(intent, TimeLineActivity.REQUEST_CODE);

                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = tweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                Log.i("Tweet Adapter", "onClick: starting Tweet details");
                context.startActivity(intent);
            }

        }


    }
}
