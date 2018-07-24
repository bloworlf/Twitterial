package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
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
            "favourites_count": 2151,
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

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screen_name = jsonObject.getString("screen_name");
            user.profile_image_url = jsonObject.getString("profile_image_url");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

}
