package com.qartf.doseforreddit.mvp.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.qartf.doseforreddit.mvp.data.database.DatabaseContract.Accounts;

public class DatabaseProvider extends ContentProvider {

    public static final String LOG_TAG = DatabaseProvider.class.getSimpleName();
    private static final int ACCESS_TOKEN = 100;
    private static final int USER_NAME = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_ACCOUNTS, ACCESS_TOKEN);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_ACCOUNTS + "/#", USER_NAME);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCESS_TOKEN:
                cursor = database.query(Accounts.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_NAME:
                selection = Accounts._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Accounts.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCESS_TOKEN:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        String movieId = values.getAsString(Accounts.USER_NAME);
        if (movieId == null) {
            throw new IllegalArgumentException("Movie id can not be empty");
        }

        String name = values.getAsString(Accounts.ACCESS_TOKEN);
        if (name == null) {
            throw new IllegalArgumentException("Movie object can not be empty");
        }

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long id = database.insert(Accounts.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCESS_TOKEN:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case USER_NAME:
                selection = Accounts._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(Accounts.USER_NAME)) {
            String movieId = values.getAsString(Accounts.USER_NAME);
            if (movieId == null) {
                throw new IllegalArgumentException("Movie id can not be empty");
            }
        }

        if (values.containsKey(Accounts.ACCESS_TOKEN)) {
            String name = values.getAsString(Accounts.ACCESS_TOKEN);
            if (name == null) {
                throw new IllegalArgumentException("Movie object can not be empty");
            }
        }

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated = database.update(Accounts.TABLE_NAME, values, selection, selectionArgs);
        databaseHelper.close();

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCESS_TOKEN:
                rowsDeleted = database.delete(Accounts.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_NAME:
                selection = Accounts._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Accounts.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCESS_TOKEN:
                return Accounts.CONTENT_LIST_TYPE;
            case USER_NAME:
                return Accounts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
