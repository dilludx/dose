package com.dose.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: String,
    val frequency: String, // "Daily", "Twice Daily", "Weekly", etc.
    val times: String, // Comma-separated times like "08:00,14:00,20:00"
    val instructions: String = "", // "Take with food", "Before meals", etc.
    val startDate: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val pillsRemaining: Int = 0,
    val pillsPerDose: Int = 1,
    val refillReminder: Int = 10, // Remind when X pills remaining
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getTimesList(): List<String> {
        return times.split(",").filter { it.isNotBlank() }
    }
}

@Entity(tableName = "dose_history")
data class DoseHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medicationId: Long,
    val medicationName: String,
    val scheduledTime: Long,
    val takenTime: Long? = null,
    val status: String = "pending", // "pending", "taken", "skipped", "missed"
    val date: String // Format: "2024-01-15"
)
