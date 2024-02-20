package ru.artrostudio.mytask


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("Developer.System","Приложение запустилось")

        //окна
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val windowSetDate : ConstraintLayout = findViewById(R.id.windowSetDate)
        val windowSetTime : ConstraintLayout = findViewById(R.id.windowSetTime)

        val dbManager : DateBaseManager = DateBaseManager(this)
        val tasks : ArrayList<DataTask> = dbManager.getTasks()

        val notifications : Notifications = Notifications(this)

        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)
        // определяем и настраиваем RecyclerView
        val adapter = TasksRecyclerViewAdapter(this as Context, tasks)
        rvTasks.hasFixedSize()
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        if (tasks.size == 0) {
            // выполняем вывод инфы об отсутствии задач
            Toast.makeText(this, "Нет задач",Toast.LENGTH_LONG).show()
        }

        val buttonTasksAdd : Button = findViewById(R.id.tasksAdd)
        buttonTasksAdd.setOnClickListener() {
            windowTasks.visibility = View.GONE
            windowAddTask.visibility = View.VISIBLE
        }

        val buttonAddTaskCancel : Button = findViewById(R.id.addTaskCancel)
        buttonAddTaskCancel.setOnClickListener() {
            windowTasks.visibility = View.VISIBLE
            windowAddTask.visibility = View.GONE
        }

        //val buttonDateCancel : Button = findViewById(R.id.dateCancel)
        val buttonDateCancel : Button = windowSetDate.findViewById(R.id.cancel)
        buttonDateCancel.setOnClickListener() {
            windowTasks.visibility = View.VISIBLE
            windowSetDate.visibility = View.GONE
        }

        val buttonSetTime : Button = findViewById(R.id.setTime)
        buttonSetTime.setOnClickListener() {
            windowSetTime.visibility = View.VISIBLE
            windowSetDate.visibility = View.GONE
        }

        //val buttonTimeCancel : Button = findViewById(R.id.timeCancel)
        val buttonTimeCancel : Button = windowSetTime.findViewById(R.id.cancel)
        buttonTimeCancel.setOnClickListener() {
            windowSetDate.visibility = View.VISIBLE
            windowSetTime.visibility = View.GONE
        }

        val buttonTasksDate : Button = findViewById(R.id.tasksDate)
        buttonTasksDate.setOnClickListener() {
            val dateView = findViewById<DatePicker>(R.id.datePicker)
            val timeView = findViewById<TimePicker>(R.id.timePicker)

            timeView.setIs24HourView(true)

            windowTasks.visibility = View.GONE
            windowSetDate.visibility = View.VISIBLE
        }

        val buttonTasksNotification : Button = findViewById(R.id.tasksNotification)
        buttonTasksNotification.setOnClickListener() {

            notifications.setNotification()



            Toast.makeText(this, "Уведомление создано",Toast.LENGTH_LONG).show()
        }

        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)

        val buttonAddTaskSave : Button = findViewById(R.id.addTaskSave)
        buttonAddTaskSave.setOnClickListener() {

            tasks.add(DataTask(
                editTextAddTaskTitle.text.toString(),
                editTextAddTaskMessage.text.toString(),
                editTextAddTaskDate.text.toString(),
                0))

            editTextAddTaskDate.setText("")
            editTextAddTaskTitle.setText("")
            editTextAddTaskMessage.setText("")

            // говорим адаптеру, что добавился новый элемент в массив и он на последнем месте
            rvTasks.adapter?.notifyItemInserted(tasks.size-1)
            // говорим адаптеру "обкови всё" - не рекомендуется
            //rvTasks.adapter?.notifyDataSetChanged()

            dbManager.saveTasks(tasks)

            windowTasks.visibility = View.VISIBLE
            windowAddTask.visibility = View.GONE
        }

        fun myAlert() {
            Toast.makeText(this, "Тест",Toast.LENGTH_LONG).show()
        }

    }


}

