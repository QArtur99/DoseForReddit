package com.qartf.doseforreddit.mvp.root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;
import com.qartf.doseforreddit.mvp.data.repository.DataRepository;
import com.qartf.doseforreddit.mvp.data.repository.MemoryRepository;
import com.qartf.doseforreddit.mvp.data.repository.TokenRepository;

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
        return new MemoryRepository(context, sharedPreferences, retrofitRedditAPI, token);
    }

    @Provides
    @Singleton
    public DataRepository.Token provideTokenRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
        return new TokenRepository(context, sharedPreferences, retrofitRedditAPI);
    }

}
