package com.example.habitstracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class RecyclerViewAdapter : ListAdapter<Habit, MainViewHolder>(ItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habit, parent, false)

        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun addElements(habits: MutableList<Habit>) {
        submitList(ArrayList(currentList).apply {
            clear()
            addAll(habits)
            notifyDataSetChanged()
        }
        )
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