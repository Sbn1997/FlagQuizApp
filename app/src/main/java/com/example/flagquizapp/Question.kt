package com.example.flagquizapp

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val imageResId: Int
)