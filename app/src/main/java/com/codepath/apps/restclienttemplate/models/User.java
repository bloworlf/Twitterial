package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class User{
    /*
    {
        "user": {
            "id": 2244994945,
            "id_str": "2244994945",
            "name": "TwitterDev",
            "screen_name": "TwitterDev",
            "location": "Internet",
            "url": "https://dev.twitter.com/",
            "description": "Your source for Twitter news",
            "verified": true,
            "followers_count": 477684,
            "friends_count": 1524,
            "listed_count": 1184,
            "friends_count": 2151,
            "statuses_count": 3121,
            "created_at": "Sat Dec 14 04:35:55 +0000 2013",
            "utc_offset": null,
            "time_zone": null,
            "geo_enabled": true,
            "lang": "en",
            "profile_image_url_https": "https://pbs.twimg.com/"
        }
    }
    */

    public String name;
    public long uid;
    public String screen_name;
    public String profile_image_url;
    public String description;
    public int followers_count;
    public boolean follow_request_sent;
    public boolean profile_use_background_image;
    public String profile_background_image_url;
    public String profile_background_color;
    public int statuses_count;
    public int friends_count;
    public int favourites_count;

    public User(){

    }

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screen_name = jsonObject.getString("screen_name");
            user.profile_image_url = jsonObject.getString("profile_image_url");
            user.description = jsonObject.getString("description");
            user.followers_count = jsonObject.getInt("followers_count");
            user.follow_request_sent = jsonObject.getBoolean("follow_request_sent");
            user.profile_use_background_image = jsonObject.getBoolean("profile_use_background_image");
            user.profile_background_image_url = jsonObject.getString("profile_background_image_url");
            user.profile_background_color = jsonObject.getString("profile_background_color");
            user.statuses_count = jsonObject.getInt("statuses_count");
            user.friends_count = jsonObject.getInt("friends_count");
            user.favourites_count = jsonObject.getInt("favourites_count");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<User> fromJSONArray(JSONArray array){
        ArrayList<User> results = new ArrayList<>();

        for(int i=0;i<array.length();i++){
            try {
                results.add(User.fromJSON(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
