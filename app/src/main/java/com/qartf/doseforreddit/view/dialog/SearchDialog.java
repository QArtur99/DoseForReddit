package com.qartf.doseforreddit.view.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.utility.Constants.Id;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDialog {
    @BindView(R.id.titleDialogSearch) TextView titleDialogSearch;
    @BindView(R.id.searchDialogEditText) EditText searchDialogEditText;
    @BindString(R.string.search_posts) String searchPosts;
    @BindString(R.string.search_subreddits) String searchSubreddits;
    @BindString(R.string.pref_post_subreddit) String prefSubredditKey;
    @BindString(R.string.pref_post_subreddit_default) String prefSubredditDefault;
    @BindString(R.string.pref_search_post) String prefSearchPost;
    @BindString(R.string.pref_search_subreddit) String prefSearchSubreddit;
    @BindString(R.string.pref_empty_tag) String prefEmptyTag;
    private AlertDialog dialog;
    private int searchId;
    private SearchDialogInter searchDialogInter;

    private Context context;

    public SearchDialog(Context context, int searchId, SearchDialogInter searchDialogInter) {
        this.context = context;
        this.searchId = searchId;
        this.searchDialogInter = searchDialogInter;

        dialog = new AlertDialog.Builder(context)
                .setView(R.layout.dialog_search)
                .create();
        dialog.show();
        ButterKnife.bind(this, dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        String title = searchId == Id.SEARCH_POSTS ? searchPosts : searchSubreddits;
        titleDialogSearch.setText(title);
    }

    @OnClick(R.id.cancelDialog)
    public void cancelDialogOnClick() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @OnClick(R.id.searchDialog)
    public void searchDialogOnClick() {
        searchDialogInter.loadFragment(searchId);

        String queryString = searchDialogEditText.getText().toString();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(searchId == Id.SEARCH_POSTS) {
            sharedPreferences.edit().putString(prefSearchPost, queryString).apply();
            searchDialogInter.searchPosts();
        }else if(searchId == Id.SEARCH_SUBREDDITS){
            sharedPreferences.edit().putString(prefSearchSubreddit, queryString).apply();
            searchDialogInter.searchSubreddits();
        }

        searchDialogInter.setTitle("Search: " + queryString);

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface SearchDialogInter {
            void searchPosts();

            void searchSubreddits();

            void loadFragment(int fragmentId);

            void setTitle(String title);
    }

}
