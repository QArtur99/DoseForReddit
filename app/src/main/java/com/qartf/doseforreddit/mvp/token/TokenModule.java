package com.qartf.doseforreddit.mvp.token;

import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ART_F on 2017-10-26.
 */
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

//    @Provides
//    @Singleton
//    public RetrofitRepository provideLoginRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
//        return new MemoryRepository(context, sharedPreferences, retrofitRedditAPI);
//    }

}
