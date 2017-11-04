package com.qartf.doseforreddit.presenter.submit;

import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.repository.DataRepository;

import java.util.HashMap;

import io.reactivex.Observable;

public class SubmitModel implements SubmitMVP.Model {

    private DataRepository.Retrofit repositoryRetrofit;
    private DataRepository.Utility repositoryUtility;

    public SubmitModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility) {
        this.repositoryRetrofit = repositoryRetrofit;
        this.repositoryUtility = repositoryUtility;
    }


    @Override
    public boolean checkConnection() {
        return repositoryUtility.checkConnection();
    }

    @Override
    public Observable<RuleParent> getSubredditRules() {
        return repositoryRetrofit.getSubredditRules();
    }

    @Override
    public Observable<SubmitParent> postSubmit(HashMap<String, String> args) {
        return repositoryRetrofit.postSubmit(args);
    }
}
