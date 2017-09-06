package com.qartf.doseforreddit.utility;

import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ART_F on 2017-09-05.
 */

public class Utility {
    public static final String DOT = "\u2022";

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
}
