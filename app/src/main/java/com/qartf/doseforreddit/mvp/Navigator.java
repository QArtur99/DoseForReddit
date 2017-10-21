package com.qartf.doseforreddit.mvp;

import android.app.Activity;

import com.google.gson.Gson;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.utility.Navigation;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

    private Activity activity;

    @Inject
    public Navigator() {
    }

    public  void setActivity(Activity activity){
        this.activity = activity;
    }

    public void startDetailActivity(Post post, AccessToken accessToken) {
        String postObjectString = new Gson().toJson(post);
        String tokenString = new Gson().toJson(accessToken);
        Navigation.startCommentsActivity(activity, postObjectString, tokenString);
    }



}
