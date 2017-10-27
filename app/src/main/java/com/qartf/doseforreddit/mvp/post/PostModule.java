package com.qartf.doseforreddit.mvp.post;

import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

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
    public PostMVP.Model provideLoginActivityModel(DataRepository.Retrofit repository){
        return new PostModel(repository);
    }

//    @Provides
//    @Singleton
//    public RetrofitRepository provideLoginRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI){
//        return new MemoryRepository(context, sharedPreferences, retrofitRedditAPI);
//    }
}
