package com.qartf.doseforreddit;

import android.app.Application;

import com.qartf.doseforreddit.data.network.RetrofitModule;
import com.qartf.doseforreddit.di.ApplicationComponent;
import com.qartf.doseforreddit.di.ApplicationModule;
import com.qartf.doseforreddit.di.CommentModule;
import com.qartf.doseforreddit.di.DaggerApplicationComponent;
import com.qartf.doseforreddit.di.PostModule;
import com.qartf.doseforreddit.di.SharedPreferencesModule;
import com.qartf.doseforreddit.di.SubredditModule;
import com.qartf.doseforreddit.di.TokenModule;

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
