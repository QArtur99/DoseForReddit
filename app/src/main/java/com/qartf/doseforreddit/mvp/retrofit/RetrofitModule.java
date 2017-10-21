package com.qartf.doseforreddit.mvp.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;
import com.qartf.doseforreddit.mvp.data.repository.MemoryRepository;
import com.qartf.doseforreddit.mvp.data.repository.RetrofitRepository;

import dagger.Module;
import dagger.Provides;


@Module
public class RetrofitModule {

    @Provides
    public RetrofitMVP.Presenter provideLoginActivityPresenter(RetrofitMVP.Model model){
        return new RetrofitPresenter(model);
    }

    @Provides
    public RetrofitMVP.Model provideLoginActivityModel(RetrofitRepository repository){
        return new RetrofitModel(repository);
    }

    @Provides
    public RetrofitRepository provideLoginRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
        return new MemoryRepository(context, sharedPreferences, retrofitRedditAPI);
    }
}
