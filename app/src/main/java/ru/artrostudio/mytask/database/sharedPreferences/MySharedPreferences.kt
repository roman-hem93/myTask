package ru.artrostudio.mytask.database.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.artrostudio.mytask.DataTask

class MySharedPreferences(_context: Context) {

    private val context : Context = _context
    private val sp = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sp.edit()

    private val STR_KEY : String = "BD_TASKS"
    private val tasks : ArrayList<DataTask> = ArrayList<DataTask>()

    fun getTasks () : ArrayList<DataTask> {
        //testAddTasks(tasks)

        // запрашиваем из текстового файла JSON строку
        val str = sp.getString(STR_KEY, "[]")
        // переводим строку в список
        Log.i("Developer.BD","Достали из файла строку с задачами: $str")
        val array = Json.decodeFromString<ArrayList<DataTask>>(str.toString())
        return array
    }

    fun saveTasks (array : ArrayList<DataTask>) {
        // преобразуем полученный список в JSON

        val str = Json.encodeToString(array)
        // сохраняем строку в текстовый файл
        Log.i("Developer.BD","Сохраняем в БД задачи: $str")
        editor.putString(STR_KEY, str).apply()
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