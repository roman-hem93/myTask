package ru.artrostudio.mytask.view

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ru.artrostudio.mytask.modules.MyAnimation

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

    inner class WindowCategories : ItemWindowDirector {

        val categories = structureView.windows.categories
        val background = structureView.windows.grayBackground

        override fun open() {
            categories.visibility = View.VISIBLE
            background.visibility = View.VISIBLE
            background.alpha = 0f // задаю тут, т.к. объект include в xml не поддерживает свойство alpa
            MyAnimation.start(categories, MyAnimation.ATTRIBUTE_X, false, 9999f, 0f,120L, MyAnimation.TYPE_EVENLY)
            MyAnimation.start(background, MyAnimation.ATTRIBUTE_ALPHA, false, 9999f, 1f,120L, MyAnimation.TYPE_EVENLY)

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            MyAnimation.start(categories, MyAnimation.ATTRIBUTE_X, false, 9999f, -structureView.windows.categories.width.toFloat(),120L, MyAnimation.TYPE_EVENLY, {categories.visibility = View.GONE})
            MyAnimation.start(background, MyAnimation.ATTRIBUTE_ALPHA, false, 9999f, 0f,120L, MyAnimation.TYPE_EVENLY,{background.visibility = View.GONE})

            categories.visibility = View.GONE
            background.visibility = View.GONE
        }
    }

    inner class WindowSetDate : ItemWindowDirector {

        override fun open() {
            structureView.windows.setDate.visibility = View.VISIBLE

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            structureView.windows.setDate.visibility = View.GONE
        }
    }

    inner class WindowSetTime : ItemWindowDirector {

        override fun open() {
            structureView.setTime.timeView.setIs24HourView(true)
            structureView.windows.setTime.visibility = View.VISIBLE

            closeCurrentWindow()                 // закрываем старое окно
            closeCurrentWindow = { close() }     // вешаем новую функцию закрытия текущего окна
        }

        override fun close() {
            structureView.windows.setTime.visibility = View.GONE
        }
    }



    interface ItemWindowDirector {
        fun open()
        fun close()
    }

}