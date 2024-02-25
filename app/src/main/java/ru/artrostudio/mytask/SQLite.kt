//package ru.artrostudio.mytask
//
//import android.database.sqlite.SQLiteOpenHelper
//
//class SQLite() : SQLiteOpenHelper {
//    private val DATABASE_NAME : String = "userstore.db" // название бд
//    private val SCHEMA : Int = 1 // версия базы данных
//    val TABLE : String = "users" // название таблицы в бд
//    // названия столбцов
//    val COLUMN_ID : String = "_id";
//    val COLUMN_NAME : String = "name";
//    val COLUMN_YEAR : String = "year";
//
//    /*public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, SCHEMA);
//    }*/
//
//
//    fun onCreate(SQLiteDatabase db) {
//
//        db.execSQL("CREATE TABLE users (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
//                + " TEXT, " + COLUMN_YEAR + " INTEGER);");
//        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
//                + ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
//    }
//
//    fun onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
//        onCreate(db);
//    }
//}