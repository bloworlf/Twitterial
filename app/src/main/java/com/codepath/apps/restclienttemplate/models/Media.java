package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Media implements Serializable {
    /*
    {
        "created_at": "Mon Jul 11 21:50:25 +0000 2016",
            "id": 752621088723566592,
            "id_str": "752621088723566592",
            "text": "Darned Pidgey is after my beer. (link)",
            "truncated": false,
            "entities": {
        "hashtags": [

    ],
        "symbols": [

    ],
        "user_mentions": [

    ],
        "urls": [
        {
            "url": "(link)",
                "expanded_url": "http://pipr.co/29zNseX",
                "display_url": "pipr.co/29zNseX",
                "indices": [
            32,
                    55
        ]
        }
    ],
        "media": [
        {
            "id": 752621086345465856,
                "id_str": "752621086345465856",
                "indices": [
            56,
                    79
        ],
            "media_url": "http://pbs.twimg.com/media/CnHY4_YXEAAYm9J.jpg",
                "media_url_https": "https://pbs.twimg.com/media/CnHY4_YXEAAYm9J.jpg",
                "url": "(link)",
                "display_url": "pic.twitter.com/S1pIUQisNW",
                "expanded_url": "http://twitter.com/andypiper/status/752621088723566592/photo/1",
                "type": "photo",
                "sizes": {
            "medium": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            },
            "thumb": {
                "w": 150,
                        "h": 150,
                        "resize": "crop"
            },
            "large": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            },
            "small": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            }
        }
        }
    ]
    },
        "extended_entities": {
        "media": [
        {
            "id": 752621086345465856,
                "id_str": "752621086345465856",
                "indices": [
            56,
                    79
        ],
            "media_url": "http://pbs.twimg.com/media/CnHY4_YXEAAYm9J.jpg",
                "media_url_https": "https://pbs.twimg.com/media/CnHY4_YXEAAYm9J.jpg",
                "url": "(link)",
                "display_url": "pic.twitter.com/S1pIUQisNW",
                "expanded_url": "http://twitter.com/andypiper/status/752621088723566592/photo/1",
                "type": "photo",
                "sizes": {
            "medium": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            },
            "thumb": {
                "w": 150,
                        "h": 150,
                        "resize": "crop"
            },
            "large": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            },
            "small": {
                "w": 582,
                        "h": 582,
                        "resize": "fit"
            }
        }
        }
    ]
    },
        "source": "<a href=\"http://ifttt.com\" rel=\"nofollow\">IFTTT</a>",
            "in_reply_to_status_id": null,
            "in_reply_to_status_id_str": null,
            "in_reply_to_user_id": null,
            "in_reply_to_user_id_str": null,
            "in_reply_to_screen_name": null,
            "user": {
        "id": 786491,
                "id_str": "786491",
                "name": "Pipes",
                "screen_name": "andypiper",
                "location": "Kingston upon Thames, London",
                "description": "I'm on the @TwitterDev team, supporting and listening to developers working on the Twitter platform. Code, community, and respect. #HeForShe",
                "url": "(link)",
                "entities": {
            "url": {
                "urls": [
                {
                    "url": "(link)",
                        "expanded_url": "http://about.me/andypiper",
                        "display_url": "about.me/andypiper",
                        "indices": [
                    0,
                            23
            ]
                }
        ]
            },
            "description": {
                "urls": [

        ]
            }
        },
        "protected": false,
                "followers_count": 13542,
                "friends_count": 3898,
                "listed_count": 781,
                "created_at": "Wed Feb 21 15:14:48 +0000 2007",
                "favourites_count": 65599,
                "utc_offset": 3600,
                "time_zone": "London",
                "geo_enabled": true,
                "verified": false,
                "statuses_count": 89932,
                "lang": "en",
                "contributors_enabled": false,
                "is_translator": false,
                "is_translation_enabled": false,
                "profile_background_color": "ACDEEE",
                "profile_background_image_url": "http://abs.twimg.com/images/themes/theme18/bg.gif",
                "profile_background_image_url_https": "https://abs.twimg.com/images/themes/theme18/bg.gif",
                "profile_background_tile": false,
                "profile_image_url": "http://pbs.twimg.com/profile_images/753281917944893440/63g61GqD_normal.jpg",
                "profile_image_url_https": "https://pbs.twimg.com/profile_images/753281917944893440/63g61GqD_normal.jpg",
                "profile_banner_url": "https://pbs.twimg.com/profile_banners/786491/1468424285",
                "profile_link_color": "6EA8E6",
                "profile_sidebar_border_color": "FFFFFF",
                "profile_sidebar_fill_color": "F6F6F6",
                "profile_text_color": "333333",
                "profile_use_background_image": true,
                "has_extended_profile": true,
                "default_profile": false,
                "default_profile_image": false,
                "following": false,
                "follow_request_sent": false,
                "notifications": false
    },
        "geo": null,
            "coordinates": null,
            "place": null,
            "contributors": null,
            "is_quote_status": false,
            "retweet_count": 1,
            "favorite_count": 11,
            "favorited": false,
            "retweeted": false,
            "possibly_sensitive": false,
            "possibly_sensitive_appealable": false,
            "lang": "en"
    }
    */

    public String type;
    public String media_url;
    public String display_url;
    public String expanded_url;
    public int width;
    public int height;

    public static Media fromJSON(JSONObject jsonObject){
        Media media = new Media();

        try {
            media.type = jsonObject.getString("type");
            media.media_url = jsonObject.getString("media_url");
            media.display_url = jsonObject.getString("display_url");
            media.expanded_url = jsonObject.getString("expanded_url");
            media.width = jsonObject.getJSONObject("sizes").getJSONObject("medium").getInt("w");
            media.height = jsonObject.getJSONObject("sizes").getJSONObject("medium").getInt("h");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return media;
    }
}
