package com.qartf.doseforreddit.presenter.token;

import com.qartf.doseforreddit.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TokenModule {

    @Provides
    @Singleton
    public TokenMVP.Presenter provideLoginActivityPresenter(TokenMVP.Model model){
        return new TokenPresenter(model);
    }

    @Provides
    @Singleton
    public TokenMVP.Model provideLoginActivityModel(DataRepository.Retrofit repository){
        return new TokenModel(repository);
    }
}