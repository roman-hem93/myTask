package ru.artrostudio.mytask.database.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Константы с самыми глобальными запросами на создание структуры БД и удаления БД
private const val SQL_CREATE_TABLE_TASKS =
    "CREATE TABLE ${StructureBD.tableTasks.TABLE_NAME} (" +
            "${StructureBD.allTable.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${StructureBD.tableTasks.COLUMN_NAME_TITLE} TEXT," +
            "${StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION} TEXT)"

private const val SQL_DELETE_TABLE_TASKS = "DROP TABLE IF EXISTS ${StructureBD.tableTasks.TABLE_NAME}"

class SQLiteManager(context: Context) : SQLiteOpenHelper(context, StructureBD.DATABASE_NAME, null, StructureBD.DATABASE_VERSION) {

    // Обязательный метод
    // Вызывается при первом создании базы данных
    // Здесь должно происходить создание таблиц и их первоначальное заполнение
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_TASKS)

        Log.i("Developer.BD","Выполнился метод onCreate SQLite (создание бд)")
    }

    // Обязательный метод
    //Вызывается, когда базу данных необходимо обновить (для удаления таблиц, добавления таблиц, изменения количества столбцов)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //удаление таблицы
        //db.execSQL(SQL_DELETE_TABLE_TASKS)

        //создание новой пустой таблицы
        //onCreate(db)

        Log.i("Developer.BD","Выполнился метод onUpgrade SQLite (обновление версии бд)")
    }
}