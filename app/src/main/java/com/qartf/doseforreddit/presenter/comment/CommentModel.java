package com.qartf.doseforreddit.presenter.comment;

import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.repository.DataRepository;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class CommentModel implements CommentMVP.Model {

    private DataRepository.Retrofit repositoryRetrofit;
    private DataRepository.Utility repositoryUtility;

    public CommentModel(DataRepository.Retrofit repositoryRetrofit, DataRepository.Utility repositoryUtility) {
        this.repositoryRetrofit = repositoryRetrofit;
        this.repositoryUtility = repositoryUtility;
    }
    @Override
    public Observable<CommentParent> getComments() {
        return repositoryRetrofit.getComments();
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
