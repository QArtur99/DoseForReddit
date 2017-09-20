package com.qartf.doseforreddit.network;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ART_F on 2017-09-19.
 */

public class RetrofitControl {
    private MainActivity mainActivity;
    private Retrofit retrofit;
    private RetrofitRedditAPI feedAPI;

    public RetrofitControl(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Auth.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedAPI = retrofit.create(RetrofitRedditAPI.class);
    }


    public void refreshToken(){
        Call<AccessToken> call = feedAPI.refreshToken(basicToken(Constants.Auth.CLIENT_ID), "refresh_token", mainActivity.accessToken.getRefreshToken());
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                String token = response.body().getAccessToken();
                mainActivity.accessToken.setAccessToken(token);
                RetrofitControlOAuth retrofitControlOAuth = new RetrofitControlOAuth(mainActivity);
                retrofitControlOAuth.getSubredditPosts();
//                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.POSTS, null, mainActivity).forceLoad();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );
                Toast.makeText(mainActivity, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }
}
