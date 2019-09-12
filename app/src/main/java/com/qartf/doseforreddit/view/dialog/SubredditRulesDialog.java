package com.qartf.doseforreddit.view.dialog;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.RulesItem;
import com.qartf.doseforreddit.view.adapter.SubredditRulesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditRulesDialog {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    public GridLayoutManager layoutManager;
    public SubredditRulesAdapter postsAdapter;

    private Context context;
    private RuleParent ruleParent;
    private AlertDialog dialog;

    public SubredditRulesDialog(Context context, RuleParent ruleParent) {
        this.context = context;
        this.ruleParent = ruleParent;

        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_subreddit_rules)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        setAdapter(ruleParent.rules);
    }

    public void setAdapter(List<RulesItem> movieList) {
        layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        postsAdapter = new SubredditRulesAdapter(context, movieList);
        recyclerView.setAdapter(postsAdapter);
    }
}
