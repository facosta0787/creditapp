package com.creditapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.creditapp.db.dbManager;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dbCredit.sqlite";
    private static final int DB_VERSION = 1;

    public dbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(dbManager.tUsuarios);
        db.execSQL(clienteModel.tClientes);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
