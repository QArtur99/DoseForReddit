package com.qartf.doseforreddit.utility;

/**
 * Created by ART_F on 2017-08-30.
 */

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
        public static final String SCOPE = "identity edit flair mysubreddits read report subscribe vote";
        public static final String ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token";
        public static final String REVOKE_TOKEN_URL = "https://www.reddit.com/api/v1/revoke_token";

        public static final String NO_NAME_GRAND_TYPE = "https://oauth.reddit.com/grants/installed_client";
        public static final String BASE_URL = "https://www.reddit.com";
        public static final String BASE_URL_OAUTH = "https://oauth.reddit.com";
    }

    //Loaders id
    public class Id {
        public static final int LOAD_USERS = 5;
        public static final int GUEST_AUTH = 6;
        public static final int USER_AUTH = 7;
        public static final int USER_REFRESH = 8;
        public static final int ME = 9;
        public static final int POSTS = 10;
        public static final int COMMENTS = 11;
        public static final int SEARCH_POSTS = 12;
        public static final int SEARCH_SUBREDDITS = 13;
        public static final int SUBREDDIT_SUBSCRIBE = 14;
        public static final int VOTE = 15;

    }

    public class Utility{
        public static final String POST_OBJECT_STRING = "postObjectString";
        public static final String ANONYMOUS = "Anonymous";
    }


    public class Post{
        public static final String SUBREDDIT = "subreddit";
        public static final String SORT_BY = "sort_by";
    }

    public class Comments {
        public static final String SUBREDDIT = "subreddit";
        public static final String ID = "id";
        public static final String SORT = "sort";
    }

    public class Vote {
        public static final String DIR = "dir";
        public static final String FULLNAME = "fullname";
    }

    public class Search {
        public static final String QUERY = "query";
        public static final String POST = "post";
        public static final String SUBREDDIT = "subreddit";
    }

    public class Subscribe{
        public static final String SUBSCRIBE = "subscribe";
        public static final String SUB = "sub";
        public static final String UN_SUB = "unsub";
        public static final String FULLNAME = "fullname";
    }




}
