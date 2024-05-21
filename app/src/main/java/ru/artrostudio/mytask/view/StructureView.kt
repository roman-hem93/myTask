package ru.artrostudio.mytask.view

import android.content.Context
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.artrostudio.mytask.R

class StructureView(activity: AppCompatActivity) {

    val windows = Windows(activity)

    val startLoading = StartLoading(windows.startLoading)
    val tasks = Tasks(windows.tasks)
    val addTask = AddTask(windows.addTask)
    val setDate = SetDate(windows.setDate)
    val setTime = SetTime(windows.setTime)
    val setting = Setting(windows.setting)
    val categories = Categories(windows.categories)
    val bottomMenu = BottomMenu(windows.bottomMenu)
    val grayBackground = GrayBackground(windows.grayBackground)


    // список идентификаторов всех окон (в том числе мелких/летающих/выпадающих)
    class Windows(activity: AppCompatActivity) {
        val startLoading : ConstraintLayout = activity.findViewById(R.id.windowStartLoading)
        val tasks : ConstraintLayout = activity.findViewById(R.id.windowTasks)
        val addTask : ConstraintLayout = activity.findViewById(R.id.windowAddTask)
        val setDate : ConstraintLayout = activity.findViewById(R.id.windowSetDate)
        val setTime : ConstraintLayout = activity.findViewById(R.id.windowSetTime)
        val setting : ConstraintLayout = activity.findViewById(R.id.windowSetting)
        val categories : ConstraintLayout = activity.findViewById(R.id.windowCategories)
        val bottomMenu : ConstraintLayout = activity.findViewById(R.id.bottomMenu)
        val grayBackground : ConstraintLayout = activity.findViewById(R.id.windowGreyBackground)
    }

    // список идентификаторов интерфейса окна startLoading
    class StartLoading(window : ConstraintLayout) {
        //кнопки


        //поля ввода

    }

    // список идентификаторов интерфейса окна addTask
    class AddTask(window : ConstraintLayout) {
        //кнопки
        val btSave : Button = window.findViewById(R.id.addTaskSave)
        val btDelete : Button = window.findViewById(R.id.addTaskDelete)
        val btCancel : Button = window.findViewById(R.id.addTaskCancel)

        //поля ввода
        val etDate : EditText = window.findViewById(R.id.addTaskDate)
        val etTitle : EditText = window.findViewById(R.id.addTaskTitle)
        val etMessage : EditText = window.findViewById(R.id.addTaskMessage)
    }

    // список идентификаторов интерфейса окна Tasks
    class Tasks(window : ConstraintLayout) {
        //кнопки
        val btAdd : Button = window.findViewById(R.id.tasksAdd)
        val btNotification : Button = window.findViewById(R.id.tasksNotification)
        val btSetDate : Button = window.findViewById(R.id.tasksDate)

        // ImageView
        val ivCatigories : ImageView = window.findViewById(R.id.buttonCatigoriesTasksItem)

        //RecyclerView
        val rvTasks : RecyclerView = window.findViewById(R.id.tasksRV)
    }

    // список идентификаторов интерфейса окна SetDate
    class SetDate(window : ConstraintLayout) {
        //кнопки
        val btCancel : Button = window.findViewById(R.id.setDateCancel)
        val btTime : Button = window.findViewById(R.id.setDateSetTime)

        // выбор даты
        val dateView = window.findViewById<DatePicker>(R.id.datePicker)

    }

    // список идентификаторов интерфейса окна SetTime
    class SetTime(window : ConstraintLayout) {
        //кнопки
        val btCancel : Button = window.findViewById(R.id.setTimeCancel)

        // выбор времени
        val timeView = window.findViewById<TimePicker>(R.id.timePicker)
    }

    // список идентификаторов интерфейса окна Setting
    class Setting(window : ConstraintLayout) {

    }

    // список идентификаторов интерфейса окна Categories
    class Categories(window : ConstraintLayout) {

    }

    // список идентификаторов интерфейса окна BottomMenu
    class BottomMenu(window : ConstraintLayout) {

    }

    // список идентификаторов интерфейса окна GrayBackground
    class GrayBackground(window : ConstraintLayout) {

    }





}
