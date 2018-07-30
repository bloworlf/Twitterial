package com.codepath.apps.restclienttemplate.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.FollowAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.others.SpacesItemDecoration;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class FollowList extends AppCompatActivity {

    private User userFollowers, userFriends;
    private long next_cursor;

    TwitterClient twitterClient;
    ArrayList<User> users;
    //FollowArrayAdapter followArrayAdapter;
    //ListView listView;
    FollowAdapter followAdapter;
    RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        userFollowers = new User();
        userFriends = new User();
        userFollowers = Parcels.unwrap(getIntent().getParcelableExtra("userFollowers"));
        userFriends = Parcels.unwrap(getIntent().getParcelableExtra("userFriends"));

        Toolbar toolbar = findViewById(R.id.follow_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.WHITE);
        if (userFollowers != null) {
            toolbar.setTitle("@"+ userFollowers.screen_name);
        }
        if(userFriends != null){
            toolbar.setTitle("@"+ userFriends.screen_name);
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });



        twitterClient = TwitterApp.getTwitterClient(this);

        //listView = findViewById(R.id.follow_listview);
        recyclerView = findViewById(R.id.follow_recyclerview);
        users = new ArrayList<>();
        //followArrayAdapter = new FollowArrayAdapter(this, users);
        followAdapter = new FollowAdapter(users);
        //listView.setAdapter(followArrayAdapter);
        recyclerView.setAdapter(followAdapter);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        SpacesItemDecoration decoration = new SpacesItemDecoration(10);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    listView.setNestedScrollingEnabled(true);
        //}

        /*
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (followArrayAdapter.getCount() - 1)) {
                    // Now your listview has hit the bottom
                    loadMoreFollowers(userFollowers.uid, next_cursor);

                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        */

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    if(userFollowers != null){
                        loadMoreFollowers(userFollowers.uid, next_cursor);
                    }
                    else if (userFriends != null){
                        loadMoreFriends(userFriends.uid, next_cursor);
                    }

                }
            }
        });

        if(userFollowers != null) {
            loadFollowers(userFollowers.uid);
        }
        else if(userFriends != null){
            loadFriends(userFriends.uid);
        }
    }

    private void loadMoreFriends(long uid, long cursor) {
        twitterClient.viewMoreFriends(new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                JSONArray userArray;

                                                try {
                                                    userArray = response.getJSONArray("users");

                                                    users.addAll(User.fromJSONArray(userArray));
                                                    //followArrayAdapter.notifyDataSetChanged();
                                                    followAdapter.notifyItemInserted(users.size() - 1);
                                                    next_cursor = response.getLong("next_cursor");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                uid,
                cursor);
    }

    private void loadFriends(long uid) {
        users.clear();
        //followArrayAdapter.notifyDataSetChanged();
        followAdapter.notifyDataSetChanged();
        twitterClient.viewFriends(new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            JSONArray userArray;

                                            try {
                                                userArray = response.getJSONArray("users");
                                                //for (int i=0;i<userArray.length();i++){
                                                //    users.add(User.fromJSON(userArray.getJSONObject(i)));
                                                //}
                                                users.addAll(User.fromJSONArray(userArray));
                                                //followArrayAdapter.notifyDataSetChanged();
                                                followAdapter.notifyItemInserted(0);
                                                next_cursor = response.getLong("next_cursor");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    },
                uid);
    }

    private void loadMoreFollowers(long uid, long cursor) {
        twitterClient.viewMoreFollowers(new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                JSONArray userArray;

                                                try {
                                                    userArray = response.getJSONArray("users");

                                                    users.addAll(User.fromJSONArray(userArray));
                                                    //followArrayAdapter.notifyDataSetChanged();
                                                    followAdapter.notifyItemInserted(users.size() - 1);
                                                    next_cursor = response.getLong("next_cursor");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                uid,
                cursor);
    }

    private void loadFollowers(long uid) {
        users.clear();
        //followArrayAdapter.notifyDataSetChanged();
        followAdapter.notifyDataSetChanged();
        twitterClient.viewFollowers(new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            JSONArray userArray;

                                            try {
                                                userArray = response.getJSONArray("users");
                                                //for (int i=0;i<userArray.length();i++){
                                                //    users.add(User.fromJSON(userArray.getJSONObject(i)));
                                                //}
                                                users.addAll(User.fromJSONArray(userArray));
                                                //followArrayAdapter.notifyDataSetChanged();
                                                followAdapter.notifyItemInserted(0);
                                                next_cursor = response.getLong("next_cursor");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    },
                uid);
    }
}
