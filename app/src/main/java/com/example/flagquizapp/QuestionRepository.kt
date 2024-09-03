package com.example.flagquizapp

object QuestionRepository {

    val questionText = "לאיזה מדינה שייך הדגל?";

    val questions = listOf(
        Question(
            questionText =  questionText ,
            options = listOf("ארצות הברית", "ארגנטינה", "ישראל"),
            correctAnswerIndex = 1,
            imageResId = R.drawable.argentina
        ),
        Question(
            questionText = questionText,
            options = listOf("יוון", "האיים המלדיבים", "קפריסין"),
            correctAnswerIndex = 2,
            imageResId = R.drawable.cyprus
        ),
        Question(
            questionText = questionText,
            options = listOf("צ'כיה", "אסטוניה", "רומניה"),
            correctAnswerIndex = 0,
            imageResId = R.drawable.czech
        )
        // Add more questions here
    )
}