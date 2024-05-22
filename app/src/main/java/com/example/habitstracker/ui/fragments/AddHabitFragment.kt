package com.example.habitstracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.R
import com.example.habitstracker.databinding.FragmentAddBinding
import com.example.habitstracker.domain.color.ColorAdapter
import com.example.habitstracker.domain.color.GridSpacingItemDecoration
import com.example.habitstracker.domain.models.Habit
import com.example.habitstracker.domain.models.Priority
import com.example.habitstracker.domain.models.Type
import com.example.habitstracker.ui.view_models.AddHabitViewModel

class AddHabitFragment(habit: Habit? = null) : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var id: Int? = habit?.id
    private var uid: String? = habit?.uid
    private var selectedColor: Int? = habit?.color
    private var title: String? = habit?.title
    private var description: String? = habit?.description
    private var type: Type? = habit?.type
    private var priority: Priority? = habit?.priority
    private var frequency: Int? = habit?.frequency
    private var count: Int? = habit?.count
    private lateinit var colorPickerDialog: AlertDialog
    private var newHabit = Habit(null, "","", "", Type.Bad, Priority.Low, 0, 0, 0)
    private val viewModel by viewModels<AddHabitViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.getParcelable<Habit>("habit") != null) {
            val h = arguments?.getParcelable<Habit>("habit")!!

            Log.d("AddHabitFragment", "Change habit $h")
            id = h.id!!
            uid = h.uid
            binding.editTextName.setText(h.title)
            title = h.title

            binding.editDescription.setText(h.description)
            description = h.description

            binding.prioritySpinner.setSelection(getIntFromPriority(h.priority))
            priority = h.priority

            if (h.type == Type.Good) {
                binding.buttonPositive.performClick()
            } else binding.buttonNegative.performClick()
            type = h.type

            binding.colorPickerButton.setBackgroundColor(h.color)
            selectedColor = h.color

            binding.addPeriod.setText(h.frequency.toString())
            frequency = h.frequency

            binding.addCount.setText(h.count.toString())
            count = h.count

            binding.saveButton.text = getString(R.string.save)
            binding.nameOfScreen.text = getString(R.string.edit_habit)
            binding.deleteButton.visibility = View.VISIBLE
        } else {
            setColorPicker()
        }
        val prioritySpinner = binding.prioritySpinner
        setTextInputListeners()
        val arrayAdapter = prioritySpinner.adapter as ArrayAdapter<String>
        val selection = when (priority) {
            Priority.Low -> arrayAdapter.getPosition("Низкий")
            Priority.Medium -> arrayAdapter.getPosition("Средний")
            else -> arrayAdapter.getPosition("Большой")
        }
        prioritySpinner.setSelection(selection)
        saveButton()
        cancelButton()
        deleteButton()
    }

    private fun getIntFromPriority(p: Priority) : Int {
        return when (p) {
            Priority.Max -> {
                2
            }
            Priority.Medium -> {
                1
            }
            else -> 0
        }
    }

    private fun setTextInputListeners() {

        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            title = text.toString()
        }

        binding.editDescription.doOnTextChanged { text, _, _, _ ->
            description = text.toString()
        }

        binding.addPeriod.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty())
                frequency = text.toString().toInt()
        }

        binding.addCount.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty())
                count = text.toString().toInt()
        }
    }

    private fun setColorPicker() {

        val circle = binding.colorPickerButton

        circle.setOnClickListener {
            val colors = listOf(
                Color.CYAN,
                Color.rgb(179, 157, 219),
                Color.MAGENTA,
                Color.rgb(245, 245, 220),
                Color.YELLOW,
                Color.rgb(169, 169, 169),
                Color.GREEN,
                Color.rgb(244, 164, 96),
                Color.BLUE,
                Color.RED,
                Color.rgb(255, 228, 181),
                Color.rgb(72, 61, 139),
                Color.rgb(205, 92, 92),
                Color.rgb(255, 165, 0),
                Color.rgb(102, 205, 170)
            )

            val numColumns = 5 // Desired number of columns
            val padding = dpToPx(15) // Convert 15 dp to pixels
            val spacing = dpToPx(15) // Set the spacing between items in dp

            val recyclerView = RecyclerView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                layoutManager = GridLayoutManager(this.context, numColumns)
                setPadding(padding, dpToPx(20), padding, padding) // Convert padding to pixels
                adapter = ColorAdapter(this.context, colors) { color ->
                    // Do something with the selected color
                    circle.setBackgroundColor(color)
                    selectedColor = color
                    colorPickerDialog.dismiss()
                }
                addItemDecoration(GridSpacingItemDecoration(numColumns, spacing, true))
            }

            colorPickerDialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.pick_color))
                .setView(recyclerView)
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            colorPickerDialog.show()
        }
    }

    private fun saveButton() {
        val okBtn = binding.saveButton
        okBtn.setOnClickListener {
            if (setHabit()) {
                if ((id != 0) && (id != null)) {
                    viewModel.updateItem(newHabit, requireContext())
                    Log.d("AddHabitFragment", "Changed in db")
                } else {
                    viewModel.addItem(newHabit, requireContext())
                    Log.d("AddHabitFragment", "Inserted in db")
                }
                Log.d("AddHabitFragment", "$newHabit")
                val navController = findNavController()
                navController.navigateUp()
            }
        }
    }

    private fun cancelButton() {
        val cancelBtn = binding.dontSaveButton
        cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun deleteButton() {
        val deleteBtn = binding.deleteButton
        deleteBtn.setOnClickListener {
            viewModel.deleteItem(newHabit, requireContext())
            findNavController().navigateUp()
        }
    }

    private fun setHabit(): Boolean {
        setHabitType()
        setHabitPriority()
        setColorPicker()

        if (title != null &&
            description != null &&
            type != null &&
            priority != null &&
            selectedColor != null &&
            frequency != null &&
            count != null
        ) {
            if ((id == 0) || (id == null)) {
                newHabit =
                    Habit(
                        0,
                        "",
                        title!!,
                        description!!,
                        type!!,
                        priority!!,
                        selectedColor!!,
                        frequency!!,
                        count!!
                    )
            } else {
                newHabit =
                    Habit(
                        id,
                        uid!!,
                        title!!,
                        description!!,
                        type!!,
                        priority!!,
                        selectedColor!!,
                        frequency!!,
                        count!!
                    )
            }
            return true
        } else {
            Log.d("AddHabitFragment", "$newHabit")
            Toast.makeText(requireContext(), getString(R.string.alert), Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }

    private fun setHabitType() {
        val rGroupView = binding.chooseType

        val rButton = binding.root.findViewById<RadioButton>(rGroupView.checkedRadioButtonId)

        val stringType = rButton?.text.toString()
        type = when (stringType) {
            getString(R.string.type_good) -> Type.Good
            getString(R.string.type_bad) -> Type.Bad
            else -> null
        }
    }

    private fun setHabitPriority() {
        val priorityView = binding.prioritySpinner
        val stringPriority = priorityView.selectedItem.toString()
        priority = when (stringPriority) {
            "Низкий" -> Priority.Low
            "Средний" -> Priority.Medium
            "Большой" -> Priority.Max
            else -> Priority.Medium
        }
    }


    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

}