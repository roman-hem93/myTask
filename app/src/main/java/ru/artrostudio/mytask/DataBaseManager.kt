package ru.artrostudio.mytask

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class DateBaseManager(_context : Context) {

    private val context : Context = _context
    private val sp = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sp.edit()

    private val STR_KEY : String = "BD_TASKS"
    private val tasks : ArrayList<DataTask> = ArrayList<DataTask>()

    fun getTasks () : ArrayList<DataTask> {
        testAddTasks(tasks)

        return tasks
    }

    fun saveTasks (tasks : ArrayList<DataTask>, callback : ()->Unit) {

        Log.d("myTask.BD",tasks.toString())

        //var text : String? = textInput.text.toString()
        //editor.putString(STR_KEY, text).apply()
    }


    private fun testAddTasks (tasks : ArrayList<DataTask>) {
        val task1 : DataTask = DataTask("Заголовок 1", "Описание", "дата текстом", 1)
        val task2 : DataTask = DataTask("Заголовок 2", "Описание", "дата текстом", 0)
        val task3 : DataTask = DataTask("Заголовок 3", "Описание", "дата текстом", 1)
        val task4 : DataTask = DataTask("Заголовок 4", "Описание", "дата текстом", 0)

        tasks.add(task1)
        tasks.add(task2)
        tasks.add(task3)
        tasks.add(task4)
    }


    /*


    SAVE
    var text : String? = textInput.text.toString()
    editor.putString(STR_KEY, text).apply()

    GET
    text = sp.getString(STR_KEY, "")

     */
}