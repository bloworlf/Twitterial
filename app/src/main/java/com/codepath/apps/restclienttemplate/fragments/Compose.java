package com.codepath.apps.restclienttemplate.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Compose extends DialogFragment {

    TwitterClient twitterClient;
    User user;
    private ImageButton compose_cancel;
    private Button compose_tweet;
    private EditText compose_text;
    private TextView compose_screen_name, compose_username, compose_char_left;
    private ImageView compose_profile;

    public static Compose newInstance(){
        return new Compose();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.compose_frag, null);

        twitterClient = new TwitterClient(getContext());

        getCurrentUser();

        compose_cancel = view.findViewById(R.id.compose_cancel);
        compose_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        compose_text = view.findViewById(R.id.compose_text);

        compose_screen_name = view.findViewById(R.id.compose_screen_name);
        //compose_screen_name.setText(user.screen_name);
        compose_username = view.findViewById(R.id.compose_username);
        //compose_username.setText(user.name);
        compose_profile = view.findViewById(R.id.compose_profile);
        //Glide.with(getContext())
        //        .load(Uri.parse(user.profile_image_url))
        //        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(12, 4)))
        //        .into(compose_profile);
        compose_char_left = view.findViewById(R.id.compose_char_left);
        //compose_char_left.setText(getCharLeft(compose_text.getText().toString().trim()));

        compose_tweet = view.findViewById(R.id.compose_tweet);
        compose_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sentTweet(compose_text.getText().toString().trim());
            }
        });


        builder.setView(view);
        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void getCurrentUser() {
        twitterClient.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);

                compose_screen_name.setText(user.screen_name);
                compose_username.setText(user.name);
                Glide.with(getContext())
                        .load(Uri.parse(user.profile_image_url))
                        .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(12, 4)))
                        .into(compose_profile);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("FAILURE", errorResponse.toString());
            }
        });
    }

    private int getCharLeft(String text){
        return 140 - text.length();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}
