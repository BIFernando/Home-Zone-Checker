package com.example.home_zone_checker

import android.os.Bundle
import android.location.Location
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (!fineLocationGranted && !coarseLocationGranted) {
            Toast.makeText(this, "Location permission is required to use this feature.", Toast.LENGTH_SHORT).show()
        }
    }

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

        val btnCheckLocation = findViewById<Button>(R.id.btn_check_location)
        btnCheckLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}