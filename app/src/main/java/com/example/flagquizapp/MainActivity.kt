package com.example.flagquizapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flagquizapp.databinding.ActivityMainBinding
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val questions = QuestionRepository.questions

    private var currentQuestionIndex = 0
    private var score = 0
    private var mistakes = 0
    private var allowedErrors = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        val selectedDifficulty = sharedPreferences.getString("userDifficultyLevel", "קל") ?: "קל"
        allowedErrors = getAllowedErrors(selectedDifficulty)

        // Display  first question
        displayQuestion()

        binding.option1Button.setOnClickListener { checkAnswer(0) }
        binding.option2Button.setOnClickListener { checkAnswer(1) }
        binding.option3Button.setOnClickListener { checkAnswer(2) }

        binding.restartButton.setOnClickListener {
            restartQuiz()
        }
    }
    // fun to determined mistakes amount
    private fun getAllowedErrors(difficulty: String): Int {
        return when (difficulty) {
            "קל" -> 4
            "בינוני" -> 3
            "קשה" -> 2
            "מומחה" -> 1
            else -> 4 //
        }
    }
    // fun to display quesions
    private fun displayQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        binding.questionText.text = currentQuestion.questionText
        binding.option1Button.text = currentQuestion.options[0]
        binding.option2Button.text = currentQuestion.options[1]
        binding.option3Button.text = currentQuestion.options[2]
        binding.flagImageView.setImageResource(currentQuestion.imageResId)


        binding.option1Button.setBackgroundColor(resources.getColor(R.color.navyBlue, null))
        binding.option2Button.setBackgroundColor(resources.getColor(R.color.navyBlue, null))
        binding.option3Button.setBackgroundColor(resources.getColor(R.color.navyBlue, null))


        binding.option1Button.isEnabled = true
        binding.option2Button.isEnabled = true
        binding.option3Button.isEnabled = true
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questions[currentQuestionIndex]
        val selectedButton = when (selectedIndex) {
            0 -> binding.option1Button
            1 -> binding.option2Button
            2 -> binding.option3Button
            else -> binding.option1Button // Default
        }

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            score++
            selectedButton.setBackgroundColor(resources.getColor(R.color.green, null))
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            mistakes++
            selectedButton.setBackgroundColor(resources.getColor(R.color.red, null))
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show()
            if (mistakes >= allowedErrors) {
                endQuiz()
                return
            }
        }

        // Disable  buttons
        binding.option1Button.isEnabled = false
        binding.option2Button.isEnabled = false
        binding.option3Button.isEnabled = false

        // next question
        selectedButton.postDelayed({ goToNextQuestion() }, 1000) // 1-second delay
    }

    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion()
        } else {
            endQuiz()
        }
    }
    //fun that runs at lose or end of questions
    private fun endQuiz() {
        binding.questionText.text = "המשחק נגמר הניקוד שלך הוא:  $score"
        binding.option1Button.isEnabled = false
        binding.option2Button.isEnabled = false
        binding.option3Button.isEnabled = false
        binding.restartButton.isEnabled = true


        val topScores = getTopScores()

        if (score > topScores.minOrNull()!!) {
            topScores.add(score)
            topScores.sortDescending()

            // Keep only the top 10 scores
            if (topScores.size > 10) {
                topScores.removeAt(topScores.lastIndex)
            }

            // Save the updated top scores back to SharedPreferences
            saveTopScores(topScores)

            Toast.makeText(this, "New high score!", Toast.LENGTH_SHORT).show()
        }



        val highScore = sharedPreferences.getInt("highestScore", 0)
        if (score > highScore) {
            with(sharedPreferences.edit()) {
                putInt("highestScore", score)
                apply()
            }
        }

    }

    private fun getTopScores(): MutableList<Int> {
        val topScoresString = sharedPreferences.getString("topScores", "") ?: ""
        return if (topScoresString.isEmpty()) {
            MutableList(10) { 0 } // Default to an array of 10 zeros
        } else {
            topScoresString.split(",").map { it.toInt() }.toMutableList()
        }
    }

    private fun saveTopScores(topScores: List<Int>) {
        val topScoresString = topScores.joinToString(",")
        with(sharedPreferences.edit()) {
            putString("topScores", topScoresString)
            apply()
        }
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        mistakes = 0
        binding.option1Button.isEnabled = true
        binding.option2Button.isEnabled = true
        binding.option3Button.isEnabled = true
        binding.restartButton.isEnabled = false
        displayQuestion()
    }
}
