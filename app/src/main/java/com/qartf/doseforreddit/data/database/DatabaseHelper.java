package com.qartf.doseforreddit.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.google.gson.Gson;
import com.qartf.doseforreddit.data.entity.AccessToken;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Doseforreddit.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Accounts.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Uri insertAccount(Context mainActivity, String userName, AccessToken accessToken) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Accounts.USER_NAME, userName);
        String jsonString = new Gson().toJson(accessToken);
        values.put(DatabaseContract.Accounts.ACCESS_TOKEN, jsonString);
        return mainActivity.getContentResolver().insert(DatabaseContract.Accounts.CONTENT_URI, values);
    }

    public static int updateAccount(Context mainActivity, String userName, AccessToken accessToken) {
        String jsonString = new Gson().toJson(accessToken);
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Accounts.ACCESS_TOKEN, jsonString);

        String selection = DatabaseContract.Accounts.USER_NAME + "=?";
        String[] selectionArgs = new String[]{userName};
        return mainActivity.getContentResolver().update(DatabaseContract.Accounts.CONTENT_URI, values, selection, selectionArgs);
    }

    public static Cursor updateUser(Context context, String userName){
        String selection = DatabaseContract.Accounts.USER_NAME + "=?";
        String[] selectionArgs = new String[]{userName};
        Cursor cursor = context.getContentResolver().query(DatabaseContract.Accounts.CONTENT_URI,
                DatabaseContract.Accounts.PROJECTION_LIST,
                selection, selectionArgs, null);
        return cursor;
    }

    public static Cursor getUsers(Context context){
        Cursor cursor = context.getContentResolver().query(DatabaseContract.Accounts.CONTENT_URI,
                DatabaseContract.Accounts.PROJECTION_LIST,null, null, null);
        return cursor;
    }

}
