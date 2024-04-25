package com.example.habitstracker.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.ui.HabitsRVAdapter
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentListBinding
import com.example.habitstracker.domain.models.Type
import com.example.habitstracker.ui.ListScreenState
import com.example.habitstracker.ui.view_models.ListViewModel


class ListFragment(private val check: Boolean) : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var recyclerView: RecyclerView
    private var habitsList: MutableList<Habit> = mutableListOf()
    private val viewModel by viewModels<ListViewModel>()
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
            } catch (_: IllegalStateException) {
            }
        }
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.fillData(requireContext(), this)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    private fun clickAdapting(item: Habit) {
        Log.d("ListFragment", "Click on the habit")
        val bundle = Bundle()
        bundle.putParcelable("habit", item)
        val navController = findNavController()
        navController.navigate(R.id.addFragment, bundle)
    }

    private fun render(state: ListScreenState) {
        when (state) {
            is ListScreenState.Data -> getData(state.data)
            is ListScreenState.NoHabitsAdded -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.noHabits.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        habitsList.clear()
        viewModel.fillData(requireContext(), this)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(habits: List<Habit>) {
        binding.noHabits.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        habitsList.clear()
        habits.forEach {
            if (!check) {
                if (it.type == Type.Good) {
                    habitsList.add(it)
                }
            } else {
                if (it.type == Type.Bad) {
                    habitsList.add(it)
                }
            }
        }

        recyclerView.adapter = HabitsRVAdapter(habitsList) {
            clickAdapting(it)
        }
        recyclerView.adapter?.notifyDataSetChanged()
        Log.d("ListFragment", "$habitsList")

    }

    companion object {
        fun newInstance(check: Boolean) = ListFragment(check)
    }
}