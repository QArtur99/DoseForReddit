package com.qartf.doseforreddit.view.dialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.presenter.utility.Navigation;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostDetailSettings {

    @BindView(R.id.delete) TextView delete;
    @BindString(R.string.pref_login_signed_in) String prefLoginSignedIn;
    private AlertDialog dialog;
    private Activity context;
    private PostDetailSettingsInter postDetailSettingsInter;
    private Post post;
    private QuickReplyDialog.QuickReplyInter quickReplyInter;


    @Inject
    SharedPreferences sharedPreferences;

    public PostDetailSettings(Activity context, Post post, PostDetailSettingsInter postDetailSettingsInter, QuickReplyDialog.QuickReplyInter quickReplyInter) {
        this.context = context;
        this.post = post;
        this.postDetailSettingsInter = postDetailSettingsInter;
        this.quickReplyInter = quickReplyInter;
        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_detial_settings)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);
        ((App)context.getApplicationContext()).getComponent().inject(this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        String username = sharedPreferences.getString(prefLoginSignedIn, Constants.Utility.ANONYMOUS);
        if(post.author.equals(username)){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.delete)
    public void deleteDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        postDetailSettingsInter.removePost(post.name);
    }

    @OnClick(R.id.goToUrl)
    public void goToUrlDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Navigation.startLinkActivity(context, post.url);
    }

    @OnClick(R.id.share)
    public void searchDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Navigation.shareContent(context, post.url);
    }


    @OnClick(R.id.submitComment)
    public void submitCommentDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        new QuickReplyDialog(context, post.name , quickReplyInter);
    }

    public interface PostDetailSettingsInter {
        void removePost(String fullname);
    }
}
