package com.example.tela_inicial;

import android.database.sqlite.SQLiteDatabase;

public class DbService {
    SQLiteDatabase db;

    public DbService( ) {
        this.db = SQLiteDatabase.openDatabase("/data/data/com.example.tela_inicial", null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
    }
}
