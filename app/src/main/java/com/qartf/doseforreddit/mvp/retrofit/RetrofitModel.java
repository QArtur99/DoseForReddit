package com.qartf.doseforreddit.mvp.retrofit;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.mvp.data.repository.RetrofitRepository;

import io.reactivex.Observable;


public class RetrofitModel implements RetrofitMVP.Model {

    private RetrofitRepository repository;

    public RetrofitModel(RetrofitRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<AccessToken> getAccessToken() {
        return repository.getAccessToken();
    }

    @Override
    public Observable<PostParent> getPosts(AccessToken accessToken) {
        return repository.getPosts(accessToken);
    }

    @Override
    public void onStop() {

    }


}
