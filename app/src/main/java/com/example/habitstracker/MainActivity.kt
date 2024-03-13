package com.example.habitstracker


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.habitstracker.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Добавить новую привычку", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            val intent = Intent(this, AddHabitActivity::class.java)
            Log.d("MainActivity", "Navigate to the AddHabitActivity")
            startActivity(intent)
        }
    }
}