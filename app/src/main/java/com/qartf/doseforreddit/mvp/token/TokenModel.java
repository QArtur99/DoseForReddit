package com.qartf.doseforreddit.mvp.token;

import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import io.reactivex.Observable;

/**
 * Created by ART_F on 2017-10-26.
 */

public class TokenModel implements TokenMVP.Model {

    private DataRepository.Retrofit repository;

    public TokenModel(DataRepository.Retrofit repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Object> tokenInfo() {
        return repository.tokenInfo();
    }
}