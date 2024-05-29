package com.example.habitstracker.presentation

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.R

class HabitViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val container: ConstraintLayout = view.findViewById(R.id.habitConstraintLayout)
    private val name: TextView = view.findViewById(R.id.name)
    private val description: TextView = view.findViewById(R.id.description)
    private val priority: TextView = view.findViewById(R.id.priority_text)
    private val type: TextView = view.findViewById(R.id.type_text)
    private val period: TextView = view.findViewById(R.id.period_text)
    private val num: TextView = view.findViewById(R.id.numText)

    fun bind(view: Habit) {
        container.setBackgroundColor((view.color))
        name.text = view.title
        description.text = view.description
        priority.text = view.priority.value
        type.text = view.type.value
        period.text = view.frequency.toString()
        num.text = view.count.toString()
    }
}