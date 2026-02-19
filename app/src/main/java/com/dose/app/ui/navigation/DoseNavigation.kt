package com.dose.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dose.app.ui.screens.AddMedicationScreen
import com.dose.app.ui.screens.EditMedicationScreen
import com.dose.app.ui.screens.HomeScreen
import com.dose.app.ui.screens.MedicationDetailScreen
import com.dose.app.ui.screens.WelcomeScreen
import com.dose.app.viewmodel.MedicationViewModel

@Composable
fun DoseNavigation(
    navController: NavHostController,
    viewModel: MedicationViewModel,
    startDestination: String = Screen.Welcome.route
) {
    val medications by viewModel.medications.collectAsState()
    val todayHistory by viewModel.todayHistory.collectAsState()
    val todayStats by viewModel.todayStats.collectAsState()
    val selectedMedication by viewModel.selectedMedication.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                medications = medications,
                todayHistory = todayHistory,
                todayStats = todayStats,
                onAddClick = {
                    navController.navigate(Screen.AddMedication.route)
                },
                onMedicationClick = { medication ->
                    viewModel.selectMedication(medication.id)
                    navController.navigate(Screen.MedicationDetail.createRoute(medication.id))
                },
                onMarkTaken = { doseId, medicationId ->
                    viewModel.markDoseTaken(doseId, medicationId)
                },
                onMarkSkipped = { doseId ->
                    viewModel.markDoseSkipped(doseId)
                }
            )
        }
        
        // Add Medication Screen
        composable(Screen.AddMedication.route) {
            AddMedicationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSave = { name, dosage, frequency, times, instructions, pillsRemaining, pillsPerDose ->
                    viewModel.addMedication(
                        name = name,
                        dosage = dosage,
                        frequency = frequency,
                        times = times,
                        instructions = instructions,
                        pillsRemaining = pillsRemaining,
                        pillsPerDose = pillsPerDose
                    )
                    navController.popBackStack()
                }
            )
        }
        
        // Medication Detail Screen
        composable(
            route = Screen.MedicationDetail.route,
            arguments = listOf(
                navArgument("medicationId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getLong("medicationId") ?: return@composable
            
            selectedMedication?.let { medication ->
                val history by viewModel.todayHistory.collectAsState()
                val medicationHistory = history.filter { it.medicationId == medicationId }
                
                MedicationDetailScreen(
                    medication = medication,
                    history = medicationHistory,
                    onNavigateBack = {
                        viewModel.clearSelectedMedication()
                        navController.popBackStack()
                    },
                    onEdit = {
                        navController.navigate(Screen.EditMedication.createRoute(medicationId))
                    },
                    onDelete = {
                        viewModel.deleteMedication(medication)
                        navController.popBackStack()
                    }
                )
            }
        }
        
        // Edit Medication Screen
        composable(
            route = Screen.EditMedication.route,
            arguments = listOf(
                navArgument("medicationId") { type = NavType.LongType }
            )
        ) {
            selectedMedication?.let { medication ->
                EditMedicationScreen(
                    medication = medication,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSave = { updatedMedication ->
                        viewModel.updateMedication(updatedMedication)
                        // Go back to detail screen
                        navController.popBackStack()
                        // Refresh the selected medication
                        viewModel.selectMedication(updatedMedication.id)
                    }
                )
            }
        }
    }
}
