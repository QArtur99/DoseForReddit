package com.qartf.doseforreddit.mvp.presenter.post;

import com.qartf.doseforreddit.mvp.data.model.PostParent;
import com.qartf.doseforreddit.mvp.data.repository.DataRepository;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public class PostModel implements PostMVP.Model {

    private DataRepository.Retrofit repositoryRetrofit;
    private DataRepository.Utility repositoryUtility;

    public PostModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility) {
        this.repositoryRetrofit = repositoryRetrofit;
        this.repositoryUtility = repositoryUtility;
    }

    @Override
    public Observable<PostParent> getPosts() {
        return repositoryRetrofit.getPosts();
    }

    @Override
    public Observable<PostParent> searchPosts() {
        return repositoryRetrofit.searchPosts();
    }

    @Override
    public Observable<ResponseBody> postVote(String dir, String fullname) {
        return repositoryRetrofit.postVote(dir, fullname);
    }

    @Override
    public boolean checkConnection() {
        return repositoryUtility.checkConnection();
    }
}
