package ru.artrostudio.mytask.database.sqlite

import android.content.ContentValues
import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.artrostudio.mytask.DataTask
import java.util.Calendar
import java.util.Date

// тут описано взаимодействие с базой данных с точки зрения текущего приложения
class SQLiteManager(context: Context) {

    private val mySQLiteOpenHelper : MySQLiteOpenHelper = MySQLiteOpenHelper(context)


    // просто ссылка на аналогичный метод в исходном классе
    // следует вызывать в активити в методе onDestroy
    fun close() {
        mySQLiteOpenHelper.close()
        Log.i("Developer.BD","Работа с БД завершена")
    }

    // запросить все неудаленные задачи
    fun getTasks() : ArrayList<DataTask> {

        //testAddTasks(tasks)

        // описания в самом методе query
        val table : String = StructureBD.tableTasks.TABLE_NAME
        val columns = arrayOf(
                StructureBD.allTable.COLUMN_NAME_ID,
                StructureBD.tableTasks.COLUMN_NAME_TITLE,
                StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION,
                StructureBD.tableTasks.COLUMN_NAME_DATE,
                StructureBD.tableTasks.COLUMN_NAME_STATUS
            )
        val selection = "${StructureBD.allTable.COLUMN_NAME_STATUS_ITEM} <> ?"
        val selectionArgs = arrayOf("0")
        val groupBy = null
        val having = null
        val sortOrder = "${StructureBD.allTable.COLUMN_NAME_ID}"


        val result : ArrayList<ContentValues> = mySQLiteOpenHelper.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder)

        // переводим тупую таблицу результатов в нужный нам класс
        // вообще это нужно делать map-ом, но в интернете не нашёл инфы о том, что результат курсора его поддерживаем (все что-то там хуевертят костылями)
        val tasks = ArrayList<DataTask>()
        for (row in result) {
            val task = DataTask(
                id = row.getAsLong(StructureBD.allTable.COLUMN_NAME_ID),
                title = row.getAsString(StructureBD.tableTasks.COLUMN_NAME_TITLE),
                description = row.getAsString(StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION),
                date = row.getAsString(StructureBD.tableTasks.COLUMN_NAME_DATE),
                status = row.getAsInteger(StructureBD.tableTasks.COLUMN_NAME_STATUS)
            )
            tasks.add(task)
        }

        Log.i("Developer.BD","Результат из БД разобран, получены данные: $tasks")

        return tasks
    }



    fun addTask(task: DataTask) {

        // описания в самом методе insert
        val table = StructureBD.tableTasks.TABLE_NAME

        val values = ContentValues()
        // основная информация
        values.put(StructureBD.tableTasks.COLUMN_NAME_TITLE, task.title)
        values.put(StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION, task.description)
        values.put(StructureBD.tableTasks.COLUMN_NAME_DATE, task.date)
        values.put(StructureBD.tableTasks.COLUMN_NAME_STATUS, task.status)
        // дополняем данные системной информацией
        val dateNow = Date() // текущая дата
        values.put(StructureBD.allTable.COLUMN_NAME_STATUS_ITEM, 1)
        values.put(StructureBD.allTable.COLUMN_NAME_DATE_CREATION, dateNow.time)
        values.put(StructureBD.allTable.COLUMN_NAME_DATE_CHANGE, 0)
        values.put(StructureBD.allTable.COLUMN_NAME_DATE_DELETION, 0)

        val id = mySQLiteOpenHelper.insert(table, values)

        Log.i("Developer.BD","В БД сохранена задача с id = $id")
    }

    fun saveTask(task: DataTask) {

        // описания в самом методе update
        val table = StructureBD.tableTasks.TABLE_NAME

        val values = ContentValues()
        // основная информация
        values.put(StructureBD.tableTasks.COLUMN_NAME_TITLE, task.title)
        values.put(StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION, task.description)
        values.put(StructureBD.tableTasks.COLUMN_NAME_DATE, task.date)
        values.put(StructureBD.tableTasks.COLUMN_NAME_STATUS, task.status)
        // дополняем данные системной информацией
        val dateNow = Date() // текущая дата
        values.put(StructureBD.allTable.COLUMN_NAME_DATE_CHANGE, dateNow.time)

        val whereClause = "${StructureBD.allTable.COLUMN_NAME_ID} = ${task.id}"
        val whereArgs = null

        val updatedRows = mySQLiteOpenHelper.update(table, values, whereClause, whereArgs)

        Log.i("Developer.BD","Обновили в БД задачу с id = ${task.id}")

    }

    fun deleteTask(id: Long) {
        // под удалением подразумевается установка статуса COLUMN_NAME_STATUS_ITEM = 0
        // жестокое удаление информации из БД не приветствуется

        // описания в самом методе update
        val table = StructureBD.tableTasks.TABLE_NAME

        val values = ContentValues()
        // основная информация
        // --- НЕ МЕНЯЕТСЯ при удалении

        // дополняем данные системной информацией
        val dateNow = Date() // текущая дата
        values.put(StructureBD.allTable.COLUMN_NAME_STATUS_ITEM, 0)
        values.put(StructureBD.allTable.COLUMN_NAME_DATE_DELETION, dateNow.time)

        val whereClause = "${StructureBD.allTable.COLUMN_NAME_ID} = $id"
        val whereArgs = null

        val updatedRows = mySQLiteOpenHelper.update(table, values, whereClause, whereArgs)

        Log.i("Developer.BD","Удалили (условно говоря) в БД задачу с id = $id")
    }

    // выводит в протокол таблицу tasks из БД
    fun printTableTasks() {
        // описания в самом методе printTable
        val table = StructureBD.tableTasks.TABLE_NAME
        mySQLiteOpenHelper.printTable(table)
    }

    private fun testAddTasks(array : ArrayList<DataTask>) {
        val task1 : DataTask = DataTask(0,"Заголовок 1", "Описание", "дата текстом", 1)
        val task2 : DataTask = DataTask(1,"Заголовок 2", "Описание", "дата текстом", 0)
        val task3 : DataTask = DataTask(2,"Заголовок 3", "Описание", "дата текстом", 1)
        val task4 : DataTask = DataTask(3,"Заголовок 4", "Описание", "дата текстом", 0)

        array.add(task1)
        array.add(task2)
        array.add(task3)
        array.add(task4)
    }

}