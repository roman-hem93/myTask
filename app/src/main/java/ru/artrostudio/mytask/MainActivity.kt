package ru.artrostudio.mytask


import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    //lateinit var context: MainActivity
    lateinit var tasks: ArrayList<DataTask>
    lateinit var dbManager: DateBaseManager

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

        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        dbManager = DateBaseManager(this)
        tasks = dbManager.getTasks()

        val notifications : Notifications = Notifications(this)








        // попытки сделать что-то с SQLite, пока нафиг

        val db : SQLiteDatabase = baseContext.openOrCreateDatabase("app.db", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, age INTEGER, UNIQUE(name))")
        db.execSQL("INSERT OR IGNORE INTO users VALUES ('Tom Smith', 23), ('John Dow', 31);")

        val query : Cursor = db.rawQuery("SELECT * FROM users;", null)
        if(query.moveToFirst()) {

            val name : String = query.getString(0)
            val age : Int = query.getInt(1)

            Log.i("Developer.BD","Тест SQL: $name и $age")
        }

        query.close()
        db.close()





















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

    fun myAlert() {
        Toast.makeText(this, "Тест",Toast.LENGTH_LONG).show()
    }


}

