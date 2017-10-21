package com.qartf.doseforreddit.mvp.root;

import android.content.Context;

import com.qartf.doseforreddit.mvp.BaseNavigationMainActivity;
import com.qartf.doseforreddit.mvp.MainActivity;
import com.qartf.doseforreddit.mvp.MainFragment;
import com.qartf.doseforreddit.mvp.retrofit.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class, com.qartf.doseforreddit.mvp.data.network.RetrofitModule.class})
public interface ApplicationComponent {


    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);
    void inject(BaseNavigationMainActivity baseNavigationMainActivity);

    Context provideContext();


}
