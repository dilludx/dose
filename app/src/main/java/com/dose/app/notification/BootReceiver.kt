package com.dose.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dose.app.data.DoseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all medication alarms after device restart
            rescheduleAllAlarms(context)
        }
    }
    
    private fun rescheduleAllAlarms(context: Context) {
        val database = DoseDatabase.getDatabase(context)
        val alarmScheduler = AlarmScheduler(context)
        
        CoroutineScope(Dispatchers.IO).launch {
            val medications = database.medicationDao().getAllActiveMedications().first()
            medications.forEach { medication ->
                alarmScheduler.scheduleMedicationAlarms(medication)
            }
        }
    }
}
