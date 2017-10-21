package com.qartf.doseforreddit.mvp.root;

import android.app.Application;

import com.qartf.doseforreddit.mvp.retrofit.RetrofitModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .retrofitModule(new RetrofitModule())
                .retrofitModule(new com.qartf.doseforreddit.mvp.data.network.RetrofitModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
