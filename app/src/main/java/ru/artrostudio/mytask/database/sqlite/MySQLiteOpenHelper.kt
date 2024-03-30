package ru.artrostudio.mytask.database.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ru.artrostudio.mytask.DataTask


// В этом классе происходит прямое взаимодействие с БД
// тут только самые глобальные общие методы
// в идеале полностью абстрагироваться от текущего приложения
class MySQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, StructureBD.DATABASE_NAME, null, StructureBD.DATABASE_VERSION) {

    // Обязательный метод
    // Вызывается при первом создании базы данных
    // Здесь должно происходить создание таблиц и их первоначальное заполнение
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(StructureBD.SQL_CREATE_TABLE_TASKS)

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
    fun insert(table: String, values: ContentValues) : Long {
        // table - таблица, в которую пишем
        // values данные в виде ключ-значение

        // открывает связь с БД с правами на всё
        val db = this.writableDatabase

        val newRowId = db.insert(table, null, values)

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Выполнена запись в  БД. Таблица: $table, id: $newRowId, данные: $values")

        // возвращаем id новой записи
        return newRowId
    }

    //ЧТЕНИЕ ДАННЫХ
    fun query(table: String, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, groupBy: String?, having: String?, sortOrder: String?) : ArrayList<ContentValues> {
        // table - таблица, в которой роемся
        // columns - возвращаемые столбцы или null чтобы вернуть все (ПРИМЕР: columns = arrayOf("nameColumn", "nameColumn2") или columns = null)
        // selection - столбцы, учитываемые в фильрации WHERE или null чтобы не фильтровать (ПРИМЕР: selection = "nameColumn = ?, nameColumn = ?" )
        // selectionArgs - значения для столбцов WHERE  (ПРИМЕР selectionArgs = arrayOf("5", "10")) // значения подставятся вместо знаков вопроса в selection
        // groupBy - группировка - это что такое?
        // having - фильтрация ГРУПП (т.е. работает в паре с groupBy) или null (ПРИМЕР having = "nameColumn > 5")
        // sortOrder - порядок сортировки или null (ПРИМЕР sortOrder = "nameColumn DESC")

        // открывает связь с БД с правами на чтение (на практике запись и удаление тоже сработали, надо разбираться)
        val db = this.readableDatabase
        val cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder)

        val list = ArrayList<ContentValues>()

        // проходимся по полученным данным
        with(cursor) {
            while (moveToNext()) {
                // из результата получаем массив с названиями столбцов
                val values = ContentValues()

                val columns = cursor.columnNames

                // собираем всю инфу из строки
                for (colum in columns) {
                    values.put(colum, getString(getColumnIndexOrThrow(colum)))
                }
                list.add(values)
            }
        }
        // закрываем курсор
        cursor.close()

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Выполнена вызгрузка из БД. Из таблицы: $table, данные: $list")

        return list
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

    //ПРОИЗВОЛЬНЫЙ ЗАПРОС
    fun requestSQL(request : String) {
        // открывает связь с БД с правами на всё
        val db = this.writableDatabase

        db.execSQL(request)

        // закрываем сессию общения с базой (но для полного отключения нужно закрыть экземпляр SQLiteOpenHelper)
        db.close()

        Log.i("Developer.BD","Выполнен произвольный запрос к БД")
    }

}