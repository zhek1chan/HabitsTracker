package com.example.habitstracker.presentation.ui.fragments


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
import com.example.habitstracker.presentation.HabitsRVAdapter
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentListBinding
import com.example.habitstracker.domain.models.Type
import com.example.habitstracker.presentation.ListScreenState
import com.example.habitstracker.presentation.viewmodel.ListViewModel


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
            is ListScreenState.Loading -> showProgressBar()
            is ListScreenState.Error -> showToast(false)
            is ListScreenState.Success -> showToast(true)
            is ListScreenState.Data -> getData(state.data)
            is ListScreenState.NoHabitsAdded -> showEmpty()
        }
    }

    private fun showProgressBar() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun showToast(b: Boolean) {
        binding.loadingIndicator.visibility = View.GONE
        /*val text = if (b == false) {
            resources.getString(R.string.error_synch)
        } else {
            resources.getString(R.string.success)
        }
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(requireContext(), text, duration)
        toast.show()*/
    }

    private fun showEmpty() {
        binding.loadingIndicator.visibility = View.GONE
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
        binding.loadingIndicator.visibility = View.GONE
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
    }

    companion object {
        fun newInstance(check: Boolean) = ListFragment(check)
    }
}