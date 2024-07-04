package ru.artrostudio.mytask.view

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.artrostudio.mytask.DataTask
import ru.artrostudio.mytask.R
import ru.artrostudio.mytask.database.sqlite.SQLiteManager

class ViewWindowTasks(activity : AppCompatActivity, val structureView : StructureView) {

    init {

    }

    fun NAME() {
        val rvTasks : RecyclerView = findViewById(R.id.tasksRV)



        val lambdaOK : (ArrayList<DataTask>) -> Unit = { result : ArrayList<DataTask> ->
            tasks = result

            // определяем и настраиваем RecyclerView
            val adapter = TasksRecyclerViewAdapter(this as Context, tasks, { id: Long -> openTask(id)}, { id: Long -> setStatus(id)})
            rvTasks.hasFixedSize()
            rvTasks.layoutManager = LinearLayoutManager(this)
            rvTasks.adapter = adapter

            if (tasks.size == 0) {
                // выполняем вывод инфы об отсутствии задач
                Toast.makeText(this, "Нет задач", Toast.LENGTH_LONG).show()
            }

        }
        val lambdaERROR : () -> Unit = {}
    }

}