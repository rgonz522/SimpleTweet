package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGreplyScreenName = "param1";
    private static final String ARG_PARAM2 = "param2";

    String replyScreenName = "";
    EditText etCompose;
    Button btTweet;
    TextView tvCounter;


    TwitterClient client;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComposeFragment() {
        // Required empty public constructor
    }


    public static final int MAX_CHAR_TWEET = 280;

    public static final String TAG = "ComposeFragment";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param replyname Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String replyname, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARGreplyScreenName, replyname);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            setReplyScreenName(getArguments().getString(ARGreplyScreenName));

            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        client = TwitterApp.getRestClient(getActivity());







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_compose, container, false);


        btTweet = view.findViewById(R.id.btTweet);
        etCompose = view.findViewById(R.id.etCompose);
        tvCounter = view.findViewById(R.id.tvCounter);
        //set click listener on button
        //make an API Call to twitter
        btTweet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final String tweetContent = replyScreenName + etCompose.getText().toString();

                if (tweetContent.isEmpty()) {
                    Toast.makeText(getActivity(), "Sorry your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tweetContent.length() > MAX_CHAR_TWEET) {
                    Toast.makeText(getActivity(), "Sorry your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onClick: TWEET" + tweetContent);
                Toast.makeText(getActivity(), tweetContent, Toast.LENGTH_SHORT).show();

            client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json)
                    {
                        Log.i(TAG, "onSuccess:  to publish tweet" );
                        try {
                            Tweet tweet = Tweet.fromJSONObject(json.jsonObject);
                            Log.i(TAG, "Published tweet says" + tweet);

                            Log.i(TAG, "onSuccess:  reply" + tweet.body) ;
                            dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
                    {
                        Log.d(TAG, "onFailure: to publish tweet ", throwable);
                    }
                });
            }
        });

        etCompose.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                int current_char    = etCompose.getText().toString().length();
                int char_left       = MAX_CHAR_TWEET - current_char;

                tvCounter.setText( char_left + " characters left");

                if(char_left < 0 ){ tvCounter.setTextColor(Color.RED);}
                else { tvCounter.setTextColor(Color.BLACK); }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });



        return  view;
    }
    public void setReplyScreenName(String screenname)
    {
        Log.i(TAG, "setReplyScreenName: " + screenname);
        if(screenname.length() > 0 && !screenname.contains("Some Title")) replyScreenName = "@" + screenname + " " ;
        Log.i(TAG, "setReplyScreenName: " + replyScreenName);
        Log.i(TAG, "setReplyScreenName: is reply empty" + (replyScreenName == null));
        Log.i(TAG, "setReplyScreenName: is reply empty" + (replyScreenName.length()));
    }



}