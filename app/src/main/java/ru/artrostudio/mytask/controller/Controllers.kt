package ru.artrostudio.mytask.controller

import androidx.appcompat.app.AppCompatActivity
import ru.artrostudio.mytask.view.StructureView
import ru.artrostudio.mytask.view.WindowsDirector

class Controllers(private val activity: AppCompatActivity) {



    init {

        val structureView = StructureView(activity)
        val windowsDirector = WindowsDirector(activity, structureView)



        val controllerTasks = ControllerTasks(activity, windowsDirector, structureView)
        val controllerAddTask = ControllerAddTask(activity, windowsDirector, structureView)
        val controllerCategories = ControllerCategories(activity, windowsDirector, structureView)
        val controllerSetDat = ControllerSetDate(activity, windowsDirector, structureView)
        val controllerSetTime = ControllerSetTime(activity, windowsDirector, structureView)

    }
}