package com.qartf.doseforreddit.dialog;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.qartf.doseforreddit.R;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ART_F on 2017-09-06.
 */

public class LoginDialog {
    @BindString(R.string.pref_login_signed_in) String prefLoginSignedIn;
    private AlertDialog dialog;
    private SharedPreferences sharedPreferences;

    public LoginDialog(AppCompatActivity mainActivity, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        dialog = new AlertDialog.Builder(mainActivity)
                .setView(R.layout.dialog_login)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
    }

    @OnClick(R.id.cancelDialog)
    public void cancelDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @OnClick(R.id.loginDialog)
    public void searchDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
        sharedPreferences.edit().putString(prefLoginSignedIn, "Anonymous").apply();
    }
}

