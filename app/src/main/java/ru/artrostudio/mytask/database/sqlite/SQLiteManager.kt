package ru.artrostudio.mytask.database.sqlite

import android.content.ContentValues
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.artrostudio.mytask.DataTask
import java.util.Calendar
import java.util.Date

// тут описано взаимодействие с базой данных с точки зрения текущего приложения
// тут реализованы корутины для выполнения обращений к БД в отдельном потоке
class SQLiteManager(context: Context) {

    private val mySQLiteOpenHelper : MySQLiteOpenHelper = MySQLiteOpenHelper(context, StructureBD.SQL_CREATE_ALL_TABLE)

    // просто ссылка на аналогичный метод в исходном классе
    // следует вызывать в активити в методе onDestroy
    fun close() {
        mySQLiteOpenHelper.close()
        Log.i("Developer.BD","Работа с БД завершена")
    }

    // запросить все задачи, кроме помеченных на удаление
    fun getTasks(lambdaOK: (ArrayList<DataTask>)-> Unit, lambdaERROR: ()-> Unit) {
        // lambdaOK - лямбда-функция, выполняемая в случае успеха
        // lambdaERROR - лямбда-функция, выполняемая в случае ошибок

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


        val lambdaOKforBD : (ArrayList<ContentValues>)-> Unit = {result: ArrayList<ContentValues> ->
            // переводим тупую таблицу результатов в нужный нам класс
            // вообще это нужно делать map-ом, но в интернете не нашёл инфы о том, что результат курсора его поддерживает (все что-то там хуевертят костылями, ниже мой)
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

            lambdaOK(tasks)
        }
        val lambdaERRORforBD : ()-> Unit = lambdaERROR

        mySQLiteOpenHelper.query(table, columns, selection, selectionArgs, groupBy, having, sortOrder, lambdaOKforBD, lambdaERRORforBD)
    }


    // добавляет одну новую задачу task
    fun addTask(task: DataTask, lambdaOK: (newId : Long)-> Unit, lambdaERROR: ()-> Unit) {
        // lambdaOK - лямбда-функция, выполняемая в случае успеха
        // lambdaERROR - лямбда-функция, выполняемая в случае ошибок

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

        val lambdaOKforBD : (Long)-> Unit = {newId: Long ->
            Log.i("Developer.BD","В БД сохранена задача с id = $newId")
            lambdaOK(newId)
        }
        val lambdaERRORforBD : ()-> Unit = lambdaERROR

        mySQLiteOpenHelper.insert(table, values, lambdaOKforBD, lambdaERRORforBD)
    }

    // изменяем одну задачу task
    fun saveTask(task: DataTask, lambdaOK: ()-> Unit, lambdaERROR: ()-> Unit) {
        // lambdaOK - лямбда-функция, выполняемая в случае успеха
        // lambdaERROR - лямбда-функция, выполняемая в случае ошибок

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

        val lambdaOKforBD : ()-> Unit = {
            Log.i("Developer.BD","Обновлена в БД задача с id = ${task.id}")
            lambdaOK()
        }
        val lambdaERRORforBD : ()-> Unit = lambdaERROR

        mySQLiteOpenHelper.update(table, values, whereClause, whereArgs, lambdaOKforBD, lambdaERRORforBD)
    }

    // выставляем пометку на удаление для задачи id
    fun deleteTask(id: Long, lambdaOK: ()-> Unit, lambdaERROR: ()-> Unit) {
        // lambdaOK - лямбда-функция, выполняемая в случае успеха
        // lambdaERROR - лямбда-функция, выполняемая в случае ошибок

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

        val lambdaOKforBD : ()-> Unit = {
            Log.i("Developer.BD","Удалили (условно говоря) в БД задачу с id = $id")
            lambdaOK()
        }
        val lambdaERRORforBD : ()-> Unit = lambdaERROR

        mySQLiteOpenHelper.update(table, values, whereClause, whereArgs, lambdaOKforBD, lambdaERRORforBD)


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