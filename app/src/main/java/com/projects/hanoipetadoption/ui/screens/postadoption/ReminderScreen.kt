package com.projects.hanoipetadoption.ui.screens.postadoption

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.projects.hanoipetadoption.data.model.postadoption.Reminder
import com.projects.hanoipetadoption.data.model.postadoption.ReminderType
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.ReminderState
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.ReminderViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Reminder Screen for displaying and managing pet care reminders
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    petId: String?,
    onAddReminderClick: (String?) -> Unit,
    viewModel: ReminderViewModel = koinViewModel()
) {
    val upcomingRemindersState by viewModel.upcomingRemindersState.collectAsState()
    val petRemindersState by viewModel.remindersState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }

    // If petId is null, we're showing all upcoming reminders
    // If petId is provided, we're showing reminders for a specific pet
    LaunchedEffect(petId) {
        viewModel.loadUpcomingReminders(7) // Show reminders for the next 7 days
        
        if (petId != null) {
            viewModel.loadRemindersForPet(petId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhắc nhở chăm sóc") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddReminderClick(petId) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm nhắc nhở mới")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Only show tabs if we have a specific pet ID
            if (petId != null) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        text = { Text("Tất cả") },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    Tab(
                        text = { Text("Sắp tới") },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    // Show pet-specific reminders if tab 0 is selected and we have a pet ID
                    selectedTabIndex == 0 && petId != null -> {
                        when (val state = petRemindersState) {
                            is ReminderState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            is ReminderState.Success -> {
                                ReminderList(
                                    reminders = state.reminders,
                                    onMarkComplete = { viewModel.markReminderComplete(it) {} }
                                )
                            }
                            is ReminderState.Error -> {
                                ErrorMessage(
                                    message = state.message,
                                    onRetry = { viewModel.loadRemindersForPet(petId) }
                                )
                            }
                        }
                    }
                    // Show upcoming reminders otherwise
                    else -> {
                        when (val state = upcomingRemindersState) {
                            is ReminderState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            is ReminderState.Success -> {
                                ReminderList(
                                    reminders = state.reminders,
                                    onMarkComplete = { viewModel.markReminderComplete(it) {} }
                                )
                            }
                            is ReminderState.Error -> {
                                ErrorMessage(
                                    message = state.message,
                                    onRetry = { viewModel.loadUpcomingReminders(7) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * List of reminders
 */
@Composable
fun ReminderList(
    reminders: List<Reminder>,
    onMarkComplete: (Int) -> Unit
) {
    if (reminders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Không có nhắc nhở nào cả, bạn thêm mới nhé!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reminders) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onMarkComplete = onMarkComplete
                )
            }
        }
    }
}

/**
 * Item representing a single reminder
 */
@Composable
fun ReminderItem(
    reminder: Reminder,
    onMarkComplete: (Int) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reminder.dueDate)
    
    // Calculate days remaining
    val today = Date()
    val daysRemaining = TimeUnit.MILLISECONDS.toDays(
        reminder.dueDate.time - today.time
    ).toInt()
    
    val urgencyColor = when {
        reminder.isCompleted -> MaterialTheme.colorScheme.surfaceVariant
        daysRemaining < 0 -> MaterialTheme.colorScheme.error
        daysRemaining == 0 -> MaterialTheme.colorScheme.error
        daysRemaining <= 2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(urgencyColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = urgencyColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else null
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                reminder.description?.let { description ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            if (!reminder.isCompleted) {
                IconButton(
                    onClick = { reminder.id?.let { onMarkComplete(it) } }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Mark as Complete",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Error message display
 */
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
