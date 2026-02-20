package com.dose.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.dose.app.R
import com.dose.app.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Soft pastel gradient — light mint to light lavender
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8FBF3),  // Very soft mint
                        Color(0xFFF3EAFC),  // Very soft lavender
                        Color(0xFFEDE5F7)   // Slightly deeper lavender at bottom
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            // App Icon with pulse animation
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Dose App Icon",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(28.dp))
            )

            Spacer(modifier = Modifier.height(28.dp))

            // App Name — dark text on pastel bg
            Text(
                text = "DOSE",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Medication Management\nMade Simple",
                style = MaterialTheme.typography.titleLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Feature list
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FeatureItem(
                    icon = Icons.Outlined.Notifications,
                    text = "Smart reminders at the right time",
                    iconTint = PrimaryGreen
                )
                FeatureItem(
                    icon = Icons.Outlined.CheckCircle,
                    text = "Track your daily doses",
                    iconTint = PrimaryGreen
                )
                FeatureItem(
                    icon = Icons.Outlined.Inventory2,
                    text = "Refill alerts when running low",
                    iconTint = SecondaryPurple
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Get Started Button — brand gradient
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pharmacist badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Verified,
                    contentDescription = null,
                    tint = SecondaryPurple.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Designed by a Pharmacist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    text: String,
    iconTint: Color = PrimaryGreen
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    DoseTheme {
        WelcomeScreen(onGetStarted = {})
    }
}
