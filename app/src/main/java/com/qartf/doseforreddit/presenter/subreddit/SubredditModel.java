package com.qartf.doseforreddit.presenter.subreddit;

import com.qartf.doseforreddit.data.entity.SubredditParent;
import com.qartf.doseforreddit.data.repository.DataRepository;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by ART_F on 2017-10-26.
 */

public class SubredditModel implements SubredditMVP.Model {

    private DataRepository.Retrofit repositoryRetrofit;
    private DataRepository.Utility repositoryUtility;

    public SubredditModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility) {
        this.repositoryRetrofit = repositoryRetrofit;
        this.repositoryUtility = repositoryUtility;
    }


    @Override
    public Observable<SubredditParent> getSubreddits() {
        return repositoryRetrofit.getSubreddits();
    }

    @Override
    public Observable<ResponseBody> postSubscribe(String subscribe, String fullname) {
        return repositoryRetrofit.postSubscribe(subscribe, fullname);
    }

    @Override
    public boolean checkConnection() {
        return repositoryUtility.checkConnection();
    }

}
