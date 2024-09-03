package com.example.flagquizapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import android.view.View
import android.widget.AdapterView
import android.widget.Button  // Import for Button
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.flagquizapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPreferences: SharedPreferences



    private val difficultyToErrorsMap = mapOf(
        "קל" to 4,
        "בינוני" to 3,
        "קשה" to 2,
        "מומחה" to 1
    )

    private fun getAllowedErrors(difficulty: String): Int {
        return difficultyToErrorsMap[difficulty] ?: 4 // Default to 4 errors if something goes wrong
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        // Load saved user data
        loadUserData()

        // Set up the difficulty spinner
        setupDifficultySpinner()

        // Handle Edit Name button click
        binding.editNameButton.setOnClickListener {
            // Show a dialog to edit the name
            editName()
        }

        val scoreTableButton: Button = findViewById(R.id.btn_score_table)
        scoreTableButton.setOnClickListener {
            val intent = Intent(this, ScoreTableActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()  // Handle the back action using the dispatcher
        return true
    }

    private fun setupDifficultySpinner() {
        val difficultyLevels = arrayOf("קל", "בינוני", "קשה", "מומחה")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.difficultySpinner.adapter = adapter

        // Set the spinner selection based on saved data
        val savedDifficultyLevel = sharedPreferences.getInt("difficultyLevel", 0)
        binding.difficultySpinner.setSelection(savedDifficultyLevel)

        // Set an item selected listener to update the allowed errors
        binding.difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDifficulty = difficultyLevels[position]
                val allowedErrors = getAllowedErrors(selectedDifficulty)
                saveUserData()
                Toast.makeText(this@ProfileActivity, "Allowed errors: $allowedErrors", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }
    //edit player name
    private fun editName() {
        val nameInput = EditText(this)
        nameInput.setText(binding.nameText.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(nameInput)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = nameInput.text.toString().trim()
                if (newName.isNotEmpty()) {
                    // Update both nameText and profileNameText
                    binding.nameText.text = newName
                    binding.profileNameText.text = newName
                    saveUserData() // Save the name immediately
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
    // load user details for profile
    private fun loadUserData() {
        val userName = sharedPreferences.getString("userName", "")
        if (userName.isNullOrEmpty()) {
            binding.nameText.text = "שם השחקן" // Default if empty
            binding.profileNameText.text = "שם השחקן"
        } else {
            binding.nameText.text = userName
            binding.profileNameText.text = userName
        }

        val highScore = sharedPreferences.getInt("highestScore", 0)
        binding.highestScoreText.text = "הניקוד הכי גבוה: $highScore"
    }

    private fun saveUserData() {
        val selectedDifficulty = binding.difficultySpinner.selectedItem.toString()
        val userName = binding.nameText.text.toString()

        with(sharedPreferences.edit()) {
            putString("userName", userName)  // Save the name, even if it hasn't changed
            putString("userDifficultyLevel", selectedDifficulty)  // Save the difficulty level
            apply()
        }
    }
}
