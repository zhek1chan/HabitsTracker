package com.example.habitstracker.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.App
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentSearchBinding
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.models.Type
import com.example.habitstracker.presentation.HabitsRVAdapter
import com.example.habitstracker.presentation.ListScreenState
import com.example.habitstracker.presentation.viewmodel.ListViewModel
import com.example.habitstracker.presentation.viewmodel.ListViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Collections.swap


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView
    private var habitsList: MutableList<Habit> = mutableListOf()
    private var sortedList: MutableList<Habit> = mutableListOf()
    private val viewModel: ListViewModel by viewModels {
        ListViewModelFactory(
            (requireActivity().application as App).appComponent.getAllHabitsUseCase(),
            (requireActivity().application as App).appComponent.getUpdateHabitsFromRemoteUseCase(),
        )
    }
    private var sortByOld: Boolean? = null
    private var sortByRegular: Boolean? = null
    private var sortByBad: Boolean? = null
    private var searchText: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.fillData(this)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.buttonFiltersShow.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        listenFilterButtons()

        binding.buttonApply.setOnClickListener {
            if (binding.buttonApply.text == getString(R.string.cancel)) {
                cancelFilters()
                binding.buttonApply.setText(R.string.apply)
                binding.buttonApply.setBackgroundColor(resources.getColor(R.color.yp_blue))
            } else {
                if ((sortByRegular == null) && (sortByBad == null) && (sortByOld == null)) {
                    //только поисковый текст
                    searchByText()
                } else if ((sortByRegular == null) && (sortByBad == null)) {
                    //сорт по дате
                    searchByText()
                    filterByData()
                } else if ((sortByRegular == null) && (sortByOld == null)) {
                    //сортировка по типу
                    searchByText()
                    filterByType()
                } else if ((sortByRegular != null) && (sortByBad == null) && (sortByOld == null)) {
                    //сортировка по частоте
                    searchByText()
                    filterByRegularity()
                } else if ((sortByRegular != null) && (sortByBad != null) && (sortByOld == null)) {
                    //сортировка по частоте и типу
                    searchByText()
                    filterByRegularity()
                    filterByType()
                } else if ((sortByRegular != null) && (sortByBad == null)) {
                    //сортировка по частоте и дате
                    searchByText()
                    filterByRegularity()
                    filterByData()
                } else if (sortByRegular == null)  {
                    //сортировка по частоте и дате
                    searchByText()
                    filterByType()
                    filterByData()
                } else {
                    //сортировка по частоте и дате, и по типу
                    searchByText()
                    filterByType()
                    filterByData()
                    filterByRegularity()
                }
                if ((sortByRegular != null) || (sortByBad != null) || (sortByOld != null) || (!searchText.isNullOrEmpty())) {
                    getData(sortedList)
                    if (sortedList.isEmpty()) {
                        showError()
                    } else getData(sortedList)
                    binding.buttonApply.setText(R.string.cancel)
                    binding.buttonApply.setBackgroundColor(resources.getColor(R.color.red))
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_no_filters), Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(context, getString(R.string.nothing_found_error), Toast.LENGTH_LONG).show()
    }

    private fun clickAdapting(item: Habit) {
        Log.d("FilterFragment", "Click on the habit")
        val bundle = Bundle()
        bundle.putParcelable("habit", item)
        val navController = findNavController()
        navController.navigate(R.id.addFragment, bundle)
    }

    private fun render(state: ListScreenState) {
        when (state) {
            is ListScreenState.Data -> getData(state.data)
            is ListScreenState.NoHabitsAdded -> showEmpty()
            is ListScreenState.Error -> showErrorLoading()
            is ListScreenState.Success -> success()
            is ListScreenState.Loading -> success()
        }
    }

    private fun success(){
        Log.d("SearchFragment", "Success in synchronizing data")
    }

    private fun showErrorLoading() {
        binding.noHabits.text = getString(R.string.error_getting_from_serv)
        binding.noHabits.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showEmpty() {
        binding.noHabits.text = getString(R.string.no_habits)
        binding.noHabits.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        habitsList.clear()
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(habits: List<Habit>) {
        binding.noHabits.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        habitsList = habits.toMutableList()

        recyclerView.adapter = HabitsRVAdapter(habitsList) {
            clickAdapting(it)
        }
        recyclerView.adapter?.notifyDataSetChanged()
        Log.d("FilterFragment", "$habitsList")
    }

    private fun listenFilterButtons() {

        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            searchText = text.toString()
        }

        binding.sortByDate.setOnClickListener {
            sortByOld = if (binding.sortByDate.text.toString() == (getString(R.string.sort_by_new))) {
                binding.sortByDate.setText(R.string.sort_by_old)
                true
            } else {
                binding.sortByDate.setText(R.string.sort_by_new)
                false
            }
        }

        binding.buttonPositive.setOnClickListener {
            sortByBad = false
        }

        binding.buttonNegative.setOnClickListener {
            sortByBad = true
        }

        binding.buttonRegular.setOnClickListener {
            sortByRegular = true
        }

        binding.buttonUnregular.setOnClickListener {
            sortByRegular = false
        }
    }

    private fun filterByRegularity() {
        if (sortByRegular == false) {
            var sorted = false
            while (!sorted) {
                sorted = true
                for (i in 1 until sortedList.size) {
                    val previous: Habit = sortedList[i - 1]
                    val current: Habit = sortedList[i]
                    if (previous.frequency > current.frequency) {
                        swap(sortedList, i - 1, i)
                        sorted = false
                    }
                }
            }
        } else {
            var sorted = false
            while (!sorted) {
                sorted = true
                for (i in 1 until sortedList.size) {
                    val previous: Habit = sortedList[i - 1]
                    val current: Habit = sortedList[i]
                    if (previous.frequency < current.frequency) {
                        swap(sortedList, i - 1, i)
                        sorted = false
                    }
                }
            }
        }
        Log.d("FilterFragment", "Sorted by reg $sortedList")
    }

    private fun filterByData() {
        sortedList = sortedList.asReversed()
        Log.d("Filter", "Sorted by data $sortedList")
    }

    private fun filterByType() {
        sortedList = if (sortByBad == true) {
            val list = sortedList.filter {
                it.type == Type.Bad
            }
            list.toMutableList()
        } else {
            val list = sortedList.filter {
                it.type == Type.Good
            }
            list.toMutableList()
        }
    }

    private fun searchByText() {
        if (!searchText.isNullOrEmpty() && searchText != "") {
            sortedList.clear()
            habitsList.forEach {
                if (it.title.lowercase() == searchText)
                    sortedList.add(it)
            }
        }
        Log.d("Filter", "Sorted by naming $sortedList")
    }

    private fun cancelFilters() {
        searchText = null
        sortByRegular = null
        sortByBad = null
        sortByOld = null
        binding.chooseType.clearCheck()
        binding.chooseFrequency.clearCheck()
        binding.editTextName.text.clear()
        viewModel.fillData(this)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        sortedList = habitsList
    }

}