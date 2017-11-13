package com.qartf.doseforreddit.view.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.utility.Navigation;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostDetailSettings {

    private AlertDialog dialog;
    private Activity context;
    private PostDetailSettingsInter postDetailSettingsInter;
    private Post post;


    public PostDetailSettings(Activity context, Post post, PostDetailSettingsInter postDetailSettingsInter) {
        this.context = context;
        this.post = post;
        this.postDetailSettingsInter = postDetailSettingsInter;
        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_detial_settings)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
    }

//    @OnClick(R.id.)
//    public void cancelDialogOnClick() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//    }

    @OnClick(R.id.share)
    public void searchDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Navigation.shareContent(context, post.url);
    }

    public interface PostDetailSettingsInter {
        void submitComment(String fullname, String submitText);
    }
}
