package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

import org.json.JSONException;

import okhttp3.Headers;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance(); // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;       // Change this inside apikey.properties
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET; // Change this inside apikey.properties

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public static User Current_User;

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));

	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeLine(JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		Log.d("TwitterClient", "getHomeTimeLine: " );
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}
	public void publishTweet(String tweetContent, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("status", tweetContent);
		Log.d("TwitterClient", "getHomeTimeLine: " );
		params.put("since_id", 1);
		client.post(apiUrl, params,"", handler);
	}
	public void getNextPageOfTweets(JsonHttpResponseHandler handler, long maxId) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", 1);
		client.get(apiUrl, params, handler);
	}

	public void publishRetweet(Long tweetID, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/statuses/retweet/"+ tweetID+ ".json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("id", tweetID);
		Log.d("TwitterClient", "getHomeTimeLine: " );

		client.post(apiUrl, params,"", handler);
	}
	public void favoringTweet( Long tweetID, JsonHttpResponseHandler handler) {

		String apiUrl = getApiUrl("favorites/create.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("id", tweetID);
		Log.d("TwitterClient", "getHomeTimeLine: " );

		client.post(apiUrl, params,"", handler);
	}
	public void unfavoringTweet( Long tweetID, JsonHttpResponseHandler handler) {

		String apiUrl = getApiUrl("favorites/destroy.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("id", tweetID);
		Log.d("TwitterClient", "getHomeTimeLine: " );

		client.post(apiUrl, params,"", handler);
	}
	public void getUser() {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

		Log.d("TwitterClient", "getHomeTimeLine: " );

		client.get(apiUrl, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Headers headers, JSON json)
			{
				try {
					Current_User = User.fromJSONOBJECT(json.jsonObject);
					Log.i("Twitter", "onSuccess: " +  Current_User.screen_name);
				} catch (JSONException e)
				{
					Log.e("TwitterClient", "failed to get User From JSOn: ",e );
				}
			}

			@Override
			public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
			{

			}
		});
	}

	public void getListFollowers(long user_id, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		client.get(apiUrl, params, handler);
	}

	public void getCurrentUserListFollowers( JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		client.get(apiUrl, params, handler);
	}

	public void getListFollowing(long user_id, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		client.get(apiUrl, params, handler);
	}
	public void getCurrentUserListFollowing( JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friends/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		client.get(apiUrl, params, handler);
	}

}
