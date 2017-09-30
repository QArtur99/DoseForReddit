package com.qartf.doseforreddit.utility;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.ImageActivity;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.activity.SelfActivity;
import com.qartf.doseforreddit.activity.VideoActivity;
import com.qartf.doseforreddit.model.Post;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ART_F on 2017-09-05.
 */

public class Utility {
    private static final String DOT = "\u2022";
    private static final String KILO = "K";

    private static DecimalFormat decimalFormat = new DecimalFormat("##.#");

    public static void timeFormat(String timeDoubleString, TextView time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long currentTime = calendar.getTimeInMillis() / 1000L;
        double timeDouble = Double.valueOf(timeDoubleString);
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

    public static int getTabLayoutIndex(String[] array, String value) {
        for(int i=0;i<array.length;i++) {
            if (array[i].equals(value)){
                return i;
            }
        }
        return -1;
    }
    
    public static void startIntentPreview(FragmentActivity fragmentActivity, Post post){
        Intent intent = null;
        String link = "";
        String postHint = post.postHint;
        if (!post.previewGif.isEmpty()) {
            String to_remove = "amp;";
            link = post.previewGif.replace(to_remove, "");
            intent = new Intent(fragmentActivity, VideoActivity.class);
        } else if (postHint.equals("link")) {
            String to_remove = "amp;";
            link = post.previewUrl.replace(to_remove, "");
            intent = new Intent(fragmentActivity, ImageActivity.class);
        } else if (postHint.equals("rich:video")) {
            if (post.domain.contains("youtube.com")) {
                Uri uri = Uri.parse(post.url);
                String v = uri.getQueryParameter("v");
                link = "https://www.youtube.com/embed/" + v;
                intent = new Intent(fragmentActivity, LinkActivity.class);
            } else if (post.domain.contains("youtu.be")) {
                Uri uri = Uri.parse(post.url);
                link = "https://www.youtube.com/embed/" + uri.getLastPathSegment();
                intent = new Intent(fragmentActivity, LinkActivity.class);
            } else {
                String to_remove = "amp;";
                link = post.previewUrl.replace(to_remove, "");
                intent = new Intent(fragmentActivity, ImageActivity.class);
            }
        } else if (postHint.equals("image")) {
            String to_remove = "amp;";
            link = post.previewUrl.replace(to_remove, "");
            intent = new Intent(fragmentActivity, ImageActivity.class);
        } else if (postHint.equals("self") || post.domain.contains("self") && !post.selftext.isEmpty()) {
            link = post.selftext;
            intent = new Intent(fragmentActivity, SelfActivity.class);
        }

        if (intent != null) {
            intent.putExtra("link", link);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(fragmentActivity).toBundle();
                fragmentActivity.startActivity(intent, bundle);
            } else {
                fragmentActivity.startActivity(intent);
            }
        }
    }

    public static void loadThumbnail(Context context, Post post, ImageView thumbnail) {
        if(!post.previewUrl.isEmpty() && checkThumbnail(post.thumbnail)){
            String to_remove = "amp;";
            String new_string = post.previewUrl.replace(to_remove, "");
            Glide.with(context)
                    .load(new_string)
                    .thumbnail(Glide.with(context).load(R.drawable.ic_ondemand_video))
                    .into(thumbnail);
        } else if (!post.thumbnail.isEmpty() && !checkThumbnail(post.thumbnail)) {
            String to_remove = "amp;";
            String new_string = post.thumbnail.replace(to_remove, "");
            Glide.with(context)
                    .load(new_string)
                    .thumbnail(Glide.with(context).load(R.drawable.ic_ondemand_video))
                    .into(thumbnail);
        } else {
            thumbnail.setImageResource(R.drawable.ic_ondemand_video);
        }
    }

    private static Boolean checkThumbnail(String thumbnail) {
        if (thumbnail.equals("self")) {
            return true;
        }
        else
        if (thumbnail.equals("default")) {
            return true;
        }
        else if (thumbnail.equals("image")) {
            return true;
        }
        else if (thumbnail.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void setThumbnailSize(Context context, View view) {
        ImageView imageView =  view.findViewById(R.id.thumbnail);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getRealSize(size);
        int height;
        if(pxToDp(size.y) >= 600 && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            height = size.x / 10;
        }else{
            height = size.x / 5;
        }

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = height;
        imageView.setLayoutParams(layoutParams);
    }

    public static Boolean isTablet(Context context){
        Boolean isTablet = false;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        if(pxToDp(size.y) >= 600 && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            isTablet = true;
        }
        return isTablet;
    }

    private static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static void upsFormat(TextView score, int upsInteger) {
        if (upsInteger >= 10000) {
            double upsDouble = upsInteger / 1000d;
            String upsString = decimalFormat.format(upsDouble) + KILO;
            score.setText(upsString);
        } else {
            String points = String.valueOf(upsInteger);
            score.setText(points);
        }
    }

    public static void upsFormatPoints(TextView score, int upsInteger) {
        if (upsInteger >= 10000) {
            double upsDouble = upsInteger / 1000d;
            String upsString = decimalFormat.format(upsDouble) + KILO + " points";
            score.setText(upsString);
        } else {
            String points = String.valueOf(upsInteger) + " points";
            score.setText(points);
        }
    }

    public static void loadLinkFlairText(TextView textView,String linkFlairText) {
        if (!linkFlairText.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(linkFlairText);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static void sleepOneSec() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

}
