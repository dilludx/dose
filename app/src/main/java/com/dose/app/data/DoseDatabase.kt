package com.dose.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Medication::class, DoseHistory::class],
    version = 1,
    exportSchema = false
)
abstract class DoseDatabase : RoomDatabase() {
    
    abstract fun medicationDao(): MedicationDao
    abstract fun doseHistoryDao(): DoseHistoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: DoseDatabase? = null
        
        fun getDatabase(context: Context): DoseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoseDatabase::class.java,
                    "dose_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
