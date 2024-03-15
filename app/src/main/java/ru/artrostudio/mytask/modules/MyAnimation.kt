package ru.artrostudio.mytask.modules

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MyAnimation {

    // константы, принимаемые в качестве изменяемого параметра
    val ATTRIBUTE_X = "x" // изменение параметра x
    val ATTRIBUTE_Y = "y" // изменение параметра y
    val ATTRIBUTE_ALPHA = "alpha" // изменение параметра alpha
    val ATTRIBUTE_SCALE_X = "scaleX" // изменение параметра scaleX
    val ATTRIBUTE_SCALE_Y = "scaleY" // изменение параметра scaleY
    val ATTRIBUTE_SCALE_XY = "scaleXY" // изменение параметра scaleX и пропорциональное изменение scaleY

    // константы типов протекания анимации
    val TYPE_EVENLY = "evenly" // равномерное изменение параметра
    val TYPE_SMOOTHLY = "smoothly" // плавное начало и движение и окончание
    val TYPE_CAROUSEL = "carousel" // плавный старт, выполнение анимации и плавный возврат к исходному значению

    // прочие константы
    private val TIME_STEP : Long = 5L // время между итерациями (примерно 200 кадр/с)

    fun start(view: View, attribute: String, neededStart: Boolean, startValue: Float, stopValue: Float, timeAnimation: Long, type: String, lambda: ()->Unit = {}) {
        // attribute - анимируемый атрибут
        // neededStart - указывает на то, нужно ли использовать startValue
        //   neededStart = true - изменяем начальное значение параметра с startValue
        //   neededStart = false - изменяем параметр начиная от его текущего значения
        // type - тип протекания анимации

        // вычисляем начальное значение основного параметра, откуда начнётся анимация (для случая neededStart == true)
        var startValueFinally = startValue
        // вычисляем начальное значение зависимого параметра (нужен в некоторых случаях)
        var startValueSecondFinally = 0f
        when(attribute) {
            ATTRIBUTE_SCALE_XY -> startValueSecondFinally = (startValue/view.scaleX)*view.scaleY
        }

        // корректируем начальное значение изменяемых параметров в случае, если neededStart == false
        if (neededStart == false) {
            // основной изменяемый параметр
            when(attribute) {
                ATTRIBUTE_X -> startValueFinally = view.x
                ATTRIBUTE_Y -> startValueFinally = view.y
                ATTRIBUTE_ALPHA -> startValueFinally = view.alpha
                ATTRIBUTE_SCALE_X -> startValueFinally = view.scaleX
                ATTRIBUTE_SCALE_Y -> startValueFinally = view.scaleY
                ATTRIBUTE_SCALE_XY -> startValueFinally = view.scaleX
            }
            // зависимый изменяемый параметр
            when(attribute) {
                ATTRIBUTE_SCALE_XY -> startValueSecondFinally = view.scaleY
            }
        }

        // вычисляем конечное значение зависимого параметра
        val stopValueSecond = (stopValue/startValueFinally)*startValueSecondFinally

        // количество шагов
        val countStep = Math.round((timeAnimation/TIME_STEP).toFloat())
        //диапазон изменения параметра
        val diapason = stopValue - startValueFinally                      // основной изменяемый параметр
        val diapason_second = stopValueSecond - startValueSecondFinally   // зависимый изменяемый параметр
        // вычисляем шаг изменения параметра
        val step = diapason/countStep                        // основной изменяемый параметр
        val stepSecond = diapason_second/countStep           // зависимый изменяемый параметр

        // запускаем корутину (в основном потоке) с задержкой времени между шагами
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 1..countStep) {
                delay(TIME_STEP) // задержка
                // значение параметра на этом шаге
                var value : Float = 0f           // основной изменяемый параметр
                var valueSecond : Float = 0f     // зависимый изменяемый параметр
                when(type) {
                    TYPE_EVENLY -> {
                        value = startValueFinally + step*i                       // основной изменяемый параметр
                        valueSecond = startValueSecondFinally + stepSecond*i     // зависимый изменяемый параметр
                    }
                    TYPE_SMOOTHLY -> {/*TODO реализуй*/}
                    TYPE_CAROUSEL -> {/*TODO реализуй*/}
                }
                // присваиваем новое значение
                when(attribute) {
                    ATTRIBUTE_X -> view.x = Math.round(value).toFloat()
                    ATTRIBUTE_Y -> view.y = Math.round(value).toFloat()
                    ATTRIBUTE_ALPHA -> view.alpha = value
                    ATTRIBUTE_SCALE_X -> view.scaleX = value
                    ATTRIBUTE_SCALE_Y -> view.scaleY = value
                    ATTRIBUTE_SCALE_XY -> {
                        view.scaleX = value
                        view.scaleY = valueSecond
                    }
                }
            }
            // фиксируем окончательное положение (зазищаемся от неправильных округлений)
            when(attribute) {
                ATTRIBUTE_X -> view.x = stopValue
                ATTRIBUTE_Y -> view.y = stopValue
                ATTRIBUTE_ALPHA -> view.alpha = stopValue
                ATTRIBUTE_SCALE_X -> view.scaleX = stopValue
                ATTRIBUTE_SCALE_Y -> view.scaleY = stopValue
                ATTRIBUTE_SCALE_XY -> {
                    view.scaleX = stopValue
                    view.scaleY = stopValueSecond
                }
            }
            lambda() // выполняем лямбду после окончания анимации
        }
    }
}