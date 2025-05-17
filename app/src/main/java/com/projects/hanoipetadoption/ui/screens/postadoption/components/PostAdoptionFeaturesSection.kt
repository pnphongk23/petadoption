package com.projects.hanoipetadoption.ui.screens.postadoption.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Post-adoption features section to be added to the Pet Detail screen
 */
@Composable
fun PostAdoptionFeaturesSection(
    petId: String,
    onHealthTrackerClick: (String) -> Unit,
    onRemindersClick: (String) -> Unit,
    onCareInstructionsClick: (String) -> Unit,
    onPetStatusClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Post-Adoption Tools",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                PostAdoptionFeatureItem(
                    icon = Icons.Default.HealthAndSafety,
                    title = "Health Tracker",
                    description = "Track vaccinations, treatments, and weight",
                    onClick = { onHealthTrackerClick(petId) }
                )
                
                Divider()
                
                PostAdoptionFeatureItem(
                    icon = Icons.Default.CalendarToday,
                    title = "Reminders",
                    description = "Set reminders for vaccinations and care",
                    onClick = { onRemindersClick(petId) }
                )
                
                Divider()
                
                PostAdoptionFeatureItem(
                    icon = Icons.Default.MenuBook,
                    title = "Care Instructions",
                    description = "Access detailed care guides and documents",
                    onClick = { onCareInstructionsClick(petId) }
                )
                
                Divider()
                
                PostAdoptionFeatureItem(
                    icon = Icons.Default.Favorite,
                    title = "Pet Updates",
                    description = "Share your pet's progress and milestones",
                    onClick = { onPetStatusClick(petId) }
                )
            }
        }
    }
}

/**
 * Individual feature item in the post-adoption section
 */
@Composable
private fun PostAdoptionFeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
