package com.qartf.doseforreddit.fragmentControl;

import android.os.Bundle;
import android.view.View;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.utility.Constants;

/**
 * Created by ART_F on 2017-09-18.
 */

public class DetailFragmentControl implements DetailFragment.OnListItemClickListener {


    private MainActivity mainActivity;

    public DetailFragmentControl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public void onRefresh(int loaderId) {
        switch (loaderId) {
            case Constants.Id.COMMENTS:
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.COMMENTS, mainActivity.argsDetail, mainActivity).forceLoad();
                break;
        }

    }

    @Override
    public void getComments(PostObject post, String sortBy) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Comments.SUBREDDIT, post.subreddit);
        bundle.putString(Constants.Comments.ID, post.id);
        bundle.putString(Constants.Comments.SORT, sortBy);
        mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.COMMENTS, bundle, mainActivity).forceLoad();
    }

    @Override
    public void onClick(PostObject post, View view) {
        Bundle args;
        switch (view.getId()) {
            case R.id.upContainer:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "1");
                args.putString(Constants.Vote.FULLNAME, post.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
            case R.id.downContainer:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "-1");
                args.putString(Constants.Vote.FULLNAME, post.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
        }
    }

    @Override
    public void onCommentListItemClick(Comment comment, View view) {
        Bundle args;
        switch (view.getId()) {
            case R.id.commentVoteUp:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "1");
                args.putString(Constants.Vote.FULLNAME, comment.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
            case R.id.commentVoteDown:
                args = new Bundle();
                args.putString(Constants.Vote.DIR, "-1");
                args.putString(Constants.Vote.FULLNAME, comment.name);
                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.VOTE, args, mainActivity).forceLoad();
                break;
        }
    }

}
