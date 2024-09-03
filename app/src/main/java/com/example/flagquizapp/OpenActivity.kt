package com.example.flagquizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flagquizapp.databinding.ActivityOpenBinding

class OpenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpenBinding.inflate(layoutInflater)


        setContentView(binding.root)


        binding.startGameButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
