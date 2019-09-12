package com.qartf.doseforreddit.presenter.utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ART_F on 2017-09-05.
 */

public class Utility {
    private static final String DOT = "\u2022";
    private static final String KILO = "K";

    private static final DecimalFormat decimalFormat = new DecimalFormat("##.#");

    public static String timeFormat(String timeDoubleString) {
        String dateString;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long currentTime = calendar.getTimeInMillis() / 1000L;
        double pastTime = Double.valueOf(timeDoubleString);
        long secAgo = currentTime - ((long) pastTime);
        if (secAgo >= 31536000) {
            int date = (int) secAgo / 31536000;
            dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "yr" : "yrs";
        } else if (secAgo >= 2592000) {
            int date = (int) secAgo / 2592000;
            dateString = DOT + String.valueOf(date) + "m";
        } else if (secAgo >= 604800) {
            int date = (int) secAgo / 604800;
            dateString = DOT + String.valueOf(date) + "wk";
        } else if (secAgo >= 86400) {
            int date = (int) secAgo / 86400;
            dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "day" : "days";
        } else if (secAgo >= 3600) {
            int date = (int) secAgo / 3600;
            dateString = DOT + String.valueOf(date);
            dateString += (date == 1) ? "hr" : "hrs";
        } else if (secAgo >= 60) {
            int date = (int) secAgo / 60;
            dateString = DOT + String.valueOf(date) + "min";
        } else {
            dateString = DOT + String.valueOf(secAgo) + "sec";
        }
        return dateString;
    }

    public static void setTabLayoutDivider(Context context, TabLayout tabLayout) {
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        drawable.setSize(4, 1);
        linearLayout.setDividerDrawable(drawable);
    }

    public static int getTabLayoutIndex(String[] array, String value) {
        for(int i=0;i<array.length;i++) {
            if (array[i].equals(value)){
                return i;
            }
        }
        return -1;
    }
    
    public static void loadThumbnail(Context context, Post post, ImageView thumbnail) {
        if(!post.previewUrl.isEmpty() && checkThumbnail(post.thumbnail)){
            String to_remove = "amp;";
            String new_string = post.previewUrl.replace(to_remove, "");
            Glide.with(context)
                    .load(new_string)
                    .thumbnail(Glide.with(context).load(R.drawable.ic_action_picture))
                    .into(thumbnail);
        } else if (!post.thumbnail.isEmpty() && !checkThumbnail(post.thumbnail)) {
            String to_remove = "amp;";
            String new_string = post.thumbnail.replace(to_remove, "");
            Glide.with(context)
                    .load(new_string)
                    .thumbnail(Glide.with(context).load(R.drawable.ic_action_picture))
                    .into(thumbnail);
        } else {
            thumbnail.setImageResource(R.drawable.ic_action_picture);
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


    public static String upsFormatS(int upsInteger) {
        String upsString = "";
        if (upsInteger >= 10000) {
            double upsDouble = upsInteger / 1000d;
            upsString = decimalFormat.format(upsDouble) + KILO;
        } else {
            upsString = String.valueOf(upsInteger);
        }
        return upsString;
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

    public static <T>  T getFragmentInstance(Class<T> fragmentClass, T fragmentInstance){
        if (fragmentInstance == null) {
            try {
                fragmentInstance = fragmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fragmentInstance;
    }

    public static String handleEscapeCharacter( String str ) {
        String[] escapeCharacters = { "&gt;", "&lt;", "&amp;", "&quot;", "&apos;" };
        String[] onReadableCharacter = {">", "<", "&", "\"\"", "'"};
        for (int i = 0; i < escapeCharacters.length; i++) {
            str = str.replace(escapeCharacters[i], onReadableCharacter[i]);
        } return str;
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

    public static void hideKeyboardFrom(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }


}
