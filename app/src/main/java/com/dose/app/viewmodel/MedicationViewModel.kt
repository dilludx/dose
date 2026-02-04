package com.dose.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dose.app.DoseApplication
import com.dose.app.data.DoseHistory
import com.dose.app.data.Medication
import com.dose.app.data.MedicationRepository
import com.dose.app.notification.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = (application as DoseApplication).database
    private val repository = MedicationRepository(
        database.medicationDao(),
        database.doseHistoryDao()
    )
    private val alarmScheduler = AlarmScheduler(application)
    
    private val _medications = MutableStateFlow<List<Medication>>(emptyList())
    val medications: StateFlow<List<Medication>> = _medications.asStateFlow()
    
    private val _todayHistory = MutableStateFlow<List<DoseHistory>>(emptyList())
    val todayHistory: StateFlow<List<DoseHistory>> = _todayHistory.asStateFlow()
    
    private val _selectedMedication = MutableStateFlow<Medication?>(null)
    val selectedMedication: StateFlow<Medication?> = _selectedMedication.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _todayStats = MutableStateFlow(Pair(0, 0)) // taken, total
    val todayStats: StateFlow<Pair<Int, Int>> = _todayStats.asStateFlow()
    
    init {
        loadMedications()
        loadTodayHistory()
        loadTodayStats()
    }
    
    private fun loadMedications() {
        viewModelScope.launch {
            repository.allActiveMedications.collect { meds ->
                _medications.value = meds
            }
        }
    }
    
    private fun loadTodayHistory() {
        viewModelScope.launch {
            val today = getTodayDateString()
            repository.getHistoryForDate(today).collect { history ->
                _todayHistory.value = history
            }
        }
    }
    
    private fun loadTodayStats() {
        viewModelScope.launch {
            val today = getTodayDateString()
            val taken = repository.getTakenCountForDate(today)
            val total = repository.getTotalCountForDate(today)
            _todayStats.value = Pair(taken, total)
        }
    }
    
    fun addMedication(
        name: String,
        dosage: String,
        frequency: String,
        times: List<String>,
        instructions: String,
        pillsRemaining: Int,
        pillsPerDose: Int
    ) {
        viewModelScope.launch {
            val medication = Medication(
                name = name,
                dosage = dosage,
                frequency = frequency,
                times = times.joinToString(","),
                instructions = instructions,
                pillsRemaining = pillsRemaining,
                pillsPerDose = pillsPerDose
            )
            val id = repository.insertMedication(medication)
            val savedMedication = medication.copy(id = id)
            
            // Schedule alarms for this medication
            alarmScheduler.scheduleMedicationAlarms(savedMedication)
            
            // Create today's dose history entries
            createTodayDoseEntries(savedMedication)
        }
    }
    
    fun updateMedication(medication: Medication) {
        viewModelScope.launch {
            // Cancel old alarms
            alarmScheduler.cancelMedicationAlarms(medication)
            
            // Update in database
            repository.updateMedication(medication)
            
            // Schedule new alarms
            alarmScheduler.scheduleMedicationAlarms(medication)
        }
    }
    
    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            // Cancel alarms
            alarmScheduler.cancelMedicationAlarms(medication)
            
            // Delete from database
            repository.deleteMedication(medication)
        }
    }
    
    fun selectMedication(id: Long) {
        viewModelScope.launch {
            _selectedMedication.value = repository.getMedicationById(id)
        }
    }
    
    fun clearSelectedMedication() {
        _selectedMedication.value = null
    }
    
    fun markDoseTaken(doseHistoryId: Long, medicationId: Long) {
        viewModelScope.launch {
            repository.markDoseTaken(doseHistoryId)
            
            // Decrement pill count
            val medication = repository.getMedicationById(medicationId)
            medication?.let {
                repository.decrementPills(medicationId, it.pillsPerDose)
            }
            
            loadTodayStats()
        }
    }
    
    fun markDoseSkipped(doseHistoryId: Long) {
        viewModelScope.launch {
            repository.markDoseSkipped(doseHistoryId)
            loadTodayStats()
        }
    }
    
    private suspend fun createTodayDoseEntries(medication: Medication) {
        val today = getTodayDateString()
        val times = medication.getTimesList()
        
        times.forEach { time ->
            val parts = time.split(":")
            if (parts.size == 2) {
                val scheduledTime = getTimeInMillis(parts[0].toInt(), parts[1].toInt())
                
                val doseHistory = DoseHistory(
                    medicationId = medication.id,
                    medicationName = medication.name,
                    scheduledTime = scheduledTime,
                    date = today
                )
                repository.insertDoseHistory(doseHistory)
            }
        }
        loadTodayHistory()
        loadTodayStats()
    }
    
    private fun getTodayDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    
    private fun getTimeInMillis(hour: Int, minute: Int): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
        calendar.set(java.util.Calendar.MINUTE, minute)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
