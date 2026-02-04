package com.dose.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    
    @Query("SELECT * FROM medications WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveMedications(): Flow<List<Medication>>
    
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAllMedications(): Flow<List<Medication>>
    
    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): Medication?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: Medication): Long
    
    @Update
    suspend fun updateMedication(medication: Medication)
    
    @Delete
    suspend fun deleteMedication(medication: Medication)
    
    @Query("UPDATE medications SET pillsRemaining = pillsRemaining - :amount WHERE id = :medicationId")
    suspend fun decrementPills(medicationId: Long, amount: Int)
    
    @Query("UPDATE medications SET isActive = :isActive WHERE id = :medicationId")
    suspend fun setMedicationActive(medicationId: Long, isActive: Boolean)
}

@Dao
interface DoseHistoryDao {
    
    @Query("SELECT * FROM dose_history WHERE date = :date ORDER BY scheduledTime ASC")
    fun getHistoryForDate(date: String): Flow<List<DoseHistory>>
    
    @Query("SELECT * FROM dose_history WHERE medicationId = :medicationId ORDER BY scheduledTime DESC LIMIT 30")
    fun getHistoryForMedication(medicationId: Long): Flow<List<DoseHistory>>
    
    @Query("SELECT * FROM dose_history WHERE date = :date AND medicationId = :medicationId")
    suspend fun getHistoryForMedicationOnDate(medicationId: Long, date: String): List<DoseHistory>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoseHistory(doseHistory: DoseHistory): Long
    
    @Update
    suspend fun updateDoseHistory(doseHistory: DoseHistory)
    
    @Query("UPDATE dose_history SET status = :status, takenTime = :takenTime WHERE id = :id")
    suspend fun markDose(id: Long, status: String, takenTime: Long?)
    
    @Query("SELECT COUNT(*) FROM dose_history WHERE medicationId = :medicationId AND status = 'taken'")
    suspend fun getTakenCountForMedication(medicationId: Long): Int
    
    @Query("SELECT COUNT(*) FROM dose_history WHERE date = :date AND status = 'taken'")
    suspend fun getTakenCountForDate(date: String): Int
    
    @Query("SELECT COUNT(*) FROM dose_history WHERE date = :date")
    suspend fun getTotalCountForDate(date: String): Int
}
