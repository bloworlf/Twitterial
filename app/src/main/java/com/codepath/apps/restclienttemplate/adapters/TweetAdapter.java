package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.TweetDetails;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.io.Serializable;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Tweet tweet = tweets.get(position);

        viewHolder.tweet_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TweetDetails.class);
                //intent.putExtra("id", String.valueOf(tweet.id));
                intent.putExtra("tweet", (Serializable)tweet);
                context.startActivity(intent);
            }
        });

        viewHolder.user_name.setText(tweet.user.name);
        viewHolder.user_screen_name.setText("@"+tweet.user.screen_name);
        viewHolder.tweet_created_at.setText(getRelativeTimeAgo(tweet.created_at));
        viewHolder.tweet_text.setText(tweet.text);
        if (tweet.favorited){
            viewHolder.tweet_favorited.setImageResource(R.drawable.ic_like_fill);
        }
        else {
            viewHolder.tweet_favorited.setImageResource(R.drawable.ic_like_empty);
        }
        viewHolder.tweet_favorite_count.setText(String.valueOf(tweet.favorites_count));
        if (tweet.retweeted){
            viewHolder.tweet_retweeted.setImageResource(R.drawable.ic_retweet_fill);
        }
        else {
            viewHolder.tweet_retweeted.setImageResource(R.drawable.ic_retweet_empty);
        }
        viewHolder.tweet_retweet_count.setText(String.valueOf(tweet.retweet_count));
        Linkify.addLinks(viewHolder.tweet_text, Linkify.ALL);
        Glide.with(context)
                .load(Uri.parse(tweet.user.profile_image_url))
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                //.apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(12, 4)))
                .into(viewHolder.user_profile);

        viewHolder.tweet_play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.tweet_video.isPlaying()){
                    viewHolder.tweet_video.pause();
                }
                else {
                    viewHolder.tweet_video.start();
                }
            }
        });
        viewHolder.tweet_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.tweet_play_video.getVisibility() == View.VISIBLE){
                    viewHolder.tweet_play_video.setVisibility(View.GONE);
                }
                else {
                    viewHolder.tweet_play_video.setVisibility(View.VISIBLE);
                }
            }
        });

        if(tweet.media != null){
            if (tweet.media.type.equals("photo")){
                viewHolder.tweet_video.setVisibility(View.GONE);
                viewHolder.tweet_image.setVisibility(View.VISIBLE);
                viewHolder.tweet_play_video.setVisibility(View.GONE);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.tweet_image.getLayoutParams();
                layoutParams.height = tweet.media.height/2;
                viewHolder.tweet_image.setLayoutParams(layoutParams);
                Glide.with(context)
                        .load(Uri.parse(tweet.media.media_url))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(48, 4)))
                        .into(viewHolder.tweet_image);
            }
            else {
                viewHolder.tweet_video.setVisibility(View.VISIBLE);
                viewHolder.tweet_image.setVisibility(View.GONE);
                viewHolder.tweet_play_video.setVisibility(View.VISIBLE);

                Log.d("VIDEO1", tweet.media.media_url);
                Log.d("VIDEO2", tweet.media.display_url);
                Log.d("VIDEO3", tweet.media.expanded_url);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.tweet_image.getLayoutParams();
                layoutParams.height = tweet.media.height/2;
                viewHolder.tweet_video.setLayoutParams(layoutParams);

                viewHolder.tweet_video.setVideoURI(Uri.parse(tweet.media.expanded_url));
                viewHolder.tweet_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        viewHolder.tweet_video.start();
                    }
                });

            }
        }
        else{
            viewHolder.tweet_video.setVisibility(View.GONE);
            viewHolder.tweet_image.setVisibility(View.GONE);
            viewHolder.tweet_play_video.setVisibility(View.GONE);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView user_profile, tweet_image;
        public VideoView tweet_video;
        public TextView user_name, user_screen_name, tweet_text, tweet_created_at;
        public TextView tweet_favorite_count, tweet_comment_count, tweet_retweet_count;
        public ImageButton tweet_favorited, tweet_retweeted, tweet_play_video;
        public LinearLayout tweet_linearLayout;
        //public RelativeLayout relativeLayout;

        public ViewHolder(View view){
            super(view);

            tweet_linearLayout = view.findViewById(R.id.tweet_linear_layout);

            user_profile = view.findViewById(R.id.user_profile);
            user_name = view.findViewById(R.id.user_username);
            user_screen_name = view.findViewById(R.id.user_screen_name);
            tweet_created_at = view.findViewById(R.id.tweet_created_at);
            tweet_text = view.findViewById(R.id.tweet_text);
            tweet_favorited = view.findViewById(R.id.tweet_favorited);
            tweet_favorite_count = view.findViewById(R.id.tweet_favourite_count);
            tweet_comment_count = view.findViewById(R.id.tweet_comment_count);
            tweet_retweeted = view.findViewById(R.id.tweet_retweeted);
            tweet_retweet_count = view.findViewById(R.id.tweet_retweet_count);

            tweet_image = view.findViewById(R.id.tweet_image);
            tweet_video = view.findViewById(R.id.tweet_video);
            tweet_play_video = view.findViewById(R.id.tweet_play_video);

            //relativeLayout = view.findViewById(R.id.image_video_relativeLayout);

            user_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ONCLICKLISTENER", "PROFILE");
                }
            });
            user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ONCLICKLISTENER", "USERNAME");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    /*
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private TweetAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TweetAdapter.ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
    */
}
