package com.example.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

public class DictionaryProvider extends ContentProvider {

    private SqliteDatabaseManager dbManager;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(ContractDictionary.AUTHORITY, ContractDictionary.PATH, 1);
        sUriMatcher.addURI(ContractDictionary.AUTHORITY, ContractDictionary.PATH + "/#", 2);
    }
    @Override
    public boolean onCreate() {
        dbManager = new SqliteDatabaseManager(getContext());
        return false;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projections, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor mCursor = null;
        switch (sUriMatcher.match(uri)){
            case 1:
                mCursor = db.query(ContractDictionary.Dictionary.TABLE_NAME, projections, selection, selectionArgs, null, null, null);
                break;
            case 2:
                selection = selection + ContractDictionary.Dictionary.ID + " = " + uri.getLastPathSegment();
                mCursor = db.query(ContractDictionary.Dictionary.TABLE_NAME, projections, selection, selectionArgs, null, null, null);
                break;
            default:
                Toast.makeText(getContext(), "Invalid content uri", Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case 1:
                return ContractDictionary.CONTENT_DICTIONARY_LIST;
            case 2:
                return ContractDictionary.CONTENT_DICTIONARY_ITEM;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        if(sUriMatcher.match(uri) != 1) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        long rowId = db.insert(ContractDictionary.Dictionary.TABLE_NAME, null, contentValues);
        if(rowId > 0) {
            Uri articleUri = ContentUris.withAppendedId(ContractDictionary.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(articleUri, null);
            return articleUri;
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        int count = 0;
        switch(sUriMatcher.match(uri)) {
            case 1:
                count = db.delete(ContractDictionary.Dictionary.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(ContractDictionary.Dictionary.TABLE_NAME, ContractDictionary.Dictionary.ID + " = " + rowId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = db.update(ContractDictionary.Dictionary.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case 2:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(ContractDictionary.Dictionary.TABLE_NAME, contentValues, ContractDictionary.Dictionary.ID + " = " + rowId +
                        (!TextUtils.isEmpty(selection) ? " AND (" + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
