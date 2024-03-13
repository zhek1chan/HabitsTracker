package com.example.habitstracker

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColor
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitstracker.databinding.ActivityCreateHabbitBinding

class AddHabitActivity(private val habit: Habit? = null) : AppCompatActivity() {
    private lateinit var binding: ActivityCreateHabbitBinding
    private var selectedColor: Int? = habit?.color
    private var title: String? = habit?.title
    private var description: String? = habit?.description
    private var type: Int? = habit?.type
    private var priority: Int? = habit?.priority
    private var frequency: Int? = habit?.frequency
    private var count: Int? = habit?.count
    private lateinit var colorPickerDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHabbitBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

            val recyclerView = RecyclerView(this).apply {
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

            colorPickerDialog = AlertDialog.Builder(this)
                .setTitle("Выберите цвет")
                .setView(recyclerView)
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            colorPickerDialog.show()
        }
    }

    private fun setColor(circle: ImageView) {
        circle.backgroundTintList = ColorStateList.valueOf(selectedColor!!)
    }

    private fun saveButton() {
        val okBtn = binding.saveButton
        okBtn.setOnClickListener {
            setHabit()
            val intent = Intent(this, AddHabitActivity::class.java)
            Log.d("AddHabitActivity", "Navigate to the MainActivity by pButton")
            startActivity(intent)
        }
    }

    private fun cancelButton() {
        val cancelBtn = binding.buttonNegative
        cancelBtn.setOnClickListener {
            val intent = Intent(this, AddHabitActivity::class.java)
            Log.d("AddHabitActivity", "Navigate to the MainActivity by nButton")
            startActivity(intent)
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
            val newHabit =
                Habit(
                    title!!,
                    description!!,
                    type!!,
                    priority!!,
                    selectedColor!!,
                    frequency!!,
                    count!!
                )

            addHabit(newHabit)
            return true
        } else {
            Toast.makeText(this.applicationContext, "Заполните все поля!", Toast.LENGTH_SHORT)
                .show()
            setHabit()
            return true
        }
    }

    private fun addHabit(newHabit: Habit) {

    }

    private fun setHabitType() {
        val rGroupView = binding.chooseType

        val rButton = findViewById<RadioButton>(rGroupView.checkedRadioButtonId)

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

        priority = when (stringPriority) {
            "Низкий" -> 0
            "Средний" -> 1
            "Большой" -> 2
            else -> null
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}