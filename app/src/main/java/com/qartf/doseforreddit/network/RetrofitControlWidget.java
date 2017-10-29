package com.qartf.doseforreddit.network;

import android.util.Base64;

import com.qartf.doseforreddit.mvp.data.model.AccessToken;
import com.qartf.doseforreddit.mvp.data.model.PostParent;
import com.qartf.doseforreddit.mvp.presenter.utility.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitControlWidget {
    private RetrofitRedditAPI feedAPI;

    public RetrofitControlWidget(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Auth.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedAPI = retrofit.create(RetrofitRedditAPI.class);
    }

    public AccessToken refreshToken(String refreshToken) {
        Call<AccessToken> call = feedAPI.refreshToken(basicToken(Constants.Auth.CLIENT_ID), "refresh_token", refreshToken);
        AccessToken accessToken = null;
        try {
            accessToken = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }


    public AccessToken guestToken() {
        String uuid = UUID.randomUUID().toString();
        Call<AccessToken> call = feedAPI.guestToken(basicToken(Constants.Auth.CLIENT_ID), Constants.Auth.NO_NAME_GRAND_TYPE, uuid);
        AccessToken accessToken = null;
        try {
            accessToken = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public PostParent getSubredditPosts(String accessTokenString) {
        HashMap<String, String> args = new HashMap<>();
        Call<PostParent> call = feedAPI.getSubredditPosts(getBearerToken(accessTokenString), "popular", "hot", args);
        PostParent postParent = null;
        try {
            postParent = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postParent;
    }

    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }

    private String getBearerToken(String accessTokenString){
        String token = "bearer " + accessTokenString;
        return token;
    }

}
