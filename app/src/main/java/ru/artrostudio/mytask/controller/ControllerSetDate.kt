package ru.artrostudio.mytask.controller

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.artrostudio.mytask.view.StructureView
import ru.artrostudio.mytask.view.WindowsDirector

class ControllerSetDate(private val activity: AppCompatActivity, private val windowsDirector : WindowsDirector, private val structureView : StructureView) {
    private val window = structureView.setDate

    init {
        start()
    }

    fun start() {
        setBaseListeners()



    }

    // навешивает все базовые обработчики событий
    fun setBaseListeners() {
        window.btCancel.setOnClickListener() {
            windowsDirector.WindowTasks().open()
        }

        window.btTime.setOnClickListener() {
            windowsDirector.WindowSetTime().open()
        }


    }

}