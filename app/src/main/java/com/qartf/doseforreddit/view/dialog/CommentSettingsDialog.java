package com.qartf.doseforreddit.view.dialog;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.presenter.utility.Navigation;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentSettingsDialog {

    private AlertDialog dialog;
    private Activity context;
    private CommentSettingsInter commentSettingsInter;
    private Comment comment;


    public CommentSettingsDialog(Activity context, Comment comment, CommentSettingsInter commentSettingsInter) {
        this.context = context;
        this.comment = comment;
        this.commentSettingsInter = commentSettingsInter;
        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_comment_setting)
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
        Navigation.shareContent(context, comment.body);
    }

    public interface CommentSettingsInter {
        void submitComment(String fullname, String submitText);
    }
}
