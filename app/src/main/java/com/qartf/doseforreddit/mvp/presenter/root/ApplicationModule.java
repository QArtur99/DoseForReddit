package com.qartf.doseforreddit.mvp.presenter.root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;
import com.qartf.doseforreddit.mvp.data.repository.DataRepository;
import com.qartf.doseforreddit.mvp.data.repository.RetrofitRepository;
import com.qartf.doseforreddit.mvp.data.repository.TokenRepository;
import com.qartf.doseforreddit.mvp.data.repository.Utility;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public DataRepository.Retrofit provideRetrofitRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI, DataRepository.Token token){
        return new RetrofitRepository(context, sharedPreferences, retrofitRedditAPI, token);
    }

    @Provides
    @Singleton
    public DataRepository.Token provideTokenRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
        return new TokenRepository(context, sharedPreferences, retrofitRedditAPI);
    }

    @Provides
    @Singleton
    public DataRepository.Utility provideUtilityRepository(Context context){
        return new Utility(context);
    }


}
