package com.codepath.apps.restclienttemplate.activities;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.adapters.ViewPagerAdapter;
import com.codepath.apps.restclienttemplate.fragments.UserFavorites;
import com.codepath.apps.restclienttemplate.fragments.UserPhotos;
import com.codepath.apps.restclienttemplate.fragments.UserTweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ViewUser extends AppCompatActivity {

    TwitterClient twitterClient;

    Bundle bundle;

    UserTweet userTweet;
    public static User user;
    User self;

    TextView view_user_name, view_user_description, view_user_followers_count, view_user_friends_count, view_user_screen_name;
    ImageButton view_user_followed;
    ImageView view_user_profile_background, view_user_profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        twitterClient = TwitterApp.getTwitterClient(this);

        user = new User();
        self = new User();

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        getCurrentUser();

        //bundle = new Bundle();
        //bundle.putParcelable("user", Parcels.wrap(user));

        userTweet = new UserTweet();
        userTweet.setArguments(bundle);



        Toolbar toolbar = findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        ViewPager viewPager = findViewById(R.id.view_user_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.view_user_tabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setupWithViewPager(viewPager);

        view_user_description = findViewById(R.id.view_user_description);
        view_user_description.setText(user.description);
        Linkify.addLinks(view_user_description, Linkify.ALL);

        view_user_profile_background = findViewById(R.id.view_user_profile_background);
        if (user.profile_use_background_image){
            view_user_profile_background.setVisibility(View.VISIBLE);
            view_user_description.setPadding(0, 0, 0, 0);
            Glide.with(this)
                    .load(Uri.parse(user.profile_background_image_url))
                    .into(view_user_profile_background);
        }
        else {
            //view_user_profile_background.setImageResource(0);
            //view_user_profile_background.setBackgroundColor(Color.parseColor("#"+self.profile_background_color));
            view_user_profile_background.setVisibility(View.GONE);
            view_user_description.setPadding(75, 0, 0, 0);
        }

        view_user_profile = findViewById(R.id.view_user_profile);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    view_user_profile.setZ(10);
        //}
        view_user_profile.bringToFront();
        Glide.with(this)
                .load(user.profile_image_url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(48, 4)))
                .into(view_user_profile);

        view_user_name = findViewById(R.id.view_user_name);
        view_user_name.setText(user.name);

        view_user_screen_name = findViewById(R.id.view_user_screen_name);
        view_user_screen_name.setText("@"+user.screen_name);

        view_user_followed = findViewById(R.id.view_user_followed);
        //getFriendshipStatus(self.uid, self.uid);
        //if(self.follow_request_sent){
        //    view_user_followed.setImageResource(R.drawable.ic_user_added);
        //    view_user_followed.setBackgroundColor(Color.CYAN);
        //}
        //else {
        //    view_user_followed.setImageResource(R.drawable.ic_add_user);
        //    view_user_followed.setBackgroundColor(Color.TRANSPARENT);
        //}

        view_user_followers_count = findViewById(R.id.view_user_followers_count);
        view_user_followers_count.setText(TweetAdapter.withSuffix(user.followers_count));

        view_user_friends_count = findViewById(R.id.view_user_friends_count);
        view_user_friends_count.setText(TweetAdapter.withSuffix(user.friends_count));

    }

    private void getFriendshipStatus(long sourdeID, long targetID) {
        twitterClient.getFriendshipStatus(new JsonHttpResponseHandler(){
                                              @Override
                                              public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                  try {
                                                      if (response.getJSONObject("relationship").getJSONObject("source").getBoolean("following")) {
                                                          view_user_followed.setImageResource(R.drawable.ic_user_added);
                                                          //view_user_followed.setBackgroundColor(Color.CYAN);
                                                          view_user_followed.setBackgroundColor(Color.TRANSPARENT);
                                                      } else {
                                                          view_user_followed.setImageResource(R.drawable.ic_add_user);
                                                          view_user_followed.setBackgroundColor(Color.TRANSPARENT);
                                                      }
                                                  }
                                                  catch (JSONException e){
                                                      e.printStackTrace();
                                                  }
                                              }

                                              @Override
                                              public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                                  Log.d("FRIENDSHIP", errorResponse.toString());
                                              }
                                          },
                sourdeID,
                targetID);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(userTweet, TweetAdapter.withSuffix(user.statuses_count) +" Tweets");
        adapter.addFragment(new UserPhotos(), "Photos");
        adapter.addFragment(new UserFavorites(), TweetAdapter.withSuffix(user.favourites_count)+" Favorites");
        viewPager.setAdapter(adapter);
    }

    private void getCurrentUser(){
        twitterClient.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                self = User.fromJSON(response);
                if(self.uid == user.uid){
                    view_user_followed.setVisibility(View.GONE);
                }
                else {
                    getFriendshipStatus(self.uid, user.uid);
                    view_user_followed.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SELF", errorResponse.toString());
            }
        });
    }
}
