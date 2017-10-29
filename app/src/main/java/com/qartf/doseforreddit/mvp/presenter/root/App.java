package com.qartf.doseforreddit.mvp.presenter.root;

import android.app.Application;

import com.qartf.doseforreddit.mvp.data.network.RetrofitModule;
import com.qartf.doseforreddit.mvp.presenter.comment.CommentModule;
import com.qartf.doseforreddit.mvp.presenter.post.PostModule;
import com.qartf.doseforreddit.mvp.presenter.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.mvp.presenter.subreddit.SubredditModule;
import com.qartf.doseforreddit.mvp.presenter.token.TokenModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .sharedPreferencesModule(new SharedPreferencesModule())
                .tokenModule(new TokenModule())
                .postModule(new PostModule())
                .subredditModule(new SubredditModule())
                .commentModule(new CommentModule())
                .retrofitModule(new RetrofitModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
