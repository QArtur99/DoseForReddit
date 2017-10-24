package com.qartf.doseforreddit.mvp.data.repository;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;

import io.reactivex.Observable;

public interface RetrofitRepository {
    Observable<AccessToken> getAccessToken();
    Observable<AccessToken> refreshToken(AccessToken accessToken);
    Observable<PostParent> getPosts(AccessToken accessToken);
//    User getUser();
//
//    void saveUser(User user);
}
