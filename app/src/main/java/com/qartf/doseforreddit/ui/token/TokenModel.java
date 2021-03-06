package com.qartf.doseforreddit.ui.token;

import com.qartf.doseforreddit.data.repository.DataRepository;

import io.reactivex.Observable;

public class TokenModel implements TokenMVP.Model {

    private DataRepository.Retrofit repository;

    public TokenModel(DataRepository.Retrofit repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Object> tokenInfo() {
        return repository.tokenInfo();
    }

    @Override
    public Observable<String> resetToken() {
        return repository.resetToken();
    }

    @Override
    public Observable<String> getAboutMe() {
        return repository.getAboutMe();
    }


}