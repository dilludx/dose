package com.dose.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dose.app.data.Medication
import java.util.Calendar

class AlarmScheduler(private val context: Context) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun scheduleMedicationAlarms(medication: Medication) {
        val times = medication.getTimesList()
        
        times.forEachIndexed { index, time ->
            val parts = time.split(":")
            if (parts.size == 2) {
                val hour = parts[0].toIntOrNull() ?: return@forEachIndexed
                val minute = parts[1].toIntOrNull() ?: return@forEachIndexed
                
                scheduleAlarm(
                    medicationId = medication.id,
                    medicationName = medication.name,
                    dosage = medication.dosage,
                    hour = hour,
                    minute = minute,
                    requestCode = (medication.id * 100 + index).toInt()
                )
            }
        }
    }
    
    private fun scheduleAlarm(
        medicationId: Long,
        medicationName: String,
        dosage: String,
        hour: Int,
        minute: Int,
        requestCode: Int
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("medication_id", medicationId)
            putExtra("medication_name", medicationName)
            putExtra("dosage", dosage)
            putExtra("request_code", requestCode)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If the time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        // Schedule repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    
    fun cancelMedicationAlarms(medication: Medication) {
        val times = medication.getTimesList()
        
        times.forEachIndexed { index, _ ->
            val requestCode = (medication.id * 100 + index).toInt()
            cancelAlarm(requestCode)
        }
    }
    
    private fun cancelAlarm(requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
