package com.codepath.apps.restclienttemplate.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.FollowArrayAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FollowList extends AppCompatActivity {

    private User user;
    private long next_cursor;

    TwitterClient twitterClient;
    ArrayList<User> users;
    FollowArrayAdapter followArrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        user = new User();
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        Toolbar toolbar = findViewById(R.id.follow_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setTitle("@"+user.screen_name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });



        twitterClient = TwitterApp.getTwitterClient(this);

        listView = findViewById(R.id.follow_listview);
        users = new ArrayList<>();
        followArrayAdapter = new FollowArrayAdapter(this, users);
        listView.setAdapter(followArrayAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (followArrayAdapter.getCount() - 1)) {
                    // Now your listview has hit the bottom
                    loadMoreFollowers(user.uid, next_cursor);

                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        loadFollowers(user.uid);
    }

    private void loadMoreFollowers(long uid, long cursor) {
        twitterClient.viewMoreFollowers(new JsonHttpResponseHandler(){
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                JSONArray userArray;

                                                try {
                                                    userArray = response.getJSONArray("users");

                                                    users.addAll(User.fromJSONArray(userArray));
                                                    followArrayAdapter.notifyDataSetChanged();
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
        followArrayAdapter.notifyDataSetChanged();
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
                                                followArrayAdapter.notifyDataSetChanged();
                                                next_cursor = response.getLong("next_cursor");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    },
                uid);
    }
}
