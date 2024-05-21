package ru.artrostudio.mytask.controller

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.artrostudio.mytask.R
import ru.artrostudio.mytask.modules.MyAnimation
import ru.artrostudio.mytask.modules.MyNotifications
import ru.artrostudio.mytask.view.StructureView
import ru.artrostudio.mytask.view.WindowsDirector

class ControllerTasks(private val activity: AppCompatActivity, private val windowsDirector : WindowsDirector, private val structureView : StructureView) {

    /*TODO тут и везде нельзя создавать копии объектов, а нужно получить однажды созданный*/
    private val window = structureView.tasks
    private val notifications = MyNotifications(activity)


    init {
        start()
    }

    fun start() {
        setBaseListeners()



    }

    // навешивает все базовые обработчики событий
    fun setBaseListeners() {
        window.btAdd.setOnClickListener() {
            // задаём тип окна = добавление новой задачи
            windowsDirector.WindowAddTask().typeWindow = windowsDirector.WindowAddTask().TYPE_ADD
            windowsDirector.WindowAddTask().open()
            Log.i("Developer.Controller","бла бла бла")
        }

        window.ivCatigories.setOnClickListener() {
            windowsDirector.WindowCategories().open()
        }

        window.btNotification.setOnClickListener() {
            notifications.setNotification()
            Toast.makeText(activity, "Уведомление создано", Toast.LENGTH_LONG).show() /*TODO убрать во VIEW*/
        }

        window.btSetDate.setOnClickListener() {
            windowsDirector.WindowSetDate().open()



            //bottomMenu.visibility = View.GONE

        }


    }
}