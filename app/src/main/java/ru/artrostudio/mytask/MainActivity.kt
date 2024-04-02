package ru.artrostudio.mytask


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
import kotlinx.coroutines.delay
import ru.artrostudio.mytask.database.sqlite.MySQLiteOpenHelper
import ru.artrostudio.mytask.database.sqlite.SQLiteManager
import ru.artrostudio.mytask.modules.MyAnimation
import ru.artrostudio.mytask.modules.MyNotifications

class MainActivity : AppCompatActivity() {

    //lateinit var context: MainActivity
    lateinit var tasks: ArrayList<DataTask>
    lateinit var dbManager: SQLiteManager


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

        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        dbManager = SQLiteManager(this)
        tasks = dbManager.getTasks()
        //tasks = ArrayList<DataTask>()

        dbManager.printTableTasks()

        val notifications : MyNotifications = MyNotifications(this)

        buttonCatigoriesTasksItem.setOnClickListener() {
            windowCategories.visibility = View.VISIBLE
            windowGrayBackground.visibility = View.VISIBLE
            windowGrayBackground.alpha = 0f // задаю тут, т.к. объект include в xml не поддерживает свойство alpa
            MyAnimation.start(windowCategories, MyAnimation.ATTRIBUTE_X, false, 9999f, 0f,120L, MyAnimation.TYPE_EVENLY)
            MyAnimation.start(windowGrayBackground, MyAnimation.ATTRIBUTE_ALPHA, false, 9999f, 1f,120L, MyAnimation.TYPE_EVENLY)
        }

        windowGrayBackground.setOnClickListener() {
            MyAnimation.start(windowCategories, MyAnimation.ATTRIBUTE_X, false, 9999f, -windowCategories.width.toFloat(),120L, MyAnimation.TYPE_EVENLY, {windowCategories.visibility = View.GONE})
            MyAnimation.start(windowGrayBackground, MyAnimation.ATTRIBUTE_ALPHA, false, 9999f, 0f,120L, MyAnimation.TYPE_EVENLY,{windowGrayBackground.visibility = View.GONE})
        }

        windowCategories.setOnClickListener() {
            myAlert("Тест")
        }
























        // определяем и настраиваем RecyclerView
        val adapter = TasksRecyclerViewAdapter(this as Context, tasks, {id: Long -> openTask(id)}, {id: Long -> setStatus(id)})
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

    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }

    fun openTask(id: Long) {
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
                editTextAddTaskMessage.setText(item.description)
            }
        }

        windowAddTask.visibility = View.VISIBLE
        windowTasks.visibility = View.GONE
        bottomMenu.visibility = View.GONE
    }

    fun setStatus(id: Long) {

        var newStatus = 0
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {
                val oldSt = item.status
                if (oldSt == 0) newStatus = 1
                item.status = newStatus
                dbManager.saveTask(item)
            }
        }

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()
    }


      fun saveTask(id: Long) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)


        if (id == -1L) { // новая напоминалка

            val newTask = DataTask(
                id = id,
                title = editTextAddTaskTitle.text.toString(),
                description = editTextAddTaskMessage.text.toString(),
                date = editTextAddTaskDate.text.toString(),
                status = 0
            )

            tasks.add(newTask)
            dbManager.addTask(newTask)
        } else { // редактируем напоминалку id
            for(item in tasks) {
                if (item.id == id) {
                    item.title = editTextAddTaskTitle.text.toString()
                    item.description = editTextAddTaskMessage.text.toString()
                    item.date = editTextAddTaskDate.text.toString()
                    dbManager.saveTask(item)
                }
            }
        }

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()

        windowTasks.visibility = View.VISIBLE
        bottomMenu.visibility = View.VISIBLE
        windowAddTask.visibility = View.GONE
    }

    fun deleteTask(id: Long) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {
                dbManager.deleteTask(item.id)
                tasks.remove(item)

                break
            }
        }

        // говорим адаптеру, что нужно обновить нужную вьюху
        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
        // говорим адаптеру "обнови всё" - не рекомендуется
        rvTasks.adapter?.notifyDataSetChanged()

        //dbManager.saveTasks(tasks)

        windowTasks.visibility = View.VISIBLE
        bottomMenu.visibility = View.VISIBLE
        windowAddTask.visibility = View.GONE
    }

    fun myAlert(text : String) {
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
    }

    suspend fun test1(a : Int, b: Int) : Int {
        delay(10000L)
        val c = a + b
        Log.i("Developer.test","Результат: $c")

        return c
    }
    fun test2() {
        Log.i("Developer.test","Выполнилась лямбда")
        Toast.makeText(this, "Выполнилась лямбда",Toast.LENGTH_SHORT).show()
    }


}

