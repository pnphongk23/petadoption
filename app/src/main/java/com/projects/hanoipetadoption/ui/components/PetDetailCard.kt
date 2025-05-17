package com.projects.hanoipetadoption.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.projects.hanoipetadoption.ui.model.Pet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PetDetailCard(
    pet: Pet,
    modifier: Modifier = Modifier,
    isAdopted: Boolean = false, // Added isAdopted parameter
    onClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(pet.isFavorite) }
    
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column {
            // Image with favorite button overlay
            GlideImage(
                model = pet.imageRes,
                contentDescription = pet.name,
                contentScale = ContentScale.Crop,
                requestBuilderTransform = {
                    it.diskCacheStrategy(DiskCacheStrategy.DATA)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            
            // Display Adopted Badge if the pet is adopted
            if (isAdopted) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(4.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer, // Or your desired color
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Text("Đã nhận nuôi", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    overflow = TextOverflow.Ellipsis)

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            
            Text(
                text = pet.breed,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${pet.gender.displayName} • ${pet.age}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // First characteristic tag
            if (pet.characteristics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                CharacteristicTag(
                    text = pet.characteristics.first(),
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}

@Composable
fun CharacteristicTag(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}