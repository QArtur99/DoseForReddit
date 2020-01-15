package com.qartf.doseforreddit.ui.comment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.qartf.doseforreddit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuickReplyDialog {

    @BindView(R.id.submitTextEditText) EditText submitTextEditText;
    private AlertDialog dialog;
    private Context context;
    private QuickReplyInter quickReplyInter;
    private String fullname;


    public QuickReplyDialog(Context context, String fullname, QuickReplyInter quickReplyInter) {
        this.context = context;
        this.fullname = fullname;
        this.quickReplyInter = quickReplyInter;
        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_quick_reply)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
    }

    @OnClick(R.id.cancelButton)
    public void cancelDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @OnClick(R.id.submitButton)
    public void searchDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }

        if(TextUtils.isEmpty(submitTextEditText.getText().toString())){
            Toast.makeText(context, "Text can't be emplty!", Toast.LENGTH_LONG).show();
            return;
        }

        quickReplyInter.submitComment(fullname, submitTextEditText.getText().toString());
    }

    public interface QuickReplyInter {
        void submitComment(String fullname, String submitText);
    }

}
