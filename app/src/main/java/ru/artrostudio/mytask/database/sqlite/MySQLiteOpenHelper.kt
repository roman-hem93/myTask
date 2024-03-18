package ru.artrostudio.mytask.database.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Константы на создание структуры БД
// создание таблицы с задачами
private const val SQL_CREATE_TABLE_TASKS =
    "CREATE TABLE ${StructureBD.tableTasks.TABLE_NAME} (" +
            "${StructureBD.allTable.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${StructureBD.tableTasks.COLUMN_NAME_TITLE} TEXT," +
            "${StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION} TEXT)"

// В этом классе происходит прямое взаимодействие с БД
// тут только самые глобальные общие методы
class MySQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, StructureBD.DATABASE_NAME, null, StructureBD.DATABASE_VERSION) {

    // Обязательный метод
    // Вызывается при первом создании базы данных
    // Здесь должно происходить создание таблиц и их первоначальное заполнение
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_TASKS)

        Log.i("Developer.BD","Выполнился метод onCreate SQLite (создание бд)")
    }

    // Обязательный метод
    // Вызывается, когда базу данных необходимо обновить (для удаления таблиц, добавления таблиц, изменения количества столбцов, а в целом нафиг нужно)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //удаление таблицы
        //db.execSQL("DROP TABLE IF EXISTS название удаляемой таблицы")

        //создание новой пустой таблицы
        //onCreate(db)

        Log.i("Developer.BD","Выполнился метод onUpgrade SQLite (обновление версии бд)")
    }

    // ниже самописные методы

    //ЗАПИСЬ В ТАБЛИЦУ
    fun insert() {
        // открывает связь с БД с правами на всё
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(StructureBD.tableTasks.COLUMN_NAME_TITLE, "Заголовок")
            put(StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION, "Описание")
        }

        val newRowId = db?.insert(StructureBD.tableTasks.TABLE_NAME, null, values)

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Запись в  БД: $newRowId")
    }

    //ЧТЕНИЕ ДАННЫХ
    fun query() {
        // открывает связь с БД с правами на чтение (на практике запись и удаление тоже сработали)
        val db = this.readableDatabase

        // таблица, в которой роемся
        val table = StructureBD.tableTasks.TABLE_NAME
        // возвращаемые столбцы или null чтобы вернуть все
        val projection = arrayOf(StructureBD.allTable.COLUMN_NAME_ID, StructureBD.tableTasks.COLUMN_NAME_TITLE, StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION)
        // столбцы, учитываемые в фильрации WHERE
        val selection = "${StructureBD.tableTasks.COLUMN_NAME_TITLE} = ?"
        // значения для столбцов WHERE
        val selectionArgs = arrayOf("My Title")
        // это что такое?
        val groupBy = null
        // это что такое?
        val having = null
        // порядок сортировки
        val sortOrder = "${StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION} DESC"
        val cursor = db.query(table, null, null, null, groupBy, having, sortOrder)

        // проходимся по полученным данным
        val itemIds = mutableListOf<Long>()
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(StructureBD.allTable.COLUMN_NAME_ID))
                itemIds.add(itemId)
            }
        }
        cursor.close()

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Вытянули из БД: $itemIds")
    }

    // УДАЛЕНИЕ ДАННЫХ
    fun delete() {
        // открывает связь с БД с правами на всё
        val db = this.writableDatabase

        // таблица, в которой удаляем
        val table_del = StructureBD.tableTasks.TABLE_NAME
        // столбцы, учитываемые в фильрации WHERE
        val selection_del = "${StructureBD.allTable.COLUMN_NAME_ID} = ? OR ${StructureBD.allTable.COLUMN_NAME_ID} = ?"
        // значения для столбцов WHERE
        val selectionArgs_del = arrayOf("7","8")
        // выполняем запрос удаления элементов из БД (возвращает количество удалённых строк)
        val deletedRows = db.delete(table_del, selection_del, selectionArgs_del)

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Удалили из БД столько строк: $deletedRows")
    }

}