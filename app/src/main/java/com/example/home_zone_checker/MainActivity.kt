package com.example.home_zone_checker

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkLocationButton = findViewById<Button>(R.id.checkLocationButton)

        checkLocationButton.setOnClickListener {
            // Location logic to be implemented later
            Toast.makeText(this, "Checking location...", Toast.LENGTH_SHORT).show()
        }
    }
}
