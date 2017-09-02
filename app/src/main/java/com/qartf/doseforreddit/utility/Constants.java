package com.qartf.doseforreddit.utility;

/**
 * Created by ART_F on 2017-08-30.
 */

public class Constants {


    //Access token
    public static final String LOGIN_TAG = "com.qartf.doseforreddit.activities.MainActivity";
    public static final String AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
            "&response_type=code&state=%s&redirect_uri=%s&" + "duration=permanent&scope=identity";
    public static final String CLIENT_ID = "XX9hXAmumdov9Q";
    public static final String REDIRECT_URI = "http://www.example.com/unused/redirect/uri";
    public static final String STATE = "MY_RANDOM_STRING_1";
    public static final String ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
    public static final String REVOKE_TOKEN_URL = "https://www.reddit.com/api/v1/revoke_token";

    //OAuth
    public static final String NO_NAME_GRAND_TYPE = "https://oauth.reddit.com/grants/installed_client";
    public static final String BASE_URL_OAUTH = "https://oauth.reddit.com";

    //Extra
    public class Id {
        public static final int GUEST_AUTH = 1;
        public static final int USER_AUTH = 2;
        public static final int ME = 3;
        public static final int POSTS = 4;
    }


}
