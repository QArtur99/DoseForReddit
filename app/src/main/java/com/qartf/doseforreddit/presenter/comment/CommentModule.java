package com.qartf.doseforreddit.presenter.comment;

import com.qartf.doseforreddit.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class CommentModule {

    @Provides
    @Singleton
    public CommentMVP.Presenter provideLoginActivityPresenter(CommentMVP.Model model){
        return new CommentPresenter(model);
    }

    @Provides
    @Singleton
    public CommentMVP.Model provideLoginActivityModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility){
        return new CommentModel(repositoryRetrofit, repositoryUtility);
    }
}
