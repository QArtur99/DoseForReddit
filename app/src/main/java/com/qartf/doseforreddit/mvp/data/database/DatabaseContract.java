package com.qartf.doseforreddit.mvp.data.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class DatabaseContract {

    public static final String CONTENT_AUTHORITY = "com.qartf.doseforreddit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ACCOUNTS = "accounts";

    private DatabaseContract() {
    }

    public static class Accounts implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACCOUNTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNTS;


        public static final String TABLE_NAME = "Accounts";
        public static final String USER_NAME = "userName";
        public static final String ACCESS_TOKEN = "accessToken";

        public static final String[] PROJECTION_LIST = {
                _ID,
                USER_NAME,
                ACCESS_TOKEN
        };

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + USER_NAME + " TEXT,"
                        + ACCESS_TOKEN + " TEXT"
                        + ");";

        static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
