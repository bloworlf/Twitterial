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
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Mentions extends Fragment {

    TwitterClient twitterClient;
    public static TweetAdapter tweetAdapter;
    public static ArrayList<Tweet> tweets;
    public static RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    public Mentions(){
        //Requires empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mentions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        twitterClient = TwitterApp.getTwitterClient(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout = view.findViewById(R.id.mentions_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMentions();
            }
        });
        recyclerView = view.findViewById(R.id.mentions_recyclerView);
        tweets = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getContext(), tweets);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    loadMoreMentions(tweets.get(tweets.size()-1).id);
                }
                //else if(!recyclerView.canScrollVertically(-1)){
                //loadRecent(tweets.get(0).id);
                //}
            }
        });

        loadMentions();

    }

    private void loadMoreMentions(long id) {
        //TimelineActivity.progressAction.setVisible(true);
        twitterClient.loadMoreMentions(new JsonHttpResponseHandler(){
                                         @Override
                                         public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                             for (int i=0;i<response.length();i++){
                                                 try {
                                                     Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                                     tweets.add(tweet);
                                                     if (tweets.size() > 0){
                                                         tweetAdapter.notifyItemInserted(tweets.size() - 1);
                                                     }
                                                     else {
                                                         tweetAdapter.notifyItemInserted(0);
                                                     }

                                                 }
                                                 catch (JSONException e){
                                                     e.printStackTrace();
                                                 }
                                             }
                                             //TimelineActivity.progressAction.setVisible(false);
                                         }
                                         @Override
                                         public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                             Log.e("FAILURE", errorResponse.toString());
                                             //TimelineActivity.progressAction.setVisible(false);
                                         }
                                     },
                id);
    }

    private void loadMentions() {
        //TimelineActivity.progressAction.setVisible(false);
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        twitterClient.getMentionTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //super.onSuccess(statusCode, headers, response);

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
                //TimelineActivity.progressAction.setVisible(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
                //TimelineActivity.progressAction.setVisible(false);
                Log.e("FAILURE", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                swipeRefreshLayout.setRefreshing(false);
                //TimelineActivity.progressAction.setVisible(false);
                Log.e("FAILURE", errorResponse.toString());
            }
        });
    }
}
