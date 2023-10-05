package ru.artrostudio.mytask

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("myTask.System","Приложение запустилось")

        //окна
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)

        val dbManager : DateBaseManager = DateBaseManager(this)
        val tasks : ArrayList<DataTask> = dbManager.getTasks()

        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)


        if (tasks.size == 0) {
            // выполняем вывод инфы об отсутствии задач
            Toast.makeText(this, "Нет задач",Toast.LENGTH_LONG).show()
        } else {
            // определяем и настраиваем RecyclerView
            val adapter = TasksRecyclerViewAdapter(this as Context, tasks)
            rvTasks.hasFixedSize()
            rvTasks.layoutManager = LinearLayoutManager(this)
            rvTasks.adapter = adapter
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

        val editTextAddTaskDate : EditText = findViewById(R.id.addTaskDate)
        val editTextAddTaskTitle : EditText = findViewById(R.id.addTaskTitle)
        val editTextAddTaskMessage : EditText = findViewById(R.id.addTaskMessage)

        val buttonAddTaskSave : Button = findViewById(R.id.addTaskSave)
        buttonAddTaskSave.setOnClickListener() {
            tasks.add(DataTask(editTextAddTaskTitle.text.toString(), editTextAddTaskMessage.text.toString(), editTextAddTaskDate.text.toString(), 0))

            editTextAddTaskDate.setText("")
            editTextAddTaskTitle.setText("")
            editTextAddTaskMessage.setText("")

            // говорим адаптеру, что добавился новый элемент в массив и он на последнем месте
            rvTasks.adapter?.notifyItemInserted(tasks.size-1)
            // говорим адаптеру "обкови всё" - не рекомендуется
            //rvTasks.adapter?.notifyDataSetChanged()

            dbManager.saveTasks(tasks) {}


            windowTasks.visibility = View.VISIBLE
            windowAddTask.visibility = View.GONE
        }





/*
        val tvTitle : TextView = findViewById(R.id.title)
        val tvDate : TextView = findViewById(R.id.date)
        val tvMessage : TextView = findViewById(R.id.message)

        tvTitle.text = tasks.get(0).title
        tvDate.text = tasks.get(0).date
        tvMessage.text = tasks.get(0).message




        val bt1 : Button = findViewById(R.id.button1)
        val bt2 : Button = findViewById(R.id.button2)
        val tv : TextView = findViewById(R.id.textView)
        val textInput : EditText = findViewById(R.id.editTextText)

        bt1.setOnClickListener () {

        }

        bt2.setOnClickListener () {

        }
        */
    }


}

