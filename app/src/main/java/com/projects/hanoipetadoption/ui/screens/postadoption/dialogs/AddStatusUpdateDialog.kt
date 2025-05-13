package com.projects.hanoipetadoption.ui.screens.postadoption.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.projects.hanoipetadoption.data.model.postadoption.PetStatusUpdate
import java.io.File
import java.util.Date

/**
 * Dialog for adding a new pet status update
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStatusUpdateDialog(
    petId: Int,
    onDismiss: () -> Unit,
    onAddStatus: (PetStatusUpdate, List<File>) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var milestone by remember { mutableStateOf("") }
    var isMilestone by remember { mutableStateOf(false) }
    val selectedImages = remember { mutableStateListOf<Uri>() }
    val context = LocalContext.current
    
    // Photo picker launcher
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages.addAll(uris.take(5 - selectedImages.size))
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Status Update",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Status update content
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("How is your pet doing?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Milestone checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isMilestone,
                        onCheckedChange = { isMilestone = it }
                    )
                    
                    Text(
                        text = "This is a special milestone",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // Milestone field (only visible when checkbox is checked)
                if (isMilestone) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = milestone,
                        onValueChange = { milestone = it },
                        label = { Text("Milestone (e.g. First Bath, One Year Adoption Anniversary)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Image picker section
                Text(
                    text = "Add Photos",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Selected images preview
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages) { uri ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(80.dp)
                            )
                            
                            IconButton(
                                onClick = { selectedImages.remove(uri) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Image",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    
                    // Add photo button (only if less than 5 photos are selected)
                    if (selectedImages.size < 5) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { photoPicker.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Image",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            // Create status update
                            val statusUpdate = PetStatusUpdate(
                                id = null,
                                petId = petId,
                                description = content,
                                createdAt = Date(),
                                imageUrls = null, // Will be set after image upload
                                milestone = if (isMilestone && milestone.isNotEmpty()) milestone else null
                            )
                            
                            // Convert URIs to File objects for upload
                            val imageFiles = selectedImages.mapNotNull { uri ->
                                // In a real app, you would convert URI to a File here
                                // For this example, we'll just return null
                                null
                            }
                            
                            onAddStatus(statusUpdate, imageFiles)
                            onDismiss()
                        },
                        enabled = content.isNotEmpty()
                    ) {
                        Text("POST")
                    }
                }
            }
        }
    }
}
