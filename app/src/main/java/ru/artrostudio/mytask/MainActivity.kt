package ru.artrostudio.mytask

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")  // убирает необходимость запроса разрешений, УБЕРИ потом
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("Developer.System","Приложение запустилось")

        //окна
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val windowDate : ConstraintLayout = findViewById(R.id.windowDate)

        val dbManager : DateBaseManager = DateBaseManager(this)
        val tasks : ArrayList<DataTask> = dbManager.getTasks()

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

        val buttonDateCancel : Button = findViewById(R.id.dateCancel)
        buttonDateCancel.setOnClickListener() {
            windowTasks.visibility = View.VISIBLE
            windowDate.visibility = View.GONE
        }

        val buttonTasksDate : Button = findViewById(R.id.tasksDate)
        buttonTasksDate.setOnClickListener() {
            val dateView = findViewById<DatePicker>(R.id.datePicker)
            val timeView = findViewById<TimePicker>(R.id.timePicker)

            timeView.setIs24HourView(true)

            windowTasks.visibility = View.GONE
            windowDate.visibility = View.VISIBLE
        }

        val buttonTasksNotification : Button = findViewById(R.id.tasksNotification)
        buttonTasksNotification.setOnClickListener() {
            // Создаём уведомление
            val NOTIFICATION_ID = 101
            val CHANNEL_ID = "channelID"

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("Напоминание")
                .setContentText("Пора покормить кота")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            val notificationManager = NotificationManagerCompat.from(this)

            // проверка наличия резрешения
            /*
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val pm : PackageManager = getPackageManager()
            val hasPerm : Int = pm.checkPermission(permission, getPackageName())
            if (hasPerm != PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(NOTIFICATION_ID, builder.build())
            }
            */
            notificationManager.notify(NOTIFICATION_ID, builder.build())


            // альтернативный вариант
            val channel = NotificationChannel("105", "555", NotificationManager.IMPORTANCE_DEFAULT)

            val nm : NotificationManager = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)

            val builder2 = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("Напоминание")
                .setContentText("Пора покормить кота")
                //.setAutoCancel(true)
                //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            nm.notify(102,builder2)




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

    }


}

