package ru.artrostudio.mytask.database.sqlite

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.artrostudio.mytask.DataTask

// тут описано взаимодействие с базой данных с точки зрения текущего приложения
class SQLiteManager(context: Context) {

    val mySQLiteOpenHelper : MySQLiteOpenHelper
    private val tasks : ArrayList<DataTask> = ArrayList<DataTask>()
    init {
        mySQLiteOpenHelper = MySQLiteOpenHelper(context)
    }

    // просто ссылка на аналогичный метод в исходном классе
    // следует вызывать в активити в методе onDestroy
    fun close() {
        mySQLiteOpenHelper.close()
        Log.i("Developer.BD","Работа с БД завершена")
    }

    fun getTasks () : ArrayList<DataTask> {
        testAddTasks(tasks)


        //Log.i("Developer.BD","Достали из файла строку с задачами: $str")

        return tasks
    }

    fun saveTasks (array : ArrayList<DataTask>) {

        //Log.i("Developer.BD","Сохраняем в БД задачи: $str")

    }

    private fun testAddTasks (array : ArrayList<DataTask>) {
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