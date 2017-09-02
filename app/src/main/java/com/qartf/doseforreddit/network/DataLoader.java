package com.qartf.doseforreddit.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.qartf.doseforreddit.model.AboutMe;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.model.Test;
import com.qartf.doseforreddit.utility.Constants.Id;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DataLoader extends AsyncTaskLoader<Object> {

    private PostObjectParent postObjectParent;
    private List<PostObject> postObjectList;
    private String accessTokenString = "";
    private Object object;
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


    @Override
    public Object loadInBackground() {
        postObjectList = new ArrayList<>();
        try {
            switch (loaderId) {
                case Id.GUEST_AUTH:
                    object = getGuestAccess();
                    break;
                case Id.USER_AUTH:
                    object = getAccess(accessTokenString);
                    break;
                case Id.ME:
                    getMe(accessTokenString);
                    break;
                case Id.POSTS:
                    getPosts(accessTokenString);
                    object = postObjectParent;
                    break;


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    private AccessToken getAccess(String code) throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.getAccessToken(code);
        return new Gson().fromJson(jsonString, AccessToken.class);
    }


    private String getRefreshToken(String refreshToken) throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.refreshToken(refreshToken);
        JSONObject jsonObject = new JSONObject(jsonString);
        String accessToken = "";
        if (jsonObject.has("access_token")) {
            accessToken = jsonObject.getString("access_token");
        }
        return accessToken;
    }

    private void getLogOut(String accessTokenString) throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.revokeTokenAccess(accessTokenString);
//        JSONObject jsonObject = new JSONObject(jsonString);
//        if (jsonObject.has("access_token")) {
//            accessToken.setAccessToken(jsonObject.getString("access_token"));
//        }
    }

    private String getGuestAccess() throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.appOnly();
        JSONObject jsonObjectX = new JSONObject(jsonString);
        String accessToken = "";
        if (jsonObjectX.has("access_token")) {
            accessToken = jsonObjectX.getString("access_token");
        }
        return accessToken;
    }

    private void getMe(String accessTokenString) throws IOException, JSONException {
        String jsonString = GetAuthRedditAPI.getMe(accessTokenString);

        AboutMe aboutMe = new Gson().fromJson(jsonString, AboutMe.class);

        String xx  = aboutMe.name;
        String cc = xx;
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject x = jsonObject;
    }


    private void getPosts(String accessTokenString) throws IOException, JSONException {

        String jsonString = GetAuthRedditAPI.getPopular(accessTokenString);

        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");

        Test test = new Gson().fromJson(jsonObject.toString(), Test.class);
        Test xxx = test;


        JSONArray jsonArray = jsonObject.getJSONArray("children");
        int bookAmount = jsonArray.length();
        for (int i = 0; bookAmount > i; i++) {
            JSONObject jsonBookData = jsonArray.getJSONObject(i);

            postObjectList.add(new PostObject(jsonBookData));
        }
        postObjectParent = new PostObjectParent(postObjectList, jsonObject);
    }

}
