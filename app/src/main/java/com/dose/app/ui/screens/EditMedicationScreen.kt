package com.dose.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dose.app.data.Medication
import com.dose.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationScreen(
    medication: Medication,
    onNavigateBack: () -> Unit,
    onSave: (Medication) -> Unit
) {
    var name by remember { mutableStateOf(medication.name) }
    var dosage by remember { mutableStateOf(medication.dosage) }
    var selectedFrequency by remember { mutableStateOf(medication.frequency) }
    var selectedTimes by remember { mutableStateOf(medication.getTimesList()) }
    var instructions by remember { mutableStateOf(medication.instructions) }
    var pillsRemaining by remember { mutableStateOf(if (medication.pillsRemaining > 0) medication.pillsRemaining.toString() else "") }
    var pillsPerDose by remember { mutableStateOf(medication.pillsPerDose.toString()) }
    var isActive by remember { mutableStateOf(medication.isActive) }
    var showTimePicker by remember { mutableStateOf(false) }
    var editingTimeIndex by remember { mutableStateOf(-1) }
    
    val frequencies = listOf("Daily", "Twice Daily", "Three Times Daily", "Weekly", "As Needed")
    
    val isValid = name.isNotBlank() && dosage.isNotBlank() && selectedTimes.isNotEmpty()
    val hasChanges = name != medication.name ||
            dosage != medication.dosage ||
            selectedFrequency != medication.frequency ||
            selectedTimes != medication.getTimesList() ||
            instructions != medication.instructions ||
            (pillsRemaining.toIntOrNull() ?: 0) != medication.pillsRemaining ||
            (pillsPerDose.toIntOrNull() ?: 1) != medication.pillsPerDose ||
            isActive != medication.isActive
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Medication",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (isValid) {
                                val updated = medication.copy(
                                    name = name,
                                    dosage = dosage,
                                    frequency = selectedFrequency,
                                    times = selectedTimes.joinToString(","),
                                    instructions = instructions,
                                    pillsRemaining = pillsRemaining.toIntOrNull() ?: 0,
                                    pillsPerDose = pillsPerDose.toIntOrNull() ?: 1,
                                    isActive = isActive
                                )
                                onSave(updated)
                            }
                        },
                        enabled = isValid && hasChanges
                    ) {
                        Text(
                            "Update",
                            color = if (isValid && hasChanges) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            
            // Active/Inactive Toggle
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) StatusTaken.copy(alpha = 0.1f)
                        else StatusMissed.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isActive) Icons.Outlined.CheckCircle else Icons.Outlined.PauseCircle,
                                contentDescription = null,
                                tint = if (isActive) StatusTaken else StatusMissed
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isActive) "Active" else "Paused",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isActive) StatusTaken else StatusMissed
                                )
                                Text(
                                    text = if (isActive) "Reminders are active"
                                    else "Reminders are paused",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isActive,
                            onCheckedChange = { isActive = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = StatusTaken,
                                checkedThumbColor = Color.White
                            )
                        )
                    }
                }
            }
            
            // Medication Name
            item {
                Column {
                    Text(
                        text = "Medication Name *",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Paracetamol") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Medication,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )
                }
            }
            
            // Dosage
            item {
                Column {
                    Text(
                        text = "Dosage *",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dosage,
                        onValueChange = { dosage = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., 500mg, 1 tablet, 5ml") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Scale,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )
                }
            }
            
            // Frequency
            item {
                Column {
                    Text(
                        text = "Frequency",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(frequencies) { frequency ->
                            FrequencyChip(
                                text = frequency,
                                selected = frequency == selectedFrequency,
                                onClick = {
                                    selectedFrequency = frequency
                                    selectedTimes = when (frequency) {
                                        "Twice Daily" -> listOf("08:00", "20:00")
                                        "Three Times Daily" -> listOf("08:00", "14:00", "20:00")
                                        else -> listOf("08:00")
                                    }
                                }
                            )
                        }
                    }
                }
            }
            
            // Reminder Times
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reminder Times",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                        TextButton(
                            onClick = {
                                editingTimeIndex = selectedTimes.size
                                showTimePicker = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Time")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        selectedTimes.forEachIndexed { index, time ->
                            TimeChip(
                                time = time,
                                onEdit = {
                                    editingTimeIndex = index
                                    showTimePicker = true
                                },
                                onDelete = {
                                    if (selectedTimes.size > 1) {
                                        selectedTimes = selectedTimes.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
            
            // Instructions
            item {
                Column {
                    Text(
                        text = "Instructions (Optional)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g., Take with food, Before meals") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Notes,
                                contentDescription = null,
                                tint = PrimaryGreen
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )
                }
            }
            
            // Inventory Tracking
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Inventory2,
                                contentDescription = null,
                                tint = SecondaryTeal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Inventory Tracking",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = pillsRemaining,
                                onValueChange = { pillsRemaining = it.filter { c -> c.isDigit() } },
                                modifier = Modifier.weight(1f),
                                label = { Text("Pills Remaining") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryTeal,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = pillsPerDose,
                                onValueChange = { pillsPerDose = it.filter { c -> c.isDigit() } },
                                modifier = Modifier.weight(1f),
                                label = { Text("Per Dose") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryTeal,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
    
    // Time Picker Dialog
    if (showTimePicker) {
        TimePickerDialog(
            initialTime = if (editingTimeIndex < selectedTimes.size) selectedTimes[editingTimeIndex] else "08:00",
            onDismiss = { showTimePicker = false },
            onConfirm = { hour, minute ->
                val newTime = String.format("%02d:%02d", hour, minute)
                selectedTimes = if (editingTimeIndex < selectedTimes.size) {
                    selectedTimes.toMutableList().apply {
                        this[editingTimeIndex] = newTime
                    }
                } else {
                    selectedTimes + newTime
                }
                showTimePicker = false
            }
        )
    }
}
