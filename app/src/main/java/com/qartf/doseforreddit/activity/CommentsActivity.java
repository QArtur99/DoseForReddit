package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.model.PostObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DOT = "\u2022";
    public static final String KILO = "K";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.thumbnail) ImageView thumbnail;
    @BindView(R.id.ups) TextView ups;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.subreddit) TextView subreddit;
    @BindView(R.id.linkFlairText) TextView linkFlairText;
    @BindView(R.id.domain) TextView domain;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.expandArea) LinearLayout expandArea;
    @BindView(R.id.upContainer) RelativeLayout upContainer;
    @BindView(R.id.downContainer) RelativeLayout downContainer;
    @BindView(R.id.detailContainer) RelativeLayout detailContainer;
    @BindView(R.id.imageContainer) RelativeLayout imageContainer;
    private DecimalFormat decimalFormat = new DecimalFormat("##.#");
    private PostObject post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        setToolbar();

        expandArea.setSelected(true);

        post = new Gson().fromJson(getIntent().getStringExtra("link"), new TypeToken<PostObject>() {
        }.getType());

        setListeners();
        loadData();

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners() {
        upContainer.setOnClickListener(this);
        downContainer.setOnClickListener(this);
        detailContainer.setOnClickListener(this);
        imageContainer.setOnClickListener(this);
    }

    private void loadData() {
        upsFormat();
        title.setText(post.title);
        loadLinkFlairtext();
        domain.setText("(" + post.domain + ")");
        subreddit.setText(post.subreddit);
        comments.setText(DOT + post.numComents + " comments");
        timeFormat();
        loadThumnail();
    }

    private void loadLinkFlairtext() {
        if (!post.linkFlairText.equals("null")) {
            linkFlairText.setVisibility(View.VISIBLE);
            linkFlairText.setText(post.linkFlairText);
        } else {
            linkFlairText.setVisibility(View.GONE);
        }
    }

    private void loadThumnail() {
        if (checkThumbnail(post.thumbnail)) {
            Glide.with(this)
                    .load(post.thumbnail)
                    .thumbnail(Glide.with(this).load(R.drawable.ic_ondemand_video))
                    .into(thumbnail);
        } else {
            thumbnail.setImageResource(R.drawable.ic_ondemand_video);
        }
    }

    public Boolean checkThumbnail(String thumnail) {
        if (thumnail.equals("self")) {
            return false;
        } else if (thumnail.equals("default")) {
            return false;
        } else if (thumnail.equals("image")) {
            return false;
        } else if (thumnail.isEmpty()) {
            return false;
        }
        return true;
    }

    private void upsFormat() {
        int upsInteger = Integer.valueOf(post.ups);
        if (upsInteger >= 10000) {
            double upsDouble = upsInteger / 1000d;
            String upsString = decimalFormat.format(upsDouble) + KILO;
            ups.setText(upsString);
        } else {
            ups.setText(String.valueOf(upsInteger));
        }
    }

    private void timeFormat() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long currentTime = calendar.getTimeInMillis() / 1000L;
        double timeDouble = Double.valueOf(post.createdUTC);
        long trueTime = currentTime - ((long) timeDouble);
        if (trueTime >= 31536000) {
            int date = (int) trueTime / 31536000;
            String dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "yr" : "yrs";
            time.setText(dateString);
        } else if (trueTime >= 2592000) {
            int date = (int) trueTime / 2592000;
            String dateString = DOT + String.valueOf(date) + "m";
            time.setText(dateString);
        } else if (trueTime >= 604800) {
            int date = (int) trueTime / 604800;
            String dateString = DOT + String.valueOf(date) + "wk";
            time.setText(dateString);
        } else if (trueTime >= 86400) {
            int date = (int) trueTime / 86400;
            String dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "day" : "days";
            time.setText(dateString);
        } else if (trueTime >= 3600) {
            int date = (int) trueTime / 3600;
            String dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "hr" : "hrs";
            time.setText(dateString);
        } else if (trueTime >= 60) {
            int date = (int) trueTime / 60;
            String dateString = DOT + String.valueOf(date) + "min";
            time.setText(dateString);
        } else {
            String dateString = DOT + String.valueOf(trueTime) + "sec";
            time.setText(dateString);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upContainer:
                break;
            case R.id.downContainer:
                break;
            case R.id.detailContainer:
                break;
            case R.id.imageContainer:
                onListItemClick(v);
                break;
            case R.id.commentsAction:
                break;
        }
    }

    public void onListItemClick(View view) {
        Intent intent = null;
        String link = "";
        String postHint = post.postHint;
        if (postHint.equals("link")) {
            if (post.domain.contains("imgur")) {
                String v = post.url;
                if (v.contains("gallery")) {
                    link = post.url;
                    intent = new Intent(this, LinkActivity.class);
                } else if (v.contains("gif")) {
                    link = v.substring(0, v.lastIndexOf('.')) + ".mp4";
                    intent = new Intent(this, VideoActivity.class);
                } else {
                    String to_remove = "amp;";
                    link = post.previewUrl.replace(to_remove, "");
                    intent = new Intent(this, ImageActivity.class);
                }
            } else {
                String to_remove = "amp;";
                link = post.previewUrl.replace(to_remove, "");
                intent = new Intent(this, ImageActivity.class);
            }
        } else if (postHint.equals("rich:video")) {
            if (post.domain.contains("youtube.com")) {
                Uri uri = Uri.parse(post.url);
                String v = uri.getQueryParameter("v");
                link = "https://www.youtube.com/embed/" + v;
                intent = new Intent(this, LinkActivity.class);
            } else if (post.domain.contains("youtu.be")) {
                Uri uri = Uri.parse(post.url);
                link = "https://www.youtube.com/embed/" + uri.getLastPathSegment();
                intent = new Intent(this, LinkActivity.class);
            } else if (post.domain.contains("gfycat.com")) {
                String to_remove = "//";
                link = post.url.replace(to_remove, "//fat.") + ".webm";
                intent = new Intent(this, VideoActivity.class);
            } else {
                String to_remove = "amp;";
                link = post.previewUrl.replace(to_remove, "");
                intent = new Intent(this, ImageActivity.class);
            }
        } else if (postHint.equals("image")) {
            String to_remove = "amp;";
            link = post.url.contains(".gif") ? post.url.replace(to_remove, "") : post.previewUrl.replace(to_remove, "");
            intent = new Intent(this, ImageActivity.class);
        } else if (postHint.equals("self") || post.domain.contains("self") && !post.selftext.isEmpty()) {
            link = post.selftext;
            intent = new Intent(this, SelfActivity.class);
        } else if (postHint.isEmpty()) {
            link = new Gson().toJson(post);
            intent = new Intent(this, CommentsActivity.class);
        }

        if(intent != null) {
            intent.putExtra("link", link);
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
