package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.ViewUser;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;

    // Store a member variable for the users
    private List<User> users;
    private User user;

    // Pass in the article array into the constructor
    public FollowAdapter(List<User> users){
        this.users = users;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.follow_profile:
                Intent intent = new Intent(context, ViewUser.class);
                intent.putExtra("user", Parcels.wrap(user));
                context.startActivity(intent);
                break;
            case R.id.follow_user_name:
                Intent intent1 = new Intent(context, ViewUser.class);
                intent1.putExtra("user", Parcels.wrap(user));
                context.startActivity(intent1);
                break;
            case R.id.follow_user_screen_name:
                Intent intent2 = new Intent(context, ViewUser.class);
                intent2.putExtra("user", Parcels.wrap(user));
                context.startActivity(intent2);
                break;
            case R.id.follow_followed:
                //
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView follow_profile;
        ImageButton follow_followed;
        TextView follow_user_name, follow_user_screen_name;
        LinearLayout follow_linearlayout;

        public ViewHolder(View view){

            super(view);

            follow_profile = view.findViewById(R.id.follow_profile);
            follow_followed = view.findViewById(R.id.follow_followed);
            follow_user_name = view.findViewById(R.id.follow_user_name);
            follow_user_screen_name = view.findViewById(R.id.follow_user_screen_name);
            follow_linearlayout = view.findViewById(R.id.follow_linearlayout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.follow, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        user = users.get(i);

        viewHolder.follow_linearlayout.setBackgroundColor(Color.parseColor("#"+user.profile_background_color));
        viewHolder.follow_user_name.setText(user.name);
        viewHolder.follow_user_screen_name.setText("@"+user.screen_name);
        Glide.with(context)
                .load(Uri.parse(user.profile_image_url))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(48, 4)))
                .into(viewHolder.follow_profile);

        viewHolder.follow_profile.setOnClickListener(this);
        viewHolder.follow_user_screen_name.setOnClickListener(this);
        viewHolder.follow_user_name.setOnClickListener(this);
        viewHolder.follow_followed.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
