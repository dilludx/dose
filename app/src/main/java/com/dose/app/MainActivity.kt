package com.dose.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dose.app.ui.navigation.DoseNavigation
import com.dose.app.ui.navigation.Screen
import com.dose.app.ui.theme.DoseTheme
import com.dose.app.viewmodel.MedicationViewModel

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val PREFS_NAME = "dose_prefs"
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
    }
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled - notifications will work if granted
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request notification permission for Android 13+
        requestNotificationPermission()
        
        // Check if onboarding is complete
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingComplete = prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false)
        
        setContent {
            DoseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MedicationViewModel = viewModel()
                    
                    // Determine start destination
                    val startDestination = if (onboardingComplete) {
                        Screen.Home.route
                    } else {
                        Screen.Welcome.route
                    }
                    
                    // Mark onboarding as complete when navigating to Home
                    LaunchedEffect(navController) {
                        navController.addOnDestinationChangedListener { _, destination, _ ->
                            if (destination.route == Screen.Home.route && !onboardingComplete) {
                                prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETE, true).apply()
                            }
                        }
                    }
                    
                    DoseNavigation(
                        navController = navController,
                        viewModel = viewModel,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}
