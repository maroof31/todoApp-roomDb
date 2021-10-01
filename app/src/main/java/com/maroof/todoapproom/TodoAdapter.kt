package com.maroof.todoapproom


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(val list: List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todoModel: TodoModel) {
            with(itemView) {
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[Random().nextInt(colors.size)]
                val viewColorTag=findViewById<View>(R.id.viewColorTag)
                val txtShowTitle=findViewById<TextView>(R.id.txtShowTitle)
                val txtShowTask=findViewById<TextView>(R.id.txtShowTask)
                val txtShowCategory=findViewById<TextView>(R.id.txtShowCategory)
                val txtShowTime=findViewById<TextView>(R.id.txtShowTime)
                val txtShowDate=findViewById<TextView>(R.id.txtShowDate)
                    viewColorTag.setBackgroundColor(randomColor)
                txtShowTitle.text = todoModel.title
                txtShowTask.text = todoModel.description
                txtShowCategory.text = todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)

                 txtShowTime.text =updateTime(todoModel.time)
                txtShowDate.text=updateDate(todoModel.date)
            }
        }
        private fun updateTime(time: Long):String {
            //Mon, 5 Jan 2020
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat)

                return sdf.format(Date(time))

        }

        private fun updateDate(time: Long) :String{
            //Mon, 5 Jan 2020
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat)
                return sdf.format(Date(time))

        }
    }

}


