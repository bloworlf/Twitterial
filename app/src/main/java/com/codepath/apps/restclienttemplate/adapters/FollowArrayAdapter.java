package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FollowArrayAdapter extends ArrayAdapter<User> implements View.OnClickListener {

    Context context;
    User user1 = new User();

    private static class ViewHolder{
        ImageView follow_profile;
        ImageButton follow_followed;
        TextView follow_user_name, follow_user_screen_name;
        LinearLayout follow_linearlayout;

    }

    public FollowArrayAdapter(Context context, ArrayList<User> users){
        super(context, android.R.layout.simple_dropdown_item_1line, users);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        user1 = user;

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.follow, parent, false);

            viewHolder.follow_profile = convertView.findViewById(R.id.follow_profile);
            viewHolder.follow_followed = convertView.findViewById(R.id.follow_followed);
            viewHolder.follow_user_name = convertView.findViewById(R.id.follow_user_name);
            viewHolder.follow_user_screen_name = convertView.findViewById(R.id.follow_user_screen_name);
            viewHolder.follow_linearlayout = convertView.findViewById(R.id.follow_linearlayout);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.follow_linearlayout.setBackgroundColor(Color.parseColor("#"+user.profile_background_color));
        viewHolder.follow_user_name.setText(user.name);
        viewHolder.follow_user_screen_name.setText("@"+user.screen_name);
        Glide.with(context)
                .load(Uri.parse(user.profile_image_url))
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(48, 4)))
                .into(viewHolder.follow_profile);

        //viewHolder.follow_profile.setOnClickListener(this);
        //viewHolder.follow_user_screen_name.setOnClickListener(this);
        //viewHolder.follow_user_name.setOnClickListener(this);
        //viewHolder.follow_followed.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.follow_profile:
                Intent intent = new Intent(context, ViewUser.class);
                intent.putExtra("user", Parcels.wrap(user1));
                context.startActivity(intent);
                break;
            case R.id.follow_user_name:
                Intent intent1 = new Intent(context, ViewUser.class);
                intent1.putExtra("user", Parcels.wrap(user1));
                context.startActivity(intent1);
                break;
            case R.id.follow_user_screen_name:
                Intent intent2 = new Intent(context, ViewUser.class);
                intent2.putExtra("user", Parcels.wrap(user1));
                context.startActivity(intent2);
                break;
            case R.id.follow_followed:
                //
                break;
        }
    }
}
