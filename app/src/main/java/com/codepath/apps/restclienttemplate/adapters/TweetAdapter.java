package com.codepath.apps.restclienttemplate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.activities.TweetDetails;
import com.codepath.apps.restclienttemplate.fragments.Timeline;
import com.codepath.apps.restclienttemplate.models.Media;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private Context context;
    private List<Tweet> tweets;
    private TwitterClient twitterClient;
    boolean deleted;

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

        twitterClient = TwitterApp.getTwitterClient(context);

        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final Tweet tweet = tweets.get(position);

        viewHolder.tweet_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TweetDetails.class);
                //intent.putExtra("id", String.valueOf(tweet.id));
                intent.putExtra("tweet", tweet);
                context.startActivity(intent);
            }
        });
        viewHolder.tweet_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(tweet, position);

                return true;
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

        viewHolder.tweet_retweeted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.retweeted){
                    unRetweet(tweet.id);
                }
                else {
                    reTweet(tweet.id);
                }
                updateTweetView(tweet.id, position);
                Timeline.tweetAdapter.notifyItemChanged(position);

            }
        });

        viewHolder.tweet_favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tweet.favorited){
                    unlikeTweet(tweet.id);
                }
                else {
                    likeTweet(tweet.id);
                }
                updateTweetView(tweet.id, position);
                Timeline.tweetAdapter.notifyItemChanged(position);
            }
        });

    }

    private void updateTweetView(long id, final int position) {
        twitterClient.getTweetDetail(new JsonHttpResponseHandler(){
                                         @Override
                                         public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                             Tweet tweet = Tweet.fromJSON(response);
                                             tweets.set(position, tweet);
                                             //Timeline.tweetAdapter.notifyItemChanged(position);
                                         }

                                         @Override
                                         public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                             //Timeline.tweetAdapter.notifyItemChanged(position);
                                         }
                                     },
                id);
    }

    private void showDialog(final Tweet tweet, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deleting a status");
        builder.setMessage("Are you sure you want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(deleteTweet(tweet.id)){
                    Timeline.tweets.remove(position);
                    Timeline.tweetAdapter.notifyItemRemoved(position);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        builder.show();
    }

    private boolean deleteTweet(long id) {
        twitterClient.deleteTweet(new JsonHttpResponseHandler(){
                                      @Override
                                      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                          deleted = true;
                                      }

                                      @Override
                                      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                          deleted = false;
                                      }
                                  },
                id);
        return deleted;
    }

    private void likeTweet(long id) {
        twitterClient.likeTweet(new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Toast.makeText(context, "You\'ve liked.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        Toast.makeText(context, "Oops! Didn't like", Toast.LENGTH_SHORT).show();
                                    }
                                },
                id);
    }

    private void unlikeTweet(long id){
        twitterClient.unlikeTweet(new JsonHttpResponseHandler(){
                                      @Override
                                      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                          Toast.makeText(context, "You\'ve unliked.", Toast.LENGTH_SHORT).show();
                                      }

                                      @Override
                                      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                          Toast.makeText(context, "Oops! Didn't unlike", Toast.LENGTH_SHORT).show();
                                      }
                                  },
                id);
    }

    private void unRetweet(long id) {
        twitterClient.unRetweet(new JsonHttpResponseHandler(){
                                  @Override
                                  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                      Toast.makeText(context, "You\'ve unretweeted.", Toast.LENGTH_SHORT).show();
                                  }

                                  @Override
                                  public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                      Toast.makeText(context, "Oops! Didn't unretweet", Toast.LENGTH_SHORT).show();
                                  }
                              },
                id);
    }

    private void reTweet(long id) {
        twitterClient.reTweet(new JsonHttpResponseHandler(){
                                  @Override
                                  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                      Toast.makeText(context, "You\'ve retweeted.", Toast.LENGTH_SHORT).show();
                                  }

                                  @Override
                                  public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                      Toast.makeText(context, "Oops! Didn't retweet", Toast.LENGTH_SHORT).show();
                                  }
                              },
                id);
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
