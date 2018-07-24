package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetails extends AppCompatActivity {

    Toolbar toolbar;
    Tweet tweet;

    public ImageView detail_user_profile, detail_tweet_image;
    public VideoView detail_tweet_video;
    public TextView detail_user_name, detail_user_screen_name, detail_tweet_text, detail_tweet_created_at;
    public TextView detail_tweet_favorite_count, detail_tweet_comment_count, detail_tweet_retweet_count;
    public ImageButton detail_tweet_favorited, detail_tweet_retweeted;

    TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("from @"+tweet.user.screen_name);
        getSupportActionBar().setTitle("from @"+tweet.user.screen_name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
                finish();
            }
        });

        detail_user_profile = findViewById(R.id.detail_user_profile);
        detail_user_name = findViewById(R.id.detail_user_username);
        detail_user_screen_name = findViewById(R.id.detail_user_screen_name);
        detail_tweet_created_at = findViewById(R.id.detail_tweet_created_at);
        detail_tweet_text = findViewById(R.id.detail_tweet_text);
        detail_tweet_favorited = findViewById(R.id.detail_tweet_favorited);
        detail_tweet_favorite_count = findViewById(R.id.detail_tweet_favourite_count);
        detail_tweet_comment_count = findViewById(R.id.detail_tweet_comment_count);
        detail_tweet_retweeted = findViewById(R.id.detail_tweet_retweeted);
        detail_tweet_retweet_count = findViewById(R.id.detail_tweet_retweet_count);

        detail_tweet_image = findViewById(R.id.detail_tweet_image);
        detail_tweet_video = findViewById(R.id.detail_tweet_video);

        detail_user_name.setText(tweet.user.name);
        detail_user_screen_name.setText("@"+tweet.user.screen_name);
        Glide.with(this)
                .load(Uri.parse(tweet.user.profile_image_url))
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(detail_user_profile);
        detail_tweet_created_at.setText(TweetAdapter.getRelativeTimeAgo(tweet.created_at));
        detail_tweet_text.setText(tweet.text);
        Linkify.addLinks(detail_tweet_text, Linkify.ALL);
        detail_tweet_favorite_count.setText(String.valueOf(tweet.favorites_count));
        detail_tweet_retweet_count.setText(String.valueOf(tweet.retweet_count));

        if(tweet.media != null){
            if (tweet.media.type.equals("photo")){
                detail_tweet_video.setVisibility(View.GONE);
                detail_tweet_image.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) detail_tweet_image.getLayoutParams();
                layoutParams.height = tweet.media.height/2;
                detail_tweet_image.setLayoutParams(layoutParams);
                Glide.with(this)
                        .load(Uri.parse(tweet.media.media_url))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(48, 4)))
                        .into(detail_tweet_image);
            }
        }
        else{
            detail_tweet_video.setVisibility(View.GONE);
            detail_tweet_image.setVisibility(View.GONE);
        }




    }
}
