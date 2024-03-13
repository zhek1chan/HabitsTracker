package com.example.habitstracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class RecyclerViewAdapter(private val onHabitListener: HabitOnClickListener) :
    ListAdapter<Habit, ViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habit, parent, false)

        return ViewHolder(view, onHabitListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun addElements(habits: List<Habit>) {
        submitList(ArrayList(currentList).apply { addAll(habits) })
    }

    interface HabitOnClickListener {

        fun onClick(habitPosition: Int)
    }
}

class ItemCallback : DiffUtil.ItemCallback<Habit>() {

    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }
}