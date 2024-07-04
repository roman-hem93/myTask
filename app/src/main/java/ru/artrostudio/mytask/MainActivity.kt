package ru.artrostudio.mytask


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
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
import ru.artrostudio.mytask.controller.ControllerTasks
import ru.artrostudio.mytask.controller.Controllers
import ru.artrostudio.mytask.database.sqlite.SQLiteManager
import ru.artrostudio.mytask.modules.MyAnimation
import ru.artrostudio.mytask.modules.MyNotifications
import ru.artrostudio.mytask.view.TasksRecyclerViewAdapter
import ru.artrostudio.mytask.view.WindowsDirector

class MainActivity : AppCompatActivity() {

    //lateinit var context: MainActivity
    lateinit var tasks: ArrayList<DataTask>
    lateinit var dbManager: SQLiteManager
    //lateinit var windowsDirector: WindowsDirector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("Developer.System","Приложение запустилось")

        //windowsDirector = WindowsDirector(this)  // ЭТО ЛИШНЯЯ КОПИЯ, нужно избавиться !!!!! ВиндовсДиректор создаётся в контроллерах

        // инициализируем все контроллеры
        val controllers = Controllers(this)




        //windowsDirector.WindowTasks().open()

        dbManager = SQLiteManager(this)
        dbManager.getTasks(lambdaOK,lambdaERROR)


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

                val lambdaOK : () -> Unit = {
                    //после успешного сохранения в БД:

                    // говорим адаптеру, что нужно обновить нужную вьюху
                    //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
                    // говорим адаптеру "обнови всё" - не рекомендуется
                    rvTasks.adapter?.notifyDataSetChanged()
                }
                val lambdaERROR : () -> Unit = {}

                dbManager.saveTask(item, lambdaOK, lambdaERROR)

                break
            }
        }
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

            val lambdaOK : (Long) -> Unit = {newId : Long ->
                //после успешного сохранения в БД:

                //присваиваем новый id
                newTask.id = newId

                // добавляем эту задачу в память
                tasks.add(newTask)

                // говорим адаптеру, что нужно обновить нужную вьюху
                //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
                // говорим адаптеру "обнови всё" - не рекомендуется
                rvTasks.adapter?.notifyDataSetChanged()

                windowTasks.visibility = View.VISIBLE
                bottomMenu.visibility = View.VISIBLE
                windowAddTask.visibility = View.GONE
            }
            val lambdaERROR : () -> Unit = {}

            dbManager.addTask(newTask, lambdaOK, lambdaERROR)
        } else { // редактируем напоминалку id
            for(item in tasks) {
                if (item.id == id) {
                    item.title = editTextAddTaskTitle.text.toString()
                    item.description = editTextAddTaskMessage.text.toString()
                    item.date = editTextAddTaskDate.text.toString()

                    val lambdaOK : () -> Unit = {
                        //после успешного сохранения в БД:

                        // говорим адаптеру, что нужно обновить нужную вьюху
                        //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
                        // говорим адаптеру "обнови всё" - не рекомендуется
                        rvTasks.adapter?.notifyDataSetChanged()

                        windowTasks.visibility = View.VISIBLE
                        bottomMenu.visibility = View.VISIBLE
                        windowAddTask.visibility = View.GONE
                    }
                    val lambdaERROR : () -> Unit = {}

                    dbManager.saveTask(item, lambdaOK, lambdaERROR)

                    break
                }
            }
        }
    }

    fun deleteTask(id: Long) {
        val windowTasks : ConstraintLayout = findViewById(R.id.windowTasks)
        val windowAddTask : ConstraintLayout = findViewById(R.id.windowAddTask)
        val bottomMenu : ConstraintLayout = findViewById(R.id.bottomMenu)
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)

        // ищем напоминалку id
        for(item in tasks) {
            if (item.id == id) {

                val lambdaOK : () -> Unit = {
                    tasks.remove(item)

                    // говорим адаптеру, что нужно обновить нужную вьюху
                    //rvTasks.adapter?.notifyItemInserted(tasks.size-1)
                    // говорим адаптеру "обнови всё" - не рекомендуется
                    rvTasks.adapter?.notifyDataSetChanged()

                    //dbManager.saveTasks(tasks)

                    windowTasks.visibility = View.VISIBLE
                    bottomMenu.visibility = View.VISIBLE
                    windowAddTask.visibility = View.GONE
                }
                val lambdaERROR : () -> Unit = {}

                dbManager.deleteTask(item.id, lambdaOK, lambdaERROR)

                break
            }
        }
    }

    fun myAlert(text : String) {
        Toast.makeText(this, text,Toast.LENGTH_SHORT).show()
    }


}

