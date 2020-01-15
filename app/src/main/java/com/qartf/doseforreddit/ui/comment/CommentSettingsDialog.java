package com.qartf.doseforreddit.ui.comment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.App;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Navigation;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentSettingsDialog {

    @BindView(R.id.delete) TextView delete;
    @BindString(R.string.pref_login_signed_in) String prefLoginSignedIn;
    private AlertDialog dialog;
    private Activity context;
    private CommentSettingsInter commentSettingsInter;
    private Comment comment;

    @Inject
    SharedPreferences sharedPreferences;


    public CommentSettingsDialog(Activity context, Comment comment, CommentSettingsInter commentSettingsInter) {
        this.context = context;
        this.comment = comment;
        this.commentSettingsInter = commentSettingsInter;
        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_comment_setting)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);
        ((App)context.getApplicationContext()).getComponent().inject(this);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        String username = sharedPreferences.getString(prefLoginSignedIn, Constants.Utility.ANONYMOUS);
        if(comment.author.equals(username)){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

    }


    @OnClick(R.id.share)
    public void searchDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Navigation.shareContent(context, comment.body);
    }

    @OnClick(R.id.delete)
    public void deleteDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        commentSettingsInter.removeComment(comment.name);
    }



    public interface CommentSettingsInter {
        void removeComment(String fullname);
    }
}
