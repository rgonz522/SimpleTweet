package com.codepath.apps.restclienttemplate;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.sql.Time;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>
{

    Context context;
    List<Tweet> tweets;

    TweetInteractions interactions;

    Layout tweet_layout;

    public static final int ROUNDED_RADIUs = 50;

    //pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets)
    {
        this.context = context;
        this.tweets = tweets;
        interactions = new TweetInteractions(context);

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
        ImageButton ibfavorite;
        ImageButton ibRetweet;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCreatedat = itemView.findViewById(R.id.tvCreatedAt);
            ivTweetPic = itemView.findViewById(R.id.ivTweetPic);
            ibreply = itemView.findViewById(R.id.ibReply);
            ibfavorite = itemView.findViewById(R.id.ibFavorite);
            ibRetweet = itemView.findViewById(R.id.ibRetweet);

        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screen_name);
            tvCreatedat.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs * 2)).into(ivProfileImage);

            if (tweet.img_url != null) {
                Glide.with(context).load(tweet.img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivTweetPic);
            } else {
                ivTweetPic.setVisibility(View.GONE);
            }

            tvBody.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
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
            });
            ibreply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  showComposeDialog(tvScreenName.getText().toString());

                }
            });
            ibRetweet.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                        interactions.retweetingTweet(tweets.get(getAdapterPosition()).id);

                }
            });
            ibfavorite.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                       if(!tweet.liked_by_current_user)
                       {
                           interactions.favoringTweet(tweets.get(getAdapterPosition()).id);
                           tweet.liked_by_current_user = true;
                       } else
                           {
                               interactions.unfavoringTweet(tweets.get(getAdapterPosition()).id);
                               tweet.liked_by_current_user = false;
                       }

                }
            });


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(User.class.getSimpleName(), Parcels.wrap(tweet.user));
                    context.startActivity(intent);
                }
            };
            tvScreenName.setOnClickListener(onClickListener);
            ivProfileImage.setOnClickListener(onClickListener);

        }

        @Override
        public void onClick(View view) {


        }


    }
    private void showComposeDialog(String replyScreenName) {

        FragmentManager fm = ((TimeLineActivity) context).getSupportFragmentManager();

        ComposeFragment composeFragment = ComposeFragment.newInstance(replyScreenName);
        Log.i("Composefragment", "showComposeDialog: " + "creating Fragment");
        composeFragment.show(fm, "fragment_edit_name");

    }



}
