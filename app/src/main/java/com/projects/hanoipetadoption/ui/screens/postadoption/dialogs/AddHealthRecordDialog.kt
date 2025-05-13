package com.projects.hanoipetadoption.ui.screens.postadoption.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.projects.hanoipetadoption.data.model.postadoption.HealthRecordCreate
import com.projects.hanoipetadoption.data.model.postadoption.RecordType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dialog for adding a new health record
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthRecordDialog(
    petId: Int,
    onDismiss: () -> Unit,
    onAddRecord: (HealthRecordCreate) -> Unit
) {
    var notes by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var selectedRecordType by remember { mutableStateOf<RecordType>(RecordType.GENERAL) }
    var expanded by remember { mutableStateOf(false) }
    
    // For date selection
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val selectedDate = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

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
                    text = "Add Health Record",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Record type dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedRecordType.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Record Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        RecordType.values().forEach { recordType ->
                            DropdownMenuItem(
                                text = { Text(recordType.toString()) },
                                onClick = {
                                    selectedRecordType = recordType
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Date selection
                OutlinedTextField(
                    value = dateFormatter.format(selectedDate),
                    onValueChange = {},
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = { 
                        TextButton(onClick = { showDatePicker = true }) {
                            Text("CHANGE")
                        }
                    }
                )
                
                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("CANCEL")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Weight field (only visible for weight record type)
                if (selectedRecordType == RecordType.WEIGHT) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                // Notes field
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                
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
                            // Create health record
                            val recordDate = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
                            val weightValue = weight.toFloatOrNull()
                              val healthRecord = HealthRecordCreate(
                                petId = petId,
                                recordType = selectedRecordType,
                                notes = notes.ifEmpty { null },
                                weight = weightValue?.toDouble()
                            )
                            
                            onAddRecord(healthRecord)
                            onDismiss()
                        },
                        enabled = when (selectedRecordType) {
                            RecordType.WEIGHT -> weight.isNotEmpty() && weight.toFloatOrNull() != null
                            else -> true
                        }
                    ) {
                        Text("SAVE")
                    }
                }
            }
        }
    }
}
