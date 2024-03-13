package com.example.habitstracker

import android.content.res.ColorStateList
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(
    view: View,
    private val onHabitListener: RecyclerViewAdapter.HabitOnClickListener
) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    private val titleContainer: LinearLayout = view.findViewById(R.id.habitNameContainer)
    private val title: TextView = view.findViewById(R.id.habitName)
    private val description: TextView = view.findViewById(R.id.habitAbout)
    private val priority: TextView = view.findViewById(R.id.priority_text)
    private val habitCount: TextView = view.findViewById(R.id.addCount)

    fun bind(view: Habit) {
        title.text = view.title
        description.text = view.description
        priority.text = when (view.priority) {
            0 -> "Низкий"
            1 -> "Средний"
            else -> "Большой"
        }
        habitCount.text = view.count.toString()
        val newColor = view.color and 0x00FFFFFF or 0x40000000
        titleContainer.backgroundTintList = ColorStateList.valueOf(newColor)
        //deleteButton()
    }

    /*private fun deleteButton() {
        doBtn.setOnClickListener {
            habitCount.text = (habitCount.text.toString().toInt() - 1).toString()
        }
    }*/

    override fun onClick(v: View?) {
        onHabitListener.onClick(adapterPosition)
    }
}