package com.dose.app.data

import kotlinx.coroutines.flow.Flow

class MedicationRepository(
    private val medicationDao: MedicationDao,
    private val doseHistoryDao: DoseHistoryDao
) {
    
    val allActiveMedications: Flow<List<Medication>> = medicationDao.getAllActiveMedications()
    val allMedications: Flow<List<Medication>> = medicationDao.getAllMedications()
    
    suspend fun getMedicationById(id: Long): Medication? {
        return medicationDao.getMedicationById(id)
    }
    
    suspend fun insertMedication(medication: Medication): Long {
        return medicationDao.insertMedication(medication)
    }
    
    suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(medication)
    }
    
    suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(medication)
    }
    
    suspend fun decrementPills(medicationId: Long, amount: Int) {
        medicationDao.decrementPills(medicationId, amount)
    }
    
    fun getHistoryForDate(date: String): Flow<List<DoseHistory>> {
        return doseHistoryDao.getHistoryForDate(date)
    }
    
    fun getHistoryForMedication(medicationId: Long): Flow<List<DoseHistory>> {
        return doseHistoryDao.getHistoryForMedication(medicationId)
    }
    
    suspend fun insertDoseHistory(doseHistory: DoseHistory): Long {
        return doseHistoryDao.insertDoseHistory(doseHistory)
    }
    
    suspend fun markDoseTaken(id: Long) {
        doseHistoryDao.markDose(id, "taken", System.currentTimeMillis())
    }
    
    suspend fun markDoseSkipped(id: Long) {
        doseHistoryDao.markDose(id, "skipped", null)
    }
    
    suspend fun getTakenCountForDate(date: String): Int {
        return doseHistoryDao.getTakenCountForDate(date)
    }
    
    suspend fun getTotalCountForDate(date: String): Int {
        return doseHistoryDao.getTotalCountForDate(date)
    }
}
