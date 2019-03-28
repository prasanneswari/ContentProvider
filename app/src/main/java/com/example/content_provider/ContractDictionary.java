package com.example.content_provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContractDictionary {
    public static final String AUTHORITY = "com.inducesmile.androidcontentprovider.Dictionary";
    public static final String PATH  = "/words";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + PATH);
    public static final String CONTENT_DICTIONARY_LIST = "vnd.android.cursor.dir/vnd.com.inducesmile.androidcontentprovider.words";
    public static final String CONTENT_DICTIONARY_ITEM = "vnd.android.cursor.item/vnd.com.inducesmile.androidcontentprovider.words";
    public static final String DATABASE_NAME = "dictionary";
    public static final int DATABASE_VERSION = 1;
    public static class Dictionary implements BaseColumns {
        private Dictionary(){}
        public static final String TABLE_NAME = "words";
        public static final String ID = "_id";
        public static final String WORD = "word";
        public static final String MEANING = "meaning";
    }
}
