package com.example.aplicativo.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{

	private static final String CREATE_QUERY = "CREATE TABLE teste (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, _index INTEGER NULL, nome TEXT NOT NULL)";
	private static final String DELETE_QUERY = "DROP TABLE teste";

	public static final String TABLE = "teste";
	public static final String FIELD_ID = "_id";
	public static final String FIELD_INDEX = "_index";
	public static final String FIELD_NOME = "nome";
	//public static final String FIELD_DESCRIPTION = "description";

	SQLiteDatabase db;

	Context context;

    @Override
    public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_QUERY);
	    this.db = this.getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
		db.execSQL(DELETE_QUERY);
	    db.execSQL(CREATE_QUERY);
    }

	public DBHelper(Context context, String name, int version){
		super(context, name, null, version);
		this.context = context;
	}
}
