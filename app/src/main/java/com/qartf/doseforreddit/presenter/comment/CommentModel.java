package com.qartf.doseforreddit.presenter.comment;

import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;
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
    public Observable<ChildCommentParent> getMorechildren(Comment comment) {
        return repositoryRetrofit.getMorechildren(comment);
    }

    @Override
    public Observable<ResponseBody> postVote(String dir, String fullname) {
        return repositoryRetrofit.postVote(dir, fullname);
    }

    @Override
    public Observable<SubmitParent> postComment(String fullname, String text) {
        return repositoryRetrofit.postComment(fullname, text);
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
    public Observable<ResponseBody> postDel(String fullname) {
        return repositoryRetrofit.postDel(fullname);
    }

    @Override
    public boolean checkConnection() {
        return repositoryUtility.checkConnection();
    }
}
