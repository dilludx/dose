package com.dose.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import com.dose.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onNavigateBack: () -> Unit,
    onSave: (
        name: String,
        dosage: String,
        frequency: String,
        times: List<String>,
        instructions: String,
        pillsRemaining: Int,
        pillsPerDose: Int
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var selectedFrequency by remember { mutableStateOf("Daily") }
    var selectedTimes by remember { mutableStateOf(listOf("08:00")) }
    var instructions by remember { mutableStateOf("") }
    var pillsRemaining by remember { mutableStateOf("") }
    var pillsPerDose by remember { mutableStateOf("1") }
    var showTimePicker by remember { mutableStateOf(false) }
    var editingTimeIndex by remember { mutableStateOf(-1) }
    
    val frequencies = listOf("Daily", "Twice Daily", "Three Times Daily", "Weekly", "As Needed")
    
    val isValid = name.isNotBlank() && dosage.isNotBlank() && selectedTimes.isNotEmpty()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Medication",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (isValid) {
                                onSave(
                                    name,
                                    dosage,
                                    selectedFrequency,
                                    selectedTimes,
                                    instructions,
                                    pillsRemaining.toIntOrNull() ?: 0,
                                    pillsPerDose.toIntOrNull() ?: 1
                                )
                            }
                        },
                        enabled = isValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
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
                                    // Adjust times based on frequency
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
                                tint = SecondaryPurple
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
                                    focusedBorderColor = SecondaryPurple,
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
                                    focusedBorderColor = SecondaryPurple,
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

@Composable
fun FrequencyChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = if (selected) PrimaryGreen else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        border = if (!selected) {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        } else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TimeChip(
    time: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val parts = time.split(":")
    val hour = parts[0].toIntOrNull() ?: 0
    val minute = parts[1].toIntOrNull() ?: 0
    val amPm = if (hour < 12) "AM" else "PM"
    val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val displayTime = String.format("%d:%02d %s", displayHour, minute, amPm)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreenLight.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = displayTime,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Remove",
                    tint = StatusMissed
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTime: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val parts = initialTime.split(":")
    val initialHour = parts[0].toIntOrNull() ?: 8
    val initialMinute = parts[1].toIntOrNull() ?: 0
    
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }
            ) {
                Text("OK", color = PrimaryGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Select Time")
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    selectorColor = PrimaryGreen,
                    timeSelectorSelectedContainerColor = PrimaryGreen,
                    timeSelectorSelectedContentColor = Color.White
                )
            )
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddMedicationScreenPreview() {
    DoseTheme {
        AddMedicationScreen(
            onNavigateBack = {},
            onSave = { _, _, _, _, _, _, _ -> }
        )
    }
}
