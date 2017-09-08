package com.qartf.doseforreddit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.MainActivity;
import com.qartf.doseforreddit.database.DatabaseContract.Accounts;
import com.qartf.doseforreddit.model.AccessToken;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Doseforreddit.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Accounts.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void insertAccount(MainActivity mainActivity, String userName, AccessToken accessToken) {
        ContentValues values = new ContentValues();
        values.put(Accounts.USER_NAME, userName);
        String jsonString = new Gson().toJson(accessToken);
        values.put(Accounts.ACCESS_TOKEN, jsonString);
        Uri newUri = mainActivity.getContentResolver().insert(Accounts.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.editor_insert_movie_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.editor_insert_movie_successful), Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateAccount(MainActivity mainActivity, String userName, AccessToken accessToken) {
        String jsonString = new Gson().toJson(accessToken);
        ContentValues values = new ContentValues();
        values.put(Accounts.ACCESS_TOKEN, jsonString);

        String selection = DatabaseContract.Accounts.USER_NAME + "=?";
        String[] selectionArgs = new String[]{userName};
        int rowsUpdated = mainActivity.getContentResolver().update(Accounts.CONTENT_URI, values, selection, selectionArgs);

        if (rowsUpdated == 0) {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.editor_insert_movie_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mainActivity, mainActivity.getString(R.string.editor_insert_movie_successful), Toast.LENGTH_SHORT).show();
        }
    }

}
