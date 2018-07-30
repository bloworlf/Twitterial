package com.codepath.apps.restclienttemplate.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewTweets extends AppCompatActivity {

    ArrayList<Tweet> tweets;
    TweetAdapter tweetAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TwitterClient twitterClient;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tweets);

        tweets = Parcels.unwrap(getIntent().getParcelableExtra("tweets"));
        query = getIntent().getStringExtra("query");

        Toolbar toolbar = findViewById(R.id.tweets_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("#"+query);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        twitterClient = TwitterApp.getTwitterClient(this);

        tweetAdapter = new TweetAdapter(this, tweets);
        tweetAdapter.notifyItemInserted(0);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.tweets_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    searchMoreTweets(tweets.get(tweets.size()-1).id);
                }
            }
        });
    }

    private void searchMoreTweets(long id) {
        twitterClient.searchMoreTweets(new JsonHttpResponseHandler(){
                                           @Override
                                           public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                               JSONArray tweetArray;

                                               try {
                                                   tweetArray = response.getJSONArray("statuses");
                                                   tweets.addAll(Tweet.fromJSONArray(tweetArray));
                                                   tweetAdapter.notifyItemInserted(tweets.size()-1);
                                               }
                                               catch (JSONException e){
                                                   e.printStackTrace();
                                               }
                                           }
                                       },
                query,
                id);
    }
}
