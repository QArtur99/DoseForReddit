package com.qartf.doseforreddit.utility;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.ui.contentView.ImageActivity;
import com.qartf.doseforreddit.ui.contentView.LinkActivity;
import com.qartf.doseforreddit.ui.contentView.SelfActivity;
import com.qartf.doseforreddit.ui.submit.SubmitActivity;
import com.qartf.doseforreddit.ui.contentView.VideoActivity;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.ui.comment.CommentsActivity;


public class Navigation {

    public static void setFragmentFrame(AppCompatActivity context, int frameId, Fragment fragment) {
        context.getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment)
                .commit();
    }

    public static void shareContent(Activity context, String postUrl){
        context.startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(context)
                .setType("text/plain")
                .setText(postUrl)
                .getIntent(), context.getResources().getString(R.string.action_share)));
    }

    public static void startLoginActivity(Activity activity){
        String url = String.format(Constants.Auth.AUTH_URL, Constants.Auth.CLIENT_ID, Constants.Auth.STATE, Constants.Auth.REDIRECT_URI, Constants.Auth.SCOPE);
        Intent intent = new Intent(activity, LinkActivity.class);
        intent.putExtra("login", Uri.parse(url).toString());
        initActivity(activity, intent);
    }

    public static void startLinkActivity(Activity activity, String url){
        Intent intent = new Intent(activity, LinkActivity.class);
        intent.putExtra("link", url);
        initActivity(activity, intent);
    }

    public static void startCommentsActivity(Activity activity, String postObject){
        Intent intent = new Intent(activity, CommentsActivity.class);
        intent.putExtra("link", postObject);
        initActivity(activity, intent);
    }

    public static void startSubmitActivity(Activity activity){
        Intent intent = new Intent(activity, SubmitActivity.class);
        initActivity(activity, intent);
    }

    private static void initActivity(Activity activity, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }

    public static void startDetailActivity(Activity activity, Post post) {
        String postObjectString = new Gson().toJson(post);
        startCommentsActivity(activity, postObjectString);
    }

    public static void startIntentPreview(FragmentActivity fragmentActivity, Post post){
        Intent intent = null;
        String link = "";
        String postHint = post.postHint;

        if (!post.previewGif.isEmpty()) {
            link = post.previewGif;
            intent = new Intent(fragmentActivity, VideoActivity.class);
        } else if (postHint.equals("link")) {
            link = post.previewUrl;
            intent = new Intent(fragmentActivity, ImageActivity.class);
        } else if (postHint.equals("rich:video")) {
            if (post.domain.contains("youtube.com")) {
                link = "https://www.youtube.com/embed/" + Uri.parse(post.url).getQueryParameter("v");
                intent = new Intent(fragmentActivity, LinkActivity.class);
            } else if (post.domain.contains("youtu.be")) {
                link = "https://www.youtube.com/embed/" + Uri.parse(post.url).getLastPathSegment();
                intent = new Intent(fragmentActivity, LinkActivity.class);
            } else {
                link = post.previewUrl;
                intent = new Intent(fragmentActivity, ImageActivity.class);
            }
        } else if (postHint.equals("image")) {
            link = post.previewUrl;
            intent = new Intent(fragmentActivity, ImageActivity.class);
        } else if (postHint.equals("self") || post.domain.contains("self") && !post.selftext.isEmpty()) {
            link = post.selftext;
            intent = new Intent(fragmentActivity, SelfActivity.class);
        }else if(postHint.isEmpty() && !post.thumbnail.isEmpty()){
            link = post.thumbnail;
            intent = new Intent(fragmentActivity, ImageActivity.class);
        }

        if (intent != null) {
            intent.putExtra("link", Utility.handleEscapeCharacter(link));
            initActivity(fragmentActivity, intent);
        }
    }

    public static void goToMail(Activity activity) {
        String email = "mailto:artur21133@gmail.com";

        Uri addressUri = Uri.parse(email);
        Intent intent = new Intent(Intent.ACTION_SENDTO, addressUri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Reddit Dose Feedback");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

}
