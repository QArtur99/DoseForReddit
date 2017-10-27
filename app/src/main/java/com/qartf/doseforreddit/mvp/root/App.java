package com.qartf.doseforreddit.mvp.root;

import android.app.Application;

import com.qartf.doseforreddit.mvp.data.network.RetrofitModule;
import com.qartf.doseforreddit.mvp.post.PostModule;
import com.qartf.doseforreddit.mvp.sharedPreferences.SharedPreferencesModule;
import com.qartf.doseforreddit.mvp.subreddit.SubredditModule;
import com.qartf.doseforreddit.mvp.token.TokenModule;

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
                .retrofitModule(new RetrofitModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
