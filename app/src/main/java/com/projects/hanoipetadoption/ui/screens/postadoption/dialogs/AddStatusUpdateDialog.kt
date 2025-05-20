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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
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
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import android.os.Environment
import androidx.core.content.FileProvider
import android.content.Context

private fun createImageFile(context: Context): Pair<File, Uri>? {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        
        if (storageDir == null) {
            // Handle error: External storage might not be available
            return null
        }

        val imageFile = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg",        /* suffix */
            storageDir     /* directory */
        )

        val photoURI: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider", // Ensure this matches your FileProvider authority
            imageFile
        )
        Pair(imageFile, photoURI)
    } catch (ex: IOException) {
        ex.printStackTrace()
        // Handle error: Could not create file
        null
    }
}

/**
 * Dialog for adding a new pet status update
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStatusUpdateDialog(
    petId: String,
    onDismiss: () -> Unit,
    onAddStatus: (PetStatusUpdate, List<File>) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var milestone by remember { mutableStateOf("") }
    var isMilestone by remember { mutableStateOf(false) }
    val selectedImages = remember { mutableStateListOf<Uri>() }
    val context = LocalContext.current
    var tempCameraImageUri by remember { mutableStateOf<Uri?>(null) } // To store URI for camera

    // Launcher for taking a picture
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraImageUri?.let { uri ->
                if (selectedImages.size < 5) {
                    selectedImages.add(uri)
                }
                // tempCameraImageUri = null // Optional: reset if you want to ensure it's fresh each time
            }
        }
    }
    
    // Photo picker launcher for gallery
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val remainingSlots = 5 - selectedImages.size
            if (remainingSlots > 0) {
                selectedImages.addAll(uris.take(remainingSlots))
            }
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
                    text = "Thêm cập nhật tình trạng mới",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Status update content
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Bé nhà bạn dạo này thế nào rồi?") },
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
                        text = "Đây là một cột mốc đặc biệt",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // Milestone field (only visible when checkbox is checked)
                if (isMilestone) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = milestone,
                        onValueChange = { milestone = it },
                        label = { Text("Cột mốc (VD: Tắm lần đầu, Kỷ niệm 1 năm nhận nuôi)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Image picker section
                Text(
                    text = "Thêm ảnh (tối đa 5 ảnh)",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                // Buttons to add images from Gallery or Camera
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Button to pick from gallery
                    Button(
                        onClick = {
                            if (selectedImages.size < 5) {
                                photoPicker.launch("image/*")
                            }
                        },
                        enabled = selectedImages.size < 5,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = "Chọn từ thư viện")
                        Spacer(Modifier.width(4.dp))
                        Text("Thư viện")
                    }

                    // Button to take a picture with camera
                    Button(
                        onClick = {
                            if (selectedImages.size < 5) {
                                val imageFileAndUri = createImageFile(context)
                                if (imageFileAndUri != null) {
                                    tempCameraImageUri = imageFileAndUri.second
                                    takePictureLauncher.launch(imageFileAndUri.second)
                                } else {
                                    // Handle error: Toast or log, "Could not prepare image file."
                                }
                            }
                        },
                        enabled = selectedImages.size < 5,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = "Chụp ảnh mới")
                        Spacer(Modifier.width(4.dp))
                        Text("Máy ảnh")
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Selected images preview
                if (selectedImages.isNotEmpty()){
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
                                        .size(24.dp) // Adjusted size for better touch target
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Xoá ảnh này",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer // Ensure good contrast
                                    )
                                }
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
                        Text("HUỶ")
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
                                imageUrls = null, // Will be set by the ViewModel after files are processed
                                milestone = if (isMilestone && milestone.isNotEmpty()) milestone else null
                            )
                            
                            // Convert URIs to File objects
                            val imageFiles = selectedImages.mapNotNull { uri ->
                                try {
                                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                        // Create a temporary file in the app's cache directory
                                        val extension = context.contentResolver.getType(uri)?.substringAfterLast('/') ?: "tmp"
                                        val tempFile = File(context.cacheDir, "upload_${UUID.randomUUID()}.$extension")
                                        FileOutputStream(tempFile).use { outputStream ->
                                            inputStream.copyTo(outputStream)
                                        }
                                        tempFile // Return the File object
                                    }
                                } catch (e: Exception) {
                                    // Log error or notify user
                                    e.printStackTrace() 
                                    null // Return null if conversion fails for this URI
                                }
                            }
                            
                            onAddStatus(statusUpdate, imageFiles)
                            onDismiss()
                        },
                        enabled = content.isNotEmpty()
                    ) {
                        Text("ĐĂNG")
                    }
                }
            }
        }
    }
}
