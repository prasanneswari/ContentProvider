package com.example.content_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDatabaseManager extends SQLiteOpenHelper {

    public SqliteDatabaseManager(Context context) {
        super(context, ContractDictionary.DATABASE_NAME, null, ContractDictionary.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + ContractDictionary.Dictionary.TABLE_NAME + "(" + ContractDictionary.Dictionary.ID + " INTEGER PRIMARY KEY,"
                + ContractDictionary.Dictionary.WORD + " TEXT," + ContractDictionary.Dictionary.MEANING + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractDictionary.Dictionary.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
