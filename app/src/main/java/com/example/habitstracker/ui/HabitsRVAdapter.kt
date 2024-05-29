package com.example.habitstracker.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.R

class HabitsRVAdapter(private val habits: List<Habit>, private val clickListener: Click) :  RecyclerView.Adapter<HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(habits[position])
        }
        holder.setIsRecyclable(false)
    }
    fun interface Click {
        fun onClick(habit: Habit)
    }
}