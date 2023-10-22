package ru.artrostudio.mytask

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
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

            // создание канала уведомлений. Должно быть создано до запуска уведомления. Книжка рекомендует создавать при запуске приложения
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "channel_name"
                val descriptionText = "channel_description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system.
                val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // второй аргумент - это то, что запустится в приложении
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            // объект действия по умолчанию при касании по уведомлению
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            // создание объекта-действия при нажатии на кнопку
            val ACTION_SNOOZE = "snooze"
            // второй аргумент - это то, что запустится в приложении
            val snoozeIntent = Intent(this, MainActivity::class.java).apply {
                action = ACTION_SNOOZE
                putExtra(EXTRA_NOTIFICATION_ID, 0)
            }
            val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, PendingIntent.FLAG_MUTABLE)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("Напоминание")
                .setContentText("Пора покормить кота")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.icon_notification, "NAME_BUTT", snoozePendingIntent)


            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, builder.build())
            }

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

