package com.qartf.doseforreddit.mvp.root;

import com.qartf.doseforreddit.mvp.BaseNavigationMainActivity;
import com.qartf.doseforreddit.mvp.MainActivity;
import com.qartf.doseforreddit.mvp.MainFragment;
import com.qartf.doseforreddit.mvp.retrofit.RetrofitModule;
import com.qartf.doseforreddit.mvp.sharedPreferences.SharedPreferencesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class,
        com.qartf.doseforreddit.mvp.data.network.RetrofitModule.class, SharedPreferencesModule.class})
public interface ApplicationComponent {


    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);
    void inject(BaseNavigationMainActivity baseNavigationMainActivity);


}
