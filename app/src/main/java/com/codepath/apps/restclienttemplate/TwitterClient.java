package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

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
	public static final String REST_CONSUMER_KEY = "rTyC9lkcJsx2rQJdAabyepjZV";       // Change this
	public static final String REST_CONSUMER_SECRET = "DEgGdS2A33CgHGDraBQQFOhYoLWnoWRm5dmFWG2tyD4IXs9C2k"; // Change this

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", "25");
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}

	public void loadMoreTweets(AsyncHttpResponseHandler handler, long id){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        params.put("max_id", id - 1);
        client.get(apiUrl, params, handler);
    }

    public void loadRecentTweets(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "25");
		params.put("since_id", id + 1);
		client.get(apiUrl, params, handler);
	}

	public void postTweet(AsyncHttpResponseHandler handler, String status){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		client.post(apiUrl, params, handler);
	}

	public void replyTweet(AsyncHttpResponseHandler handler, String status, long id){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		params.put("in_reply_to_status_id", id);
		client.post(apiUrl, params, handler);
	}

	public void deleteTweet(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/destroy/"+id+".json");
		client.post(apiUrl, handler);
	}

	public void getCurrentUser(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, handler);
	}

	public void getUserTimeline(JsonHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		client.get(apiUrl, handler);
	}

	public void getTweetDetail(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/show.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.get(apiUrl, params, handler);
	}

	public void reTweet(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/retweet/"+id+".json");
		//RequestParams params = new RequestParams();
		//params.put("id", id);
		client.post(apiUrl, handler);
	}

	public void unRetweet(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/unretweet/"+id+".json");
		client.post(apiUrl, handler);
	}

	public void likeTweet(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.post(apiUrl, params, handler);
	}

	public void unlikeTweet(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.post(apiUrl, params, handler);
	}

	public void getMentionTimeline(JsonHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("count", "25");
		params.put("since_id", 1);
		client.get(apiUrl, params, handler);
	}

	public void loadMoreMentions(AsyncHttpResponseHandler handler, long id){
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", "25");
		params.put("max_id", id - 1);
		client.get(apiUrl, params, handler);
	}

	public void loadUserTweet(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void loadMoreUserTweet(AsyncHttpResponseHandler handler, long uid, long id){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", id - 1);
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void getFriendshipStatus(AsyncHttpResponseHandler handler, long sourceID, long targetID){
		String apiUrl = getApiUrl("friendships/show.json");
		RequestParams params = new RequestParams();
		params.put("source_id", sourceID);
		params.put("target_id", targetID);
		client.get(apiUrl, params, handler);
	}

	public void loadUserFavorites(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("favorites/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void loadMoreUserFavorites(AsyncHttpResponseHandler handler, long uid, long id){
		String apiUrl = getApiUrl("favorites/list.json");
		RequestParams params = new RequestParams();
		params.put("max_id", id - 1);
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void createFriendShip(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("friendships/create.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		params.put("follow", true);
		client.post(apiUrl, params, handler);
	}

	public void destroyFriendShip(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("friendships/destroy.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		client.post(apiUrl, params, handler);
	}

	public void viewFollowers(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void viewMoreFollowers(AsyncHttpResponseHandler handler, long uid, long cursor){
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		params.put("cursor", cursor);
		client.get(apiUrl, params, handler);
	}

	public void viewFriends(AsyncHttpResponseHandler handler, long uid){
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		client.get(apiUrl, params, handler);
	}

	public void viewMoreFriends(AsyncHttpResponseHandler handler, long uid, long cursor){
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		params.put("user_id", uid);
		params.put("cursor", cursor);
		client.get(apiUrl, params, handler);
	}
	public void showUser(AsyncHttpResponseHandler handler, String screen_name){
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}
	public void searchTweets(AsyncHttpResponseHandler handler, String query){
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q", query);
		client.get(apiUrl, params, handler);
	}
	public void searchMoreTweets(AsyncHttpResponseHandler handler, String query, long id){
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q", query);
		params.put("max_id", id - 1);
		client.get(apiUrl, params, handler);
	}



	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
