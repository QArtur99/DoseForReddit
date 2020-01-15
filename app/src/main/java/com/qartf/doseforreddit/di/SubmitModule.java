package com.qartf.doseforreddit.di;

import com.qartf.doseforreddit.data.repository.DataRepository;
import com.qartf.doseforreddit.ui.submit.SubmitMVP;
import com.qartf.doseforreddit.ui.submit.SubmitModel;
import com.qartf.doseforreddit.ui.submit.SubmitPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SubmitModule {

    @Provides
    @Singleton
    public SubmitMVP.Presenter provideLoginActivityPresenter(SubmitMVP.Model model){
        return new SubmitPresenter(model);
    }

    @Provides
    @Singleton
    public SubmitMVP.Model provideLoginActivityModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility){
        return new SubmitModel(repositoryRetrofit, repositoryUtility);
    }

}
