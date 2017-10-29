package com.qartf.doseforreddit.presenter.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @Provides
    public SharedPreferencesMVP.Presenter provideLoginActivityPresenter(Context context, SharedPreferences sharedPreferences){
        return new SharedPreferencesPresenter(context, sharedPreferences);
    }
}
