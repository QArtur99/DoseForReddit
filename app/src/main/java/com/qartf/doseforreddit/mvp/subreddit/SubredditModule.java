package com.qartf.doseforreddit.mvp.subreddit;

import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SubredditModule {

    @Provides
    @Singleton
    public SubredditMVP.Presenter provideLoginActivityPresenter(SubredditMVP.Model model){
        return new SubredditPresenter(model);
    }

    @Provides
    @Singleton
    public SubredditMVP.Model provideLoginActivityModel(DataRepository.Retrofit repository){
        return new SubredditModel(repository);
    }

//    @Provides
//    @Singleton
//    public RetrofitRepository provideLoginRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
//        return new MemoryRepository(context, sharedPreferences, retrofitRedditAPI);
//    }
}
