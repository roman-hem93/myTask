package ru.artrostudio.mytask.view

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

// ПОКА это класс, который переключает окна
// НО в дальнейшем может перерасти в центральный класс взаимодействия с пользователем
class WindowsDirector(activity : AppCompatActivity) {

    private val structureView = StructureView(activity)
    private var closeCurrentWindow : ()->Unit = {this.WindowStartLoading().close()}


    inner class WindowStartLoading : ItemWindowDirector {

        override fun open() {
            structureView.windows.startLoading.visibility = View.VISIBLE

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            structureView.windows.startLoading.visibility = View.GONE
        }

    }

    inner class WindowTasks : ItemWindowDirector {

        override fun open() {
            structureView.windows.tasks.visibility = View.VISIBLE

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            structureView.windows.tasks.visibility = View.GONE
        }
    }



    inner class WindowAddTask : ItemWindowDirector {

        val TYPE_ADD = "add task"
        val TYPE_SAVE = "save task"

        var typeWindow = TYPE_ADD

        override fun open() {
            structureView.addTask.btSave.setOnClickListener() {
                if (typeWindow == TYPE_ADD) {
                    //saveTask(-1)
                } else if (typeWindow == TYPE_SAVE) {
                    //saveTask(-1)
                }
            }

            structureView.addTask.btDelete.visibility = View.GONE

            structureView.windows.addTask.visibility = View.VISIBLE
            structureView.windows.tasks.visibility = View.GONE

            structureView.windows.bottomMenu.visibility = View.GONE

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            structureView.windows.addTask.visibility = View.GONE

            structureView.addTask.etDate.setText("")
            structureView.addTask.etTitle.setText("")
            structureView.addTask.etMessage.setText("")
        }
    }



    interface ItemWindowDirector {
        fun open()
        fun close()
    }

}