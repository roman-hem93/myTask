package ru.artrostudio.mytask

class DateBaseManager {

    private val tasks : ArrayList<DataTask> = ArrayList<DataTask>()

    fun getTasks () : ArrayList<DataTask> {
        testAddTasks(tasks)

        return tasks
    }

    fun saveTasks (tasks : ArrayList<DataTask>, callback : ()->Unit) {

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
    lateinit var sp : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    val STR_KEY : String = "Name"

    sp = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE)
    editor = sp.edit()

    SAVE
    var text : String? = textInput.text.toString()
    editor.putString(STR_KEY, text).apply()

    GET
    text = sp.getString(STR_KEY, "")

     */
}