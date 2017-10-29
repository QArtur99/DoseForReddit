package com.qartf.doseforreddit.mvp.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.qartf.doseforreddit.mvp.data.model.Post;
import com.qartf.doseforreddit.mvp.presenter.utility.Navigation;

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


    public void startLoginActivity() {
        Navigation.startLoginActivity(activity);
    }

    public void startDetailActivity(Post post) {
        String postObjectString = new Gson().toJson(post);
        Navigation.startCommentsActivityMVP(activity, postObjectString);
    }



}
