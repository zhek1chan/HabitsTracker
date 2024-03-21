package com.example.habitstracker.fragments.ui

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.Habit
import com.example.habitstracker.color.ColorAdapter
import com.example.habitstracker.color.GridSpacingItemDecoration
import com.example.habitstracker.databinding.FragmentAddBinding
import com.example.habitstracker.db.AppDataBase
import com.example.habitstracker.db.HabitConvertor

class AddFragment(private var habit: Habit? = null) : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var selectedColor: Int? = habit?.color
    private var title: String? = habit?.title
    private var description: String? = habit?.description
    private var type: Int? = habit?.type
    private var priority: Int? = habit?.priority
    private var frequency: Int? = habit?.frequency
    private var count: Int? = habit?.count
    private lateinit var colorPickerDialog: AlertDialog
    private lateinit var stringHabit: String
    private val db = AppDataBase
    private val convertor = HabitConvertor()
    private var newHabit = Habit(0, "", "", 0, 0, 0, 0, 0)

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
        val prioritySpinner = binding.prioritySpinner
        setTextInputListeners()
        val arrayAdapter = prioritySpinner.adapter as ArrayAdapter<String>
        val selection = when (priority) {
            0 -> arrayAdapter.getPosition("Низкий")
            1 -> arrayAdapter.getPosition("Средний")
            else -> arrayAdapter.getPosition("Большой")
        }
        prioritySpinner.setSelection(selection)
        setColorPicker()
        saveButton()
        cancelButton()

    }

    private fun setTextInputListeners() {

        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            title = text.toString()
        }

        binding.editDescription.doOnTextChanged { text, _, _, _ ->
            description = text.toString()
            Log.d("Description", "$text")
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
                .setTitle("Выберите цвет")
                .setView(recyclerView)
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            colorPickerDialog.show()
        }
    }

    private fun saveButton() {
        val okBtn = binding.saveButton
        okBtn.setOnClickListener {
            setHabit()
            db(requireContext()).habitDao().insert(convertor.map(newHabit))
            Log.d("Inserted", "in db")
            val navController = findNavController()
            navController.navigateUp()
        }
    }

    private fun cancelButton() {
        val cancelBtn = binding.dontSaveButton
        cancelBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setHabit(): Boolean {

        setHabitType()
        setHabitPriority()

        if (title != null &&
            description != null &&
            type != null &&
            priority != null &&
            selectedColor != null &&
            frequency != null &&
            count != null
        ) {
            newHabit =
                Habit(
                    0,
                    title!!,
                    description!!,
                    type!!,
                    priority!!,
                    selectedColor!!,
                    frequency!!,
                    count!!
                )

            addHabit(newHabit)
            Log.d(
                "AddHabbitActivity",
                "$title, $description, $type, $priority, $selectedColor, $frequency, $count"
            )
            return true
        } else {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT)
                .show()
            Log.d(
                "AddHabbitActivity",
                "$title, $description, $type, $priority, $selectedColor, $frequency, $count"
            )
            return false
        }
    }

    private fun addHabit(newHabit: Habit) {
        stringHabit =
            "${newHabit.title},${newHabit.description},${newHabit.type},${newHabit.priority},${newHabit.title},${newHabit.color},${newHabit.title},${newHabit.frequency},${newHabit.count}"
    }

    private fun setHabitType() {
        val rGroupView = binding.chooseType

        val rButton = binding.root.findViewById<RadioButton>(rGroupView.checkedRadioButtonId)

        val stringType = rButton?.text.toString()
        type = when (stringType) {
            "Хороший" -> 0
            "Плохой" -> 1
            else -> null
        }
    }

    private fun setHabitPriority() {
        val priorityView = binding.prioritySpinner
        val stringPriority = priorityView.selectedItem.toString()
        Log.d("AddHabbitActivity", "какая-то хуита $stringPriority")
        priority = when (stringPriority) {
            "Низкий" -> 0
            "Средний" -> 1
            "Большой" -> 2
            else -> 2
        }
    }


    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    companion object {
        val key = "KEY"
    }
}