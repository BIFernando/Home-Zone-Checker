package com.example.home_zone_checker

import android.os.Bundle
import android.location.Location
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * Checks if the user's current location is within a 200-meter radius of the hardcoded
     * campus reference point and updates the UI accordingly.
     *
     * @param currentLocation The user's current [Location].
     */
    private fun checkZone(currentLocation: Location) {
        // Hardcoded reference point (e.g., center of a campus)
        val campusLatitude = 37.4220
        val campusLongitude = -122.0841
        val radiusInMeters = 200.0

        val referenceLocation = Location("Campus").apply {
            latitude = campusLatitude
            longitude = campusLongitude
        }

        // Calculate distance in meters
        val distance = currentLocation.distanceTo(referenceLocation)

        // Determine status
        val status = if (distance <= radiusInMeters) "Inside Zone" else "Outside Zone"

        // Update UI components
        findViewById<TextView>(R.id.statusText).text = status
        findViewById<TextView>(R.id.distanceText).text = "Distance: ${"%.2f".format(distance)} meters"
    }
}