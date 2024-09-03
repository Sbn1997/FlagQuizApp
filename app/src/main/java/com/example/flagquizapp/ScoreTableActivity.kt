package com.example.flagquizapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flagquizapp.databinding.ActivityScoreTableBinding

class ScoreTableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreTableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "טבלת ניקוד"

        // Handle the Up button
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve high scores from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val topScores = getTopScores(sharedPreferences)

        // Set up RecyclerView adapter with the top scores list
        recyclerView.adapter = ScoreAdapter(topScores)
    }

    private fun getTopScores(sharedPreferences:
                             SharedPreferences): List<Int> {
        val topScoresString = sharedPreferences.getString("topScores", "") ?: ""
        return if (topScoresString.isEmpty()) {
            listOf() // Default to an empty list
        } else {
            topScoresString.split(",").map { it.toInt() }
        }
    }
}
