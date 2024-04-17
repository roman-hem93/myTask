package ru.artrostudio.mytask.controller

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.artrostudio.mytask.view.StructureView
import ru.artrostudio.mytask.view.WindowsDirector

class ControllerTasks(activity: AppCompatActivity) {

    private val windowsDirector = WindowsDirector(activity)
    private val structureView = StructureView(activity)
    private val window = structureView.tasks


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
    }
}