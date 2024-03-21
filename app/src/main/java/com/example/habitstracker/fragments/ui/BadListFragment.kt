package com.example.habitstracker.fragments.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.Habit
import com.example.habitstracker.R
import com.example.habitstracker.RecyclerViewAdapter
import com.example.habitstracker.databinding.FragmentListBinding
import com.example.habitstracker.db.AppDataBase
import com.example.habitstracker.db.HabitConvertor
import com.example.habitstracker.db.HabitEntity

class BadListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var recyclerView: RecyclerView
    private var habitsList: MutableList<Habit> = mutableListOf()
    private var habit = Habit(0, "", "", 0, 0, 0, 0, 0)
    private val db = AppDataBase
    private val convertor = HabitConvertor()
    private val adapter = RecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = binding.fab

        fab.setOnClickListener {
            try {
                findNavController().navigate(R.id.addFragment)
            } catch (e: IllegalStateException) {}
        }
        getData(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        habitsList.clear()
        adapter.notifyDataSetChanged()
        getData(arguments)
    }

    private fun getData(savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        if (arguments != null && requireArguments().containsKey(AddFragment.key)) {
            habit = arguments?.getParcelable<Habit>(AddFragment.key) as Habit
        }

        val listFromDb: List<HabitEntity> = db(requireContext()).habitDao().getAll()

        if (!listFromDb.isEmpty()) {
            listFromDb.forEach {
                if (it.type == 1) {
                    habitsList.add(convertor.map(it))
                }
                Log.d("habits from db", "$it")
            }
        }
        if (habitsList.isNotEmpty()) {
            adapter.addElements(habitsList)
            Log.d("MainActivity", "$habitsList")
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = BadListFragment()
    }
}