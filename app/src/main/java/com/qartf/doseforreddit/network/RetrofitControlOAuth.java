package com.qartf.doseforreddit.network;

import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.utility.Constants.Auth;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ART_F on 2017-09-20.
 */

public class RetrofitControlOAuth {
    private MainActivity mainActivity;
    private Retrofit retrofit;
    private RetrofitRedditAPI feedAPI;


    public RetrofitControlOAuth(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        retrofit = new Retrofit.Builder()
                .baseUrl(Auth.BASE_URL_OAUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedAPI = retrofit.create(RetrofitRedditAPI.class);
    }

    public void getSubredditPosts(){

        String token = "bearer " + mainActivity.accessToken.getAccessToken();
        String subreddit = mainActivity.sharedPreferences.getString(mainActivity.prefPostSubreddit, mainActivity.getResources().getString(R.string.pref_post_subreddit_default));
        String subredditSortBy = mainActivity.sharedPreferences.getString(mainActivity.prefPostSortBy, mainActivity.getResources().getString(R.string.pref_post_sort_by_default));
        HashMap<String, String> args = new HashMap<>();

        Call<PostObjectParent> call = feedAPI.getSubredditPosts(token, subreddit, subredditSortBy, args);
        call.enqueue(new Callback<PostObjectParent>() {
            @Override
            public void onResponse(Call<PostObjectParent> call, Response<PostObjectParent> response) {
                mainActivity.listViewFragment.onLoadFinished(new Loader(mainActivity), (PostObjectParent) response.body());
            }

            @Override
            public void onFailure(Call<PostObjectParent> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );
                Toast.makeText(mainActivity, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
