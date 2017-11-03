package com.qartf.doseforreddit.presenter.submit;

import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.repository.DataRepository;

import io.reactivex.Observable;

public class SubmitModel implements SubmitMVP.Model {

    private DataRepository.Retrofit repository;

    public SubmitModel(DataRepository.Retrofit repository) {
        this.repository = repository;
    }


    @Override
    public Observable<RuleParent> getSubredditRules() {
        return repository.getSubredditRules();
    }
}
