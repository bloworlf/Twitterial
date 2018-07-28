package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.ViewUser;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserTweet extends Fragment {

    private User user;

    TwitterClient twitterClient;
    public static TweetAdapter tweetAdapter;
    public static ArrayList<Tweet> tweets;
    public static RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    public UserTweet(){
        //Bundle bundle = this.getArguments();
        //if (bundle != null){
        //    user = bundle.getParcelable("user");
        //}
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //user = getArguments().getParcelable("user");
        user = new User();
        user = ViewUser.user;
        //user = Parcels.unwrap(getArguments().getParcelable("user"));
        //Log.d("USER", user.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_tweet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //user = new User();

        twitterClient = TwitterApp.getTwitterClient(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout = view.findViewById(R.id.user_tweet_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserTweet(user.uid);
            }
        });
        recyclerView = view.findViewById(R.id.user_tweet_recyclerView);
        tweets = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getContext(), tweets);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (tweets.size() == 0){
                        loadUserTweet(user.uid);
                    }
                    else {
                        loadMoreUserTweet(user.uid, tweets.get(tweets.size()-1).id);
                    }

                }
                //else if(!recyclerView.canScrollVertically(-1)){
                //loadRecent(tweets.get(0).id);
                //}
            }
        });

        loadUserTweet(user.uid);

    }

    private void loadMoreUserTweet(long uid, long id) {
        twitterClient.loadMoreUserTweet(new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                                for (int i=0;i<response.length();i++){
                                                    try {
                                                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                                        tweets.add(tweet);
                                                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                                                    }
                                                    catch (JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                swipeRefreshLayout.setRefreshing(false);
                                            }
                                        },
                uid,
                id);
    }

    private void loadUserTweet(long uid) {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        twitterClient.loadUserTweet(new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                            for (int i=0;i<response.length();i++){
                                                try {
                                                    Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                                    tweets.add(tweet);
                                                    tweetAdapter.notifyItemInserted(0);
                                                }

                                                catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                            swipeRefreshLayout.setRefreshing(false);
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            Log.d("USERTWEET", errorResponse.toString());
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    },
                uid);

    }
}
