package com.qartf.doseforreddit.di;

import com.qartf.doseforreddit.data.repository.DataRepository;
import com.qartf.doseforreddit.ui.subreddit.SubredditMVP;
import com.qartf.doseforreddit.ui.subreddit.SubredditModel;
import com.qartf.doseforreddit.ui.subreddit.SubredditPresenter;

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
    public SubredditMVP.Model provideLoginActivityModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility){
        return new SubredditModel(repositoryRetrofit, repositoryUtility);
    }

}
