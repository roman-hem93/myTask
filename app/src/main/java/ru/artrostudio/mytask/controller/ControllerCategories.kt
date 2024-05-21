package ru.artrostudio.mytask.controller

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.artrostudio.mytask.view.StructureView
import ru.artrostudio.mytask.view.WindowsDirector

class ControllerCategories(private val activity: AppCompatActivity, private val windowsDirector : WindowsDirector, private val structureView : StructureView) {
    private val window = structureView.categories
    private val background = structureView.grayBackground

    init {
        start()
    }

    fun start() {
        setBaseListeners()



    }

    // навешивает все базовые обработчики событий
    fun setBaseListeners() {
        structureView.windows.grayBackground.setOnClickListener() {
            windowsDirector.WindowCategories().close()
        }


    }

}