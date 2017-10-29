package com.qartf.doseforreddit.presenter.root;

import android.app.Application;

import com.qartf.doseforreddit.data.network.RetrofitModule;
import com.qartf.doseforreddit.presenter.comment.CommentModule;
import com.qartf.doseforreddit.presenter.post.PostModule;
import com.qartf.doseforreddit.presenter.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.presenter.subreddit.SubredditModule;
import com.qartf.doseforreddit.presenter.token.TokenModule;

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
