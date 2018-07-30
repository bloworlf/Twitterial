package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class Timeline extends Fragment{

    TwitterClient twitterClient;
    public static TweetAdapter tweetAdapter;
    public static ArrayList<Tweet> tweets;
    public static RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    //private FloatingActionButton floatingActionButton;


    public Timeline(){
        //Requires an empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //floatingActionButton = view.findViewById(R.id.fabButton);

        twitterClient = TwitterApp.getTwitterClient(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout = view.findViewById(R.id.timeline_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTimeline();
            }
        });
        recyclerView = view.findViewById(R.id.tweet_recyclerView);
        tweets = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getContext(), tweets);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    loadMore(tweets.get(tweets.size()-1).id);
                }
                //else if(!recyclerView.canScrollVertically(-1)){
                //loadRecent(tweets.get(0).id);
                //}
            }
        });

        //floatingActionButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Compose compose = Compose.newInstance();
        //        compose.show(getActivity().getSupportFragmentManager(), "ComposeFragment");
        //    }
        //});

        loadTimeline();

    }

    private void loadRecent(long id) {
        twitterClient.loadRecentTweets(new JsonHttpResponseHandler(){
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
                                           public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                               Log.e("FAILURE", errorResponse.toString());
                                           }

                                       },
                id);
    }

    private void loadMore(long id) {
        //TimelineActivity.progressAction.setVisible(true);
        twitterClient.loadMoreTweets(new JsonHttpResponseHandler(){
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

    private void loadTimeline(){
        //TimelineActivity.progressAction.setVisible(true);
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        twitterClient.getHomeTimeline(new JsonHttpResponseHandler(){
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
                Log.e("FAILURE", errorResponse.toString());
            }
        });
    }
}
