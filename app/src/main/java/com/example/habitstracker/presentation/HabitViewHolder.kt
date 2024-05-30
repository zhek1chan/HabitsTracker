package com.example.habitstracker.presentation

import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.App
import com.example.habitstracker.R
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.models.Type

class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val container: ConstraintLayout = view.findViewById(R.id.habitConstraintLayout)
    private val name: TextView = view.findViewById(R.id.name)
    private val description: TextView = view.findViewById(R.id.description)
    private val priority: TextView = view.findViewById(R.id.priority_text)
    private val type: TextView = view.findViewById(R.id.type_text)
    private val period: TextView = view.findViewById(R.id.period_text)
    private val num: TextView = view.findViewById(R.id.numText)
    private val button: Button = view.findViewById(R.id.button_done)
    private var counter: Int = 0

    fun bind(view: Habit) {
        container.setBackgroundColor((view.color))
        name.text = view.title
        description.text = view.description
        priority.text = view.priority.value
        type.text = view.type.value
        period.text = view.frequency.toString()
        num.text = view.count.toString()
        button.setOnClickListener {
            counter += 1
            if ((counter >= num.text.toString().toInt()) && (view.type == Type.Bad)) {
                val text =  App.appContext.getString(R.string.stop_doing_it)
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(App.appContext, text, duration)
                toast.show()
            } else if ((counter < num.text.toString().toInt()) && (view.type == Type.Bad)) {
                val left = num.text.toString().toInt() - counter
                val text = App.appContext.getString(R.string.you_can_do_it_x_times, left)
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(App.appContext, text, duration)
                toast.show()
            } else if ((counter < num.text.toString().toInt()) && (view.type == Type.Good)) {
                val left = num.text.toString().toInt() - counter
                val text =  App.appContext.getString(R.string.you_should_do_it_x_times, left)
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(App.appContext, text, duration)
                toast.show()
            } else if ((counter >= num.text.toString().toInt()) && (view.type == Type.Good)) {
                val text =  App.appContext.getString(R.string.you_are_breathtaking)
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(App.appContext, text, duration)
                toast.show()
            }
        }
    }
}