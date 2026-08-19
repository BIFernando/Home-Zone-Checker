package com.example.home_zone_checker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var statusText: TextView
    private lateinit var distanceText: TextView
    private lateinit var locationDisplay: TextView
    private lateinit var checkLocationButton: Button

    // Request location permissions
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val fineLocationGranted =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission is required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Initialize location client
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        // Connect XML views
        statusText = findViewById(R.id.statusText)
        distanceText = findViewById(R.id.distanceText)
        locationDisplay = findViewById(R.id.locationDisplay)
        checkLocationButton = findViewById(R.id.btn_check_location)

        // Button click
        checkLocationButton.setOnClickListener {
            checkLocationPermission()
        }
    }

    /**
     * Check whether location permission has already been granted.
     */
    private fun checkLocationPermission() {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {

            // Permission already granted
            getCurrentLocation()

        } else {

            // Ask the user for permission
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Get the user's current location.
     */
    private fun getCurrentLocation() {

        // Double-check permissions
        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            return
        }

        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
            .addOnSuccessListener { location: Location? ->

                if (location != null) {

                    // Display latitude and longitude
                    displayLocation(location)

                    // Check whether the user is inside the zone
                    checkZone(location)

                } else {

                    Toast.makeText(
                        this,
                        "Unable to retrieve location. Make sure GPS is turned on.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    this,
                    "Error getting location: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    /**
     * Display the user's current latitude and longitude.
     */
    private fun displayLocation(location: Location) {

        val latitude = location.latitude
        val longitude = location.longitude

        locationDisplay.text =
            "Current Location:\n" +
                    "Latitude: $latitude\n" +
                    "Longitude: $longitude"
    }

    /**
     * Check whether the user's location is inside
     * the defined campus/home zone.
     */
    private fun checkZone(currentLocation: Location) {

        // Reference point
        // Change these coordinates to your required location.
        val zoneLatitude = 6.9742
        val zoneLongitude = 79.9154

        // Allowed radius
        val radiusInMeters = 200.0

        // Create reference location
        val referenceLocation = Location("Zone").apply {
            latitude = zoneLatitude
            longitude = zoneLongitude
        }

        // Calculate distance between
        // user's location and reference point
        val distance =
            currentLocation.distanceTo(referenceLocation)

        // Check whether user is inside the zone
        val status =
            if (distance <= radiusInMeters) {
                "Inside Zone"
            } else {
                "Outside Zone"
            }

        // Update UI
        statusText.text = status

        distanceText.text =
            "Distance: ${"%.2f".format(distance)} meters"
    }
}