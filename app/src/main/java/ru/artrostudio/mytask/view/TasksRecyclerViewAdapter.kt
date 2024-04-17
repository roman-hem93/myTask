package ru.artrostudio.mytask.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.artrostudio.mytask.DataTask
import ru.artrostudio.mytask.R

class TasksRecyclerViewAdapter(_context: Context, _array: ArrayList<DataTask>, _funOpenTask: (Long) -> Unit, _funSetStatus: (Long) -> Unit) : RecyclerView.Adapter<TasksRecyclerViewAdapter.ViewHolder>() {

    var context = _context
    var funOpenTask = _funOpenTask
    var funSetStatus = _funSetStatus
    var array = _array
    var icons = arrayOf(   // массив с иконками галок
        R.drawable.check1,
        R.drawable.check2
    )

    //val resDifference = context.resources.getString(R.string.meterDifference) // ресурс с текстом "Расход"
    //var meterName = context.resources.getStringArray(R.array.meter_name) // массив с именами приборов учёта

    // создаёт вьюху - отдельный элемент списка
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val layoutStatus = view.findViewById<ConstraintLayout>(R.id.layoutStatus) // контейнер с картинкой
        val layoutTitle = view.findViewById<ConstraintLayout>(R.id.layoutTitle) // контейнер с заголовком и датой
        val title = view.findViewById<TextView>(R.id.titleTask) // имя прибора учёта
        val date = view.findViewById<TextView>(R.id.dateTask) // показание
        val check = view.findViewById<ImageView>(R.id.checkTask) // расход

        fun bind(listItem : DataTask, context: Context) {
            title.text = listItem.title
            date.text = listItem.date
            check.setImageResource(icons[listItem.status])
            layoutTitle.setOnClickListener() {
                funOpenTask(listItem.id)
            }
            layoutStatus.setOnClickListener() {
                funSetStatus(listItem.id)
            }

        }
    }

    // опиши
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.item_task, parent, false))
    }

    // опиши
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var listItem = array.get(position)
        holder.bind(listItem, context)
    }

    // возвращает количество элементов списка (в большинстве случаев равно количеству элементов массива)
    override fun getItemCount(): Int {
        return array.size
    }

}