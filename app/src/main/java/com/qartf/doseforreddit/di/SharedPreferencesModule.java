package com.qartf.doseforreddit.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.qartf.doseforreddit.ui.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.ui.sharedPreferences.SharedPreferencesPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferencesModule {

    @Provides
    public SharedPreferencesMVP.Presenter provideLoginActivityPresenter(Context context, SharedPreferences sharedPreferences){
        return new SharedPreferencesPresenter(context, sharedPreferences);
    }
}
