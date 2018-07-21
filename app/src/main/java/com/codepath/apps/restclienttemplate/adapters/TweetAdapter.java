package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;

    public TweetAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //Context context = viewGroup.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tweetView = layoutInflater.inflate(R.layout.tweet, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tweet tweet = tweets.get(position);

        viewHolder.user_name.setText(tweet.user.name);
        viewHolder.user_screen_name.setText("@"+tweet.user.screen_name);
        viewHolder.tweet_created_at.setText(getRelativeTimeAgo(tweet.created_at));
        viewHolder.tweet_text.setText(tweet.text);
        Glide.with(context)
                .load(Uri.parse(tweet.user.profile_image_url))
                //.apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(12, 4)))
                .into(viewHolder.user_profile);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView user_profile;
        public TextView user_name, user_screen_name, tweet_text, tweet_created_at;

        public ViewHolder(View view){
            super(view);

            user_profile = view.findViewById(R.id.user_profile);
            user_name = view.findViewById(R.id.user_username);
            user_screen_name = view.findViewById(R.id.user_screen_name);
            tweet_created_at = view.findViewById(R.id.tweet_created_at);
            tweet_text = view.findViewById(R.id.tweet_text);
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
