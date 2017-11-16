package com.qartf.doseforreddit.presenter.post;

import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.repository.DataRepository;

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
    public Observable<PostParent> getPosts(String after) {
        return repositoryRetrofit.getPosts(after);
    }

    @Override
    public Observable<PostParent> searchPosts(String after) {
        return repositoryRetrofit.searchPosts(after);
    }

    @Override
    public Observable<ResponseBody> postVote(String dir, String fullname) {
        return repositoryRetrofit.postVote(dir, fullname);
    }

    @Override
    public Observable<ResponseBody> postSave(String fullname) {
        return repositoryRetrofit.postSave(fullname);
    }

    @Override
    public Observable<ResponseBody> postUnsave(String fullname) {
        return repositoryRetrofit.postUnsave(fullname);
    }

    @Override
    public boolean checkConnection() {
        return repositoryUtility.checkConnection();
    }
}
