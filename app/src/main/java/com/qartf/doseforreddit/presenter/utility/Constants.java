package com.qartf.doseforreddit.presenter.utility;


public class Constants {


    //Access token
    public static final String LOGIN_TAG = "com.qartf.doseforreddit.activities.MainActivity";
    public static final String ACCESS_DECLINED = "accessDeclined";

    public class Auth {
        public static final String AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
                "&response_type=code&state=%s&redirect_uri=%s&" + "duration=permanent&scope=%s";
        public static final String CLIENT_ID = "XX9hXAmumdov9Q";
        public static final String REDIRECT_URI = "http://www.example.com/unused/redirect/uri";
        public static final String STATE = "MY_RANDOM_STRING_1";
        public static final String SCOPE = "identity edit flair mysubreddits read report submit subscribe vote";
        public static final String ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
        public static final String REVOKE_TOKEN_URL = "https://www.reddit.com/api/v1/revoke_token";

        public static final String NO_NAME_GRAND_TYPE = "https://oauth.reddit.com/grants/installed_client";
        public static final String BASE_URL = "https://www.reddit.com";
        public static final String BASE_URL_OAUTH = "https://oauth.reddit.com";
    }

    public class Id {
        public static final int SEARCH_POSTS = 12;
        public static final int SEARCH_SUBREDDITS = 13;
        public static final int SUBMIT = 14;
    }

    public class Pref {
        public static final String prefSecondFragment = "secondFragment";
        public static final String prefDetailFragment = "detailFragment";
        public static final String prefSubmitFragment = "submitFragment";
    }

    public class Utility{
        public static final String POST_OBJECT_STRING = "postObjectString";
        public static final String ANONYMOUS = "Anonymous";
    }


    public class Subscribe{
        public static final String SUB = "sub";
        public static final String UN_SUB = "unsub";
    }




}
