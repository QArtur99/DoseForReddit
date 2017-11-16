package com.qartf.doseforreddit.view.fragment;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.submit.SubmitMVP;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.dialog.SubredditRulesDialog;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class SubmitFragment extends BaseFragmentMvp<SubmitFragment.SubmitFragmentInt> implements SubmitMVP.View {


    private static final int MAX_SIGNS = 15;
    @BindView(R.id.tabLayout) TabLayout tabLayout;

    @BindView(R.id.editTextSubreddit) EditText editTextSubreddit;
    @BindView(R.id.editTextTitle) EditText editTextTitle;
    @BindView(R.id.textViewTitleCounter) TextView textViewTitleCounter;
    @BindView(R.id.sendReplies) CheckBox sendReplies;

    @BindView(R.id.textViewUrl) TextView textViewUrl;
    @BindView(R.id.editTextUrl) EditText editTextUrl;
    @BindView(R.id.extraMarginUrl) View extraMarginUrl;
    @BindView(R.id.textViewText) TextView textViewText;
    @BindView(R.id.editTextText) EditText editTextText;
    @BindView(R.id.extraMarginLink) View extraMarginLink;
    @BindView(R.id.textViewImageUrl) TextView textViewImageUrl;
    @BindView(R.id.editTextImageUrl) EditText editTextImageUrl;
    @BindView(R.id.extraMarginImageUrl) View extraMarginImageUrl;
    @BindView(R.id.fabBottom) FloatingActionButton fabBottom;

    @BindString(R.string.pref_post_subreddit_rule) String prefPostSubredditRule;
    @BindString(R.string.pref_post_sort_by) public String prefPostSortBy;

    @Inject
    SubmitMVP.Presenter presenter;
    private String subredditString;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_submit;
    }

    @Override
    public void initComponents() {
        ((App) getContext().getApplicationContext()).getComponent().inject(this);
        Utility.setTabLayoutDivider(getContext(), tabLayout);
        addTabLayoutTabs();
        setTabLayoutListener();
        setLinkView();

        setCharLimit(editTextTitle, MAX_SIGNS);
        setEditTextTitleListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @OnClick(R.id.subredditRule)
    public void onClickSubredditRule() {
        if (!editTextSubreddit.getText().toString().isEmpty()) {
            sharedPreferences.edit().putString(prefPostSubredditRule, editTextSubreddit.getText().toString()).apply();
            presenter.getSubredditRules();
        } else {
            Toast.makeText(getContext(), "Subreddit cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.fabBottom)
    public void onClickFabBottom() {
        int selected = tabLayout.getSelectedTabPosition();
        switch (selected) {
            case 0:
                submitLink();
                break;
            case 1:
                submitText();
                break;
            case 2:
                submitImage();
                break;
        }
    }

    private void submitImage() {

    }


    private void submitText() {
        subredditString = editTextSubreddit.getText().toString();
        if (TextUtils.isEmpty(subredditString)) {
            error("Please enter the subreddit.");
            return;
        }

        String titleString = editTextTitle.getText().toString();
        if (TextUtils.isEmpty(titleString)) {
            error("Please enter the title.");
            return;
        }


        String text = editTextText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            error("Please enter the text.");
            return;
        }

        HashMap<String, String> args = new HashMap<>();
        args.put("sr", subredditString);
        args.put("title", titleString);
        args.put("sendreplies", String.valueOf(sendReplies.isChecked()));
        args.put("kind", "self");
        args.put("text", text);

        presenter.postSubmit(args);
    }

    private void submitLink() {
        subredditString = editTextSubreddit.getText().toString();
        if (TextUtils.isEmpty(subredditString)) {
            error("Please enter the subreddit.");
            return;
        }

        String titleString = editTextTitle.getText().toString();
        if (TextUtils.isEmpty(titleString)) {
            error("Please enter the title.");
            return;
        }


        String urlLink = editTextUrl.getText().toString();
        if (!URLUtil.isValidUrl(urlLink)) {
            error("Url is not valid!");
            return;
        }

        HashMap<String, String> args = new HashMap<>();
        args.put("sr", subredditString);
        args.put("title", titleString);
        args.put("sendreplies", String.valueOf(sendReplies.isChecked()));
        args.put("kind", "link");
        args.put("url", urlLink);

        presenter.postSubmit(args);
    }

    private void setEditTextTitleListener() {
        editTextTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                String counterString = String.valueOf(start + 1 + "/" + MAX_SIGNS);
                textViewTitleCounter.setText(counterString);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected = tabLayout.getSelectedTabPosition();
                switch (selected) {
                    case 0:
                        setLinkView();
                        break;
                    case 1:
                        setTextView();
                        break;
                    case 2:
                        setImageView();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void setCharLimit(EditText et, int max) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max);
        et.setFilters(filters);
    }

    private void setImageView() {
        textViewUrl.setVisibility(View.GONE);
        editTextUrl.setVisibility(View.GONE);
        extraMarginUrl.setVisibility(View.GONE);

        textViewText.setVisibility(View.GONE);
        editTextText.setVisibility(View.GONE);
        extraMarginLink.setVisibility(View.GONE);

        textViewImageUrl.setVisibility(View.VISIBLE);
        editTextImageUrl.setVisibility(View.VISIBLE);
        extraMarginImageUrl.setVisibility(View.VISIBLE);
    }

    private void setTextView() {
        textViewUrl.setVisibility(View.GONE);
        editTextUrl.setVisibility(View.GONE);
        extraMarginUrl.setVisibility(View.GONE);

        textViewText.setVisibility(View.VISIBLE);
        editTextText.setVisibility(View.VISIBLE);
        extraMarginLink.setVisibility(View.VISIBLE);

        textViewImageUrl.setVisibility(View.GONE);
        editTextImageUrl.setVisibility(View.GONE);
        extraMarginImageUrl.setVisibility(View.GONE);
    }

    private void setLinkView() {
        textViewUrl.setVisibility(View.VISIBLE);
        editTextUrl.setVisibility(View.VISIBLE);
        extraMarginUrl.setVisibility(View.VISIBLE);

        textViewText.setVisibility(View.GONE);
        editTextText.setVisibility(View.GONE);
        extraMarginLink.setVisibility(View.GONE);

        textViewImageUrl.setVisibility(View.GONE);
        editTextImageUrl.setVisibility(View.GONE);
        extraMarginImageUrl.setVisibility(View.GONE);
    }


    private void addTabLayoutTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("LINK"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("TEXT"), 1);
//        tabLayout.addTab(tabLayout.newTab().setText("IMAGE"), 2);
    }


    @Override
    public void setSubredditRules(RuleParent ruleParent) {
        new SubredditRulesDialog(getContext(), ruleParent);
    }

    @Override
    public void setSubmitted() {
        Utility.hideKeyboardFrom(getActivity());
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences.edit().putString(getString(R.string.pref_post_subreddit), subredditString).apply();
                sharedPreferences.edit().putString(prefPostSortBy, "new").apply();
                mCallback.switchToPostFragment();
            }
        }, 1000);

    }

    @Override
    public void error(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public interface SubmitFragmentInt {

        void switchToPostFragment();
    }

}
