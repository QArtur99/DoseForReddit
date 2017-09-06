package com.qartf.doseforreddit.network;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.model.AboutMe;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.utility.Constants.Comments;
import com.qartf.doseforreddit.utility.Constants.Id;
import com.qartf.doseforreddit.utility.Constants.Search;
import com.qartf.doseforreddit.utility.Constants.Subscribe;
import com.qartf.doseforreddit.utility.Constants.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class DataLoader extends AsyncTaskLoader<Object> {

    private String accessTokenString = "";
    private Object object;
    private Bundle args;
    private int loaderId;

    public DataLoader(Context context, int loaderId) {
        super(context);
        this.loaderId = loaderId;
    }

    public DataLoader(Context context, String accessTokenString, int loaderId) {
        super(context);
        this.loaderId = loaderId;
        this.accessTokenString = accessTokenString;
    }

    public DataLoader(Context context, String accessTokenString, Bundle args, int loaderId) {
        super(context);
        this.loaderId = loaderId;
        this.args = args;
        this.accessTokenString = accessTokenString;
    }


    @Override
    public Object loadInBackground() {
        try {
            switch (loaderId) {
                case Id.GUEST_AUTH:
                    object = Access.getGuestAccess();
                    break;
                case Id.USER_AUTH:
                    object = Access.getAccess(accessTokenString);
                    break;
                case Id.USER_REFRESH:
                    object = Access.getRefreshToken(accessTokenString);
                    break;
                case Id.ME:
                    object = getMe(accessTokenString);
                    break;
                case Id.POSTS:
                    object = getPosts(accessTokenString, args);
                    break;
                case Id.COMMENTS:
                    object = getComments(accessTokenString, args);
                    break;
                case Id.SEARCH_POSTS:
                    object = getQueryPosts(accessTokenString, args);
                    break;
                case Id.SEARCH_SUBREDDITS:
                    object = getSubreddits(accessTokenString, args);
                    break;
                case Id.SUBREDDIT_SUBSCRIBE:
                    object = postSubscribe(accessTokenString, args);
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private static class Access {
        private static AccessToken getAccess(String code) throws IOException, JSONException {
            String jsonString = PostAuthRedditAPI.getAccessToken(code);
            return new Gson().fromJson(jsonString, AccessToken.class);
        }

        private static String getRefreshToken(String refreshToken) throws IOException, JSONException {
            String jsonString = PostAuthRedditAPI.refreshToken(refreshToken);
            AccessToken accessToken = new Gson().fromJson(jsonString, AccessToken.class);
            return accessToken.getAccessToken();
        }

        private void getLogOut(String accessTokenString) throws IOException, JSONException {
            String jsonString = PostAuthRedditAPI.revokeTokenAccess(accessTokenString);
//        JSONObject jsonObject = new JSONObject(jsonString);
//        if (jsonObject.has("access_token")) {
//            accessToken.setAccessToken(jsonObject.getString("access_token"));
//        }
        }

        private static String getGuestAccess() throws IOException, JSONException {
            String jsonString = PostAuthRedditAPI.getGuestAccess();
            AccessToken accessToken = new Gson().fromJson(jsonString, AccessToken.class);
            return accessToken.getAccessToken();
        }
    }

    private SubredditParent getSubreddits(String accessTokenString, Bundle args) throws IOException, JSONException {
        String queryString = args.getString(Search.QUERY);
        String jsonString = GetAuthRedditAPI.getSubreddits(accessTokenString, queryString);
        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
        SubredditParent subredditParent = new Gson().fromJson(jsonObject.toString(), SubredditParent.class);
        return subredditParent;
    }

    private PostObjectParent getQueryPosts(String accessTokenString, Bundle args) throws IOException, JSONException {
        String subreddit = args.getString(Post.SUBREDDIT);
        String queryString = args.getString(Search.QUERY);
        String jsonString = GetAuthRedditAPI.getQueryPosts(accessTokenString, subreddit, queryString);
        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
        PostObjectParent postObjectParent = new Gson().fromJson(jsonObject.toString(), PostObjectParent.class);
        return postObjectParent;
    }

    private String postSubscribe(String accessTokenString, Bundle args) throws IOException, JSONException {
        String subscribe = args.getString(Subscribe.SUBSCRIBE);
        String fullname = args.getString(Subscribe.FULLNAME);
        String jsonString = PostAuthRedditAPI.postSubscribe(accessTokenString, subscribe, fullname);
        return jsonString;
    }

    private String getMe(String accessTokenString) throws IOException, JSONException {
        String jsonString = GetAuthRedditAPI.getMe(accessTokenString);
        AboutMe aboutMe = new Gson().fromJson(jsonString, AboutMe.class);
        return aboutMe.name;

    }

    private List<Comment> getComments(String accessTokenString, Bundle args) throws IOException, JSONException {
        String subreddit = args.getString(Comments.SUBREDDIT);
        String id = args.getString(Comments.ID);
        String sort = args.getString(Comments.SORT);

        String jsonString = GetAuthRedditAPI.getComments(accessTokenString, subreddit, id, sort);
        JSONArray jsonArray = new JSONArray(jsonString).getJSONObject(1).getJSONObject("data").getJSONArray("children");
        List<Comment> commentList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Comment>>() {}.getType());

        return commentList;

    }

    private PostObjectParent getPosts(String accessTokenString, Bundle args) throws IOException, JSONException {
        String subreddit = args.getString(Post.SUBREDDIT);
        String subredditSortBy = args.getString(Post.SORT_BY);

        String jsonString = GetAuthRedditAPI.getPopular(accessTokenString, subreddit, subredditSortBy);
        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
        PostObjectParent postObjectParent = new Gson().fromJson(jsonObject.toString(), PostObjectParent.class);
        return postObjectParent;
    }

}
