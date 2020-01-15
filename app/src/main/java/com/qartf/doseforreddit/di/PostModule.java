package com.qartf.doseforreddit.di;

import com.qartf.doseforreddit.data.repository.DataRepository;
import com.qartf.doseforreddit.ui.post.PostMVP;
import com.qartf.doseforreddit.ui.post.PostModel;
import com.qartf.doseforreddit.ui.post.PostPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class PostModule {

    @Provides
    @Singleton
    public PostMVP.Presenter provideLoginActivityPresenter(PostMVP.Model model){
        return new PostPresenter(model);
    }

    @Provides
    @Singleton
    public PostMVP.Model provideLoginActivityModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility){
        return new PostModel(repositoryRetrofit, repositoryUtility);
    }

}
