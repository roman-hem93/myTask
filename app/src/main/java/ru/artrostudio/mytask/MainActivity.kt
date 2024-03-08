package ru.artrostudio.mytask


import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.artrostudio.mytask.database.DataBaseManager
import ru.artrostudio.mytask.database.sqlite.SQLiteManager
import ru.artrostudio.mytask.database.sqlite.StructureBD

class MainActivity : AppCompatActivity(), Animation.AnimationListener {

    //lateinit var context: MainActivity
    lateinit var tasks: ArrayList<DataTask>
    lateinit var dbManager: DataBaseManager

    lateinit var animationAlphaIn : Animation
    lateinit var animationAlphaOut : Animation
    lateinit var animationCategoriesIn : Animation
    lateinit var animationCategoriesOut : Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("Developer.System","Приложение запустилось")

        //окна
        //context = this
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val windowSetDate : ConstraintLayout = findViewById(R.id.windowSetDate)
        val windowSetTime : ConstraintLayout = findViewById(R.id.windowSetTime)
        val windowSetting : ConstraintLayout = findViewById(R.id.windowSetting)
        val windowCategories : ConstraintLayout = findViewById(R.id.windowCategories)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val windowGrayBackground : ConstraintLayout = findViewById(R.id.windowGreyBackground)


        //val ltCategoriesList : ConstraintLayout = findViewById(R.id.ltCategoriesList)

        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)

        val buttonTasksAdd : Button = findViewById(R.id.tasksAdd)
        val buttonAddTaskSave : Button = findViewById(R.id.addTaskSave)
        val buttonAddTaskDelete : Button = findViewById(R.id.addTaskDelete)
        val buttonAddTaskCancel : Button = findViewById(R.id.addTaskCancel)
        val buttonSetDateCancel : Button = findViewById(R.id.setDateCancel)
        val buttonSetDateSetTime : Button = findViewById(R.id.setDateSetTime)
        val buttonSetTimeCancel : Button = findViewById(R.id.setTimeCancel)
        val buttonTasksDate : Button = findViewById(R.id.tasksDate)
        val buttonTasksNotification : Button = findViewById(R.id.tasksNotification)
        val buttonCatigoriesTasksItem : ImageView = findViewById(R.id.buttonCatigoriesTasksItem)

        //анимация
        //animationAlphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in)
        //animationAlphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out)
        //animationCategoriesIn = AnimationUtils.loadAnimation(this, R.anim.move_categories_in)
        //animationCategoriesOut = AnimationUtils.loadAnimation(this, R.anim.move_categories_out)
        //animationCategoriesIn.setAnimationListener(this)



        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        dbManager = DataBaseManager(this)
        tasks = dbManager.getTasks()

        val notifications : Notifications = Notifications(this)



        buttonCatigoriesTasksItem.setOnClickListener() {
            //windowCategories.visibility = View.VISIBLE
            windowGrayBackground.visibility = View.VISIBLE
            windowCategories.x = 0.0f
            //windowGrayBackground.startAnimation(animationAlphaIn)
            //windowTasks.visibility = View.GONE
            //bottomMenu.visibility = View.GONE

            //windowCategories.startAnimation(animationCategoriesIn)

        }

        windowGrayBackground.setOnClickListener() {
            //windowTasks.visibility = View.VISIBLE
            //bottomMenu.visibility = View.VISIBLE
            //windowCategories.startAnimation(animationCategoriesOut)
            windowGrayBackground.visibility = View.GONE
            windowCategories.x = -windowCategories.width.toFloat()
        }

        windowCategories.setOnClickListener() {
            myAlert("Тест")
        }









        // попытки сделать что-то с SQLite, пока нафиг
        // перенеси потом в DataBaseManager

        val str: String = StructureBD.tableTasks.TABLE_NAME

        val dbHelper = SQLiteManager(this)

        // запись данных
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(StructureBD.tableTasks.COLUMN_NAME_TITLE, "Заголовок")
            put(StructureBD.tableTasks.COLUMN_NAME_DESCRIPTION, "Описание")
        }

        val newRowId = db?.insert(StructureBD.tableTasks.TABLE_NAME, null, values)

        Log.i("Developer.BD","Запись в  БД: $newRowId")


        // запрос данных
        val db2 = dbHelper.readableDatabase

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
        Log.i("Developer.BD","Вытянули из БД: $itemIds")


        // удаление

        val db3 = dbHelper.readableDatabase
        // таблица, в которой удаляем
        val table_del = StructureBD.tableTasks.TABLE_NAME
        // столбцы, учитываемые в фильрации WHERE
        val selection_del = "${StructureBD.allTable.COLUMN_NAME_ID} = ? OR ${StructureBD.allTable.COLUMN_NAME_ID} = ?"
        // значения для столбцов WHERE
        val selectionArgs_del = arrayOf("5","6")
        // Issue SQL statement.
        val deletedRows = db3.delete(table_del, selection_del, selectionArgs_del)
        Log.i("Developer.BD","Удалили из БД столько строк: $deletedRows")

        // !!!!!!!!!!!!!!! фоновым потоком

        // это нужно завернуть в onDestroy() активити
        dbHelper.close()
        //super.onDestroy()




        // потоки

        // этот вариант подвешивает основной поток
        // отсюда книжка
        // https://kotlinlang.org/docs/coroutines-basics.html#your-first-coroutine
        fun main() = runBlocking { // this: CoroutineScope
            launch { // launch a new coroutine and continue
                delay(2000L) // non-blocking delay for 1 second (default time unit is ms)
                Log.i("Developer.test","Word")
            }
            Log.i("Developer.test","Hello")
        }
        main()

        // вариант покороче
        // вообще прикольно тут расписано:
        // https://ru.stackoverflow.com/questions/392126/%D0%9A%D0%B0%D0%BA-%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D1%82%D1%8C-10-%D1%81%D0%B5%D0%BA%D1%83%D0%BD%D0%B4%D0%BD%D1%83%D1%8E-%D0%B7%D0%B0%D0%B4%D0%B5%D1%80%D0%B6%D0%BA%D1%83
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L)
            Log.i("Developer.test","Wordddd")
        }



        // определяем и настраиваем RecyclerView
        val adapter = TasksRecyclerViewAdapter(this as Context, tasks, {id: Int -> openTask(id)}, {id: Int -> setStatus(id)})
        rvTasks.hasFixedSize()
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        if (tasks.size == 0) {
            // выполняем вывод инфы об отсутствии задач
            Toast.makeText(this, "Нет задач",Toast.LENGTH_LONG).show()
        }


        buttonTasksAdd.setOnClickListener() {
            editTextAddTaskDate.setText("")
            editTextAddTaskTitle.setText("")
            editTextAddTaskMessage.setText("")

            buttonAddTaskSave.setOnClickListener() {
                saveTask(-1)
            }
            buttonAddTaskDelete.visibility = View.GONE

            windowAddTask.visibility = View.VISIBLE
            windowTasks.visibility = View.GONE
            bottomMenu.visibility = View.GONE
        }


        buttonAddTaskCancel.setOnClickListener() {
            windowTasks.visibility = View.VISIBLE
            bottomMenu.visibility = View.VISIBLE
            windowAddTask.visibility = View.GONE
        }


        buttonSetDateCancel.setOnClickListener() {
            windowTasks.visibility = View.VISIBLE
            bottomMenu.visibility = View.VISIBLE
            windowSetDate.visibility = View.GONE
        }


        buttonSetDateSetTime.setOnClickListener() {
            windowSetTime.visibility = View.VISIBLE
            windowSetDate.visibility = View.GONE
        }


        buttonSetTimeCancel.setOnClickListener() {
            windowSetDate.visibility = View.VISIBLE
            windowSetTime.visibility = View.GONE
        }


        buttonTasksDate.setOnClickListener() {
            val dateView = findViewById<DatePicker>(R.id.datePicker)
            val timeView = findViewById<TimePicker>(R.id.timePicker)

            timeView.setIs24HourView(true)
            //dateView.
            //dateView.month =
            //dateView.year =

            windowTasks.visibility = View.GONE
            bottomMenu.visibility = View.GONE
            windowSetDate.visibility = View.VISIBLE
        }


        buttonTasksNotification.setOnClickListener() {
            notifications.setNotification()
            Toast.makeText(this, "Уведомление создано",Toast.LENGTH_LONG).show()
        }


    }

    fun openTask(id: Int) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)
        val buttonAddTaskSave : Button = findViewById(R.id.addTaskSave)
        val buttonAddTaskDelete : Button = findViewById(R.id.addTaskDelete)


        buttonAddTaskSave.setOnClickListener() {
            saveTask(id)
        }
        buttonAddTaskDelete.visibility = View.VISIBLE
        buttonAddTaskDelete.setOnClickListener() {
            deleteTask(id)
        }

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {
                editTextAddTaskDate.setText(item.date)
                editTextAddTaskTitle.setText(item.title)
                editTextAddTaskMessage.setText(item.message)
            }
        }

        windowAddTask.visibility = View.VISIBLE
        windowTasks.visibility = View.GONE
        bottomMenu.visibility = View.GONE
    }

    fun setStatus(id: Int) {

        var newStatus = 0
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {
                val oldSt = item.status
                if (oldSt == 0) newStatus = 1
                item.status = newStatus
            }
        }

        dbManager.saveTasks(tasks)

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()
    }

    fun saveTask(id: Int) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        if (id == -1) { // новая напоминалка

            // определяем наибольший id в базе
            var idNew = 0
            for(item in tasks) {
                if (item.id >= idNew) idNew = item.id + 1
            }

            tasks.add(
                DataTask(
                    idNew,
                    editTextAddTaskTitle.text.toString(),
                    editTextAddTaskMessage.text.toString(),
                    editTextAddTaskDate.text.toString(),
                    0
                )
            )
        } else { // редактируем напоминалку id
            for(item in tasks) {
                if (item.id == id) {
                    item.title = editTextAddTaskTitle.text.toString()
                    item.message = editTextAddTaskMessage.text.toString()
                    item.date = editTextAddTaskDate.text.toString()
                }
            }
        }

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()

        dbManager.saveTasks(tasks)

        windowTasks.visibility = View.VISIBLE
        bottomMenu.visibility = View.VISIBLE
        windowAddTask.visibility = View.GONE
    }

    fun deleteTask(id: Int) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {
                tasks.remove(item)
                break
            }
        }

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()

        dbManager.saveTasks(tasks)

        windowTasks.visibility = View.VISIBLE
        bottomMenu.visibility = View.VISIBLE
        windowAddTask.visibility = View.GONE
    }

    fun myAlert(text : String) {
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
    }

    override fun onAnimationStart(p0: Animation?) {

    }

    override fun onAnimationEnd(p0: Animation?) {
        //val windowCategories : ConstraintLayout = findViewById(R.id.windowCategories)
        //windowCategories.x = 0.0f
        //myAlert("dfghdfg")
    }

    override fun onAnimationRepeat(p0: Animation?) {

    }


}

