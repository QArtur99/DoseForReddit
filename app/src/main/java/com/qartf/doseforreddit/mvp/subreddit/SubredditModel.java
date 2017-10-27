package com.qartf.doseforreddit.mvp.subreddit;

import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import io.reactivex.Observable;

/**
 * Created by ART_F on 2017-10-26.
 */

public class SubredditModel implements SubredditMVP.Model {

    private DataRepository.Retrofit repository;

    public SubredditModel(DataRepository.Retrofit repository) {
        this.repository = repository;
    }


    @Override
    public Observable<SubredditParent> getSubreddits() {
        return repository.getSubreddits();
    }


}
