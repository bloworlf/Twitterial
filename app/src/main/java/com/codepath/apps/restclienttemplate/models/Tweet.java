package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel
public class Tweet{
    /*
    {
        "coordinates": {
        "coordinates": [
        121.0132101,
                14.5191613
      ],
        "type": "Point"
    },
        "favorited": false,
            "truncated": false,
            "created_at": "Mon Sep 03 08:08:02 +0000 2012",
            "id_str": "242534402280783873",
            "entities": {
        "urls": [

      ],
        "hashtags": [
        {
            "text": "twitter",
                "indices": [
            49,
                    57
          ]
        }
      ],
        "user_mentions": [
        {
            "name": "Jason Costa",
                "id_str": "14927800",
                "id": 14927800,
                "indices": [
            14,
                    25
          ],
            "screen_name": "jasoncosta"
        }
      ]
    },
        "in_reply_to_user_id_str": null,
            "contributors": null,
            "text": "Got the shirt @jasoncosta thanks man! Loving the #twitter bird on the shirt :-)",
            "retweet_count": 0,
            "in_reply_to_status_id_str": null,
            "id": 242534402280783873,
            "geo": {
        "coordinates": [
        14.5191613,
                121.0132101
      ],
        "type": "Point"
    },
        "retweeted": false,
            "in_reply_to_user_id": null,
            "place": null,
            "user": {
        "profile_sidebar_fill_color": "EFEFEF",
                "profile_sidebar_border_color": "EEEEEE",
                "profile_background_tile": true,
                "name": "Mikey",
                "profile_image_url": "http://a0.twimg.com/profile_images/1305509670/chatMikeTwitter_normal.png",
                "created_at": "Fri Jun 20 15:57:08 +0000 2008",
                "location": "Singapore",
                "follow_request_sent": false,
                "profile_link_color": "009999",
                "is_translator": false,
                "id_str": "15181205",
                "entities": {
            "url": {
                "urls": [
                {
                    "expanded_url": null,
                        "url": "http://about.me/michaelangelo",
                        "indices": [
                    0,
                            29
              ]
                }
          ]
            },
            "description": {
                "urls": [

          ]
            }
        },
        "default_profile": false,
                "contributors_enabled": false,
                "friends_count": 11,
                "url": "http://about.me/michaelangelo",
                "profile_image_url_https": "https://si0.twimg.com/profile_images/1305509670/chatMikeTwitter_normal.png",
                "utc_offset": 28800,
                "id": 15181205,
                "profile_use_background_image": true,
                "listed_count": 61,
                "profile_text_color": "333333",
                "lang": "en",
                "followers_count": 577,
                "protected": false,
                "notifications": null,
                "profile_background_image_url_https": "https://si0.twimg.com/images/themes/theme14/bg.gif",
                "profile_background_color": "131516",
                "verified": false,
                "geo_enabled": true,
                "time_zone": "Hong Kong",
                "description": "Android Applications Developer,  Studying Martial Arts, Plays MTG, Food and movie junkie",
                "default_profile_image": false,
                "profile_background_image_url": "http://a0.twimg.com/images/themes/theme14/bg.gif",
                "statuses_count": 11327,
                "friends_count": 138,
                "following": null,
                "show_all_inline_media": true,
                "screen_name": "mikedroid"
    },
        "in_reply_to_screen_name": null,
            "source": "Twitter for Android",
            "in_reply_to_status_id": null
    }
    */

    public String text;
    public long id;
    public String created_at;
    public boolean retweeted;
    public long retweet_count;
    public boolean favorited;
    public long favorites_count;
    public User user;
    public Media media;

    public Tweet(){

    }

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        try {
            tweet.text = jsonObject.getString("text");
            tweet.id = jsonObject.getLong("id");
            tweet.created_at = jsonObject.getString("created_at");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweet_count = jsonObject.getLong("retweet_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.favorites_count = jsonObject.getLong("favorite_count");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            if (jsonObject.has("extended_entities")){
                tweet.media = Media.fromJSON(jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));
            }
            else {
                tweet.media = null;
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }


        return  tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array){
        ArrayList<Tweet> results = new ArrayList<>();

        for(int i=0;i<array.length();i++){
            try {
                results.add(Tweet.fromJSON(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
