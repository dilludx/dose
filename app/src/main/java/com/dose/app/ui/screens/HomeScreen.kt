package com.dose.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dose.app.data.DoseHistory
import com.dose.app.data.Medication
import com.dose.app.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    medications: List<Medication>,
    todayHistory: List<DoseHistory>,
    todayStats: Pair<Int, Int>,
    onAddClick: () -> Unit,
    onMedicationClick: (Medication) -> Unit,
    onMarkTaken: (Long, Long) -> Unit,
    onMarkSkipped: (Long) -> Unit
) {
    val dateFormat = SimpleDateFormat("d MMMM yyyy, EEEE", Locale.getDefault())
    val today = dateFormat.format(Date())

    Scaffold(
        floatingActionButton = {
            if (medications.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = PrimaryGreen,
                    contentColor = Color.White,
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Medication",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(modifier = Modifier.padding(vertical = 12.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (userName.isNotBlank()) {
                            Text(
                                text = "Hi, ",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("👋", style = MaterialTheme.typography.headlineMedium)
                        } else {
                            Text(
                                text = "Hi 👋",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = today,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DOSE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextSecondary.copy(alpha = 0.4f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Designed by Pharmacist",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextSecondary.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Stats Card — Brand Gradient
            item {
                StatsCard(
                    taken = todayStats.first,
                    total = todayStats.second
                )
            }

            // Today's Doses
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Schedule",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (todayHistory.isNotEmpty()) {
                        Surface(
                            color = PrimaryGreenSoft,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${todayHistory.size} doses",
                                style = MaterialTheme.typography.labelMedium,
                                color = PrimaryGreen,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            if (todayHistory.isNotEmpty()) {
                items(todayHistory) { dose ->
                    DoseCard(
                        dose = dose,
                        onMarkTaken = { onMarkTaken(dose.id, dose.medicationId) },
                        onMarkSkipped = { onMarkSkipped(dose.id) }
                    )
                }
            } else if (medications.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PrimaryGreenSoft
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(PrimaryGreen.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "All done for today! 🎉",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Great job keeping up with your health.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    EmptyStateCard(onAddClick = onAddClick)
                }
            }

            // My Medications Section
            if (medications.isNotEmpty()) {
                item {
                    Text(
                        text = "My Medications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                items(medications) { medication ->
                    MedicationCard(
                        medication = medication,
                        onClick = { onMedicationClick(medication) }
                    )
                }
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun StatsCard(taken: Int, total: Int) {
    val progress = if (total > 0) taken.toFloat() / total else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreenSoft)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today's Progress",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$taken",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = " / $total doses",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                        )
                    }
                }

                // Circular Progress
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(76.dp),
                        strokeWidth = 8.dp,
                        color = PrimaryGreen,
                        trackColor = PrimaryGreen.copy(alpha = 0.15f)
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun DoseCard(
    dose: DoseHistory,
    onMarkTaken: () -> Unit,
    onMarkSkipped: () -> Unit
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val scheduledTime = timeFormat.format(Date(dose.scheduledTime))

    val statusColor by animateColorAsState(
        targetValue = when (dose.status) {
            "taken" -> StatusTaken
            "skipped" -> StatusSkipped
            else -> StatusPending
        }, label = "statusColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (dose.status == "pending") 6.dp else 1.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (dose.status == "pending") PrimaryGreen.copy(alpha = 0.3f) else Color.Transparent
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(statusColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (dose.status) {
                        "taken" -> Icons.Default.Check
                        "skipped" -> Icons.Default.Close
                        else -> Icons.Outlined.Schedule
                    },
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dose.medicationName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = scheduledTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action buttons
            if (dose.status == "pending") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onMarkSkipped,
                        modifier = Modifier
                            .size(42.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Skip",
                            tint = StatusSkipped,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    FilledIconButton(
                        onClick = onMarkTaken,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Take",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationCard(
    medication: Medication,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pill Icon — Green accent
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreenSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Medication,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${medication.dosage} • ${medication.frequency}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (medication.pillsRemaining > 0 && medication.pillsRemaining <= medication.refillReminder) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = StatusMissed.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(12.dp), tint = StatusMissed)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Refill needed (${medication.pillsRemaining} left)",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusMissed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun EmptyStateCard(onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreenSoft
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Medication,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = PrimaryGreen
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "No medications yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add your first medication to get started with reminders",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Medication", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenEmptyPreview() {
    DoseTheme {
        HomeScreen(
            userName = "Dinesh",
            medications = emptyList(),
            todayHistory = emptyList(),
            todayStats = Pair(0, 0),
            onAddClick = { },
            onMedicationClick = { },
            onMarkTaken = { _, _ -> },
            onMarkSkipped = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenWithDataPreview() {
    val sampleMeds = listOf(
        Medication(
            id = 1, name = "Paracetamol", dosage = "500mg",
            frequency = "Twice Daily", times = "08:00,20:00",
            instructions = "Take with food", pillsRemaining = 28, pillsPerDose = 1
        ),
        Medication(
            id = 2, name = "Vitamin D3", dosage = "1000 IU",
            frequency = "Daily", times = "09:00",
            instructions = "After breakfast", pillsRemaining = 5, pillsPerDose = 1,
            refillReminder = 10
        )
    )
    val sampleHistory = listOf(
        DoseHistory(
            id = 1, medicationId = 1, medicationName = "Paracetamol",
            scheduledTime = System.currentTimeMillis(), date = "2026-02-20",
            status = "taken"
        ),
        DoseHistory(
            id = 2, medicationId = 2, medicationName = "Vitamin D3",
            scheduledTime = System.currentTimeMillis() + 3600000, date = "2026-02-20",
            status = "pending"
        )
    )
    DoseTheme {
        HomeScreen(
            userName = "Dinesh",
            medications = sampleMeds,
            todayHistory = sampleHistory,
            todayStats = Pair(1, 2),
            onAddClick = { },
            onMedicationClick = { },
            onMarkTaken = { _, _ -> },
            onMarkSkipped = {}
        )
    }
}
