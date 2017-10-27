package com.qartf.doseforreddit.mvp.post;

import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import io.reactivex.Observable;


public class PostModel implements PostMVP.Model {

    private DataRepository.Retrofit repository;

    public PostModel(DataRepository.Retrofit repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PostParent> getPosts() {
        return repository.getPosts();
    }
}
