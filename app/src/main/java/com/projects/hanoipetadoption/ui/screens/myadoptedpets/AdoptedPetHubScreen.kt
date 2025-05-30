package com.projects.hanoipetadoption.ui.screens.myadoptedpets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.projects.hanoipetadoption.R
import com.projects.hanoipetadoption.ui.screens.postadoption.CareInstructionsScreen
import com.projects.hanoipetadoption.ui.screens.postadoption.HealthTrackerScreen
import com.projects.hanoipetadoption.ui.screens.postadoption.PetStatusScreen
import com.projects.hanoipetadoption.ui.screens.postadoption.ReminderScreen
import com.projects.hanoipetadoption.ui.screens.postadoption.dialogs.AddHealthRecordDialog
import com.projects.hanoipetadoption.ui.screens.postadoption.dialogs.AddReminderDialog
import com.projects.hanoipetadoption.ui.screens.postadoption.dialogs.AddStatusUpdateDialog
import com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets.AdoptedPetHubViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Tab items for the Adopted Pet Hub
 */
enum class AdoptedPetHubTab(val title: String) {
    STATUS("Cập nhật"),
    HEALTH("Sức khỏe"),
    REMINDERS("Nhắc nhở")
//    CARE("Hướng dẫn")
}

/**
 * Hub screen for displaying all post-adoption features related to a specific pet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptedPetHubScreen(
    navController: NavController,
    petId: String,
    viewModel: AdoptedPetHubViewModel = koinViewModel()
) {
    var selectedTab by remember { mutableStateOf(AdoptedPetHubTab.STATUS) }
    var petName by remember { mutableStateOf("Thú cưng") }

    var showAddStatusDialog by remember { mutableStateOf(false) }
    var showAddReminderDialog by remember { mutableStateOf(false) }
    var showAddHealthRecordDialog by remember { mutableStateOf(false) }

    // Fetch pet details when screen is created
    LaunchedEffect(petId) {
        viewModel.loadPetDetails(petId)
    }

    val pet = viewModel.petDetails.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pet.value?.name ?: "",
                        maxLines = 1,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab row for switching between post-adoption features
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                AdoptedPetHubTab.values().forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab.ordinal == index,
                        onClick = { selectedTab = tab },
                        text = { Text(text = tab.title) },
                        icon = {
                            when (tab) {
                                AdoptedPetHubTab.STATUS -> Icon(
                                    imageVector = if (selectedTab == tab) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                                    contentDescription = null
                                )
                                AdoptedPetHubTab.HEALTH -> Icon(
                                    imageVector = Icons.Default.HealthAndSafety,
                                    contentDescription = null
                                )
                                AdoptedPetHubTab.REMINDERS -> Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null
                                )
//                                AdoptedPetHubTab.CARE -> Icon(
//                                    imageVector = Icons.Default.Description,
//                                    contentDescription = null
//                                )
                            }
                        }
                    )
                }
            }

            // Content for the selected tab
            when (selectedTab) {
                AdoptedPetHubTab.STATUS -> PetStatusScreen(
                    petId = petId,
                    onAddStatusClick = {
                        showAddStatusDialog = true
                    }
                )
                AdoptedPetHubTab.HEALTH -> HealthTrackerScreen(
                    petId = petId,
                    onAddRecordClick = { showAddHealthRecordDialog = true }
                )
                AdoptedPetHubTab.REMINDERS -> ReminderScreen(
                    petId = petId,
                    onAddReminderClick = { showAddReminderDialog = true }
                )
//                AdoptedPetHubTab.CARE -> CareInstructionsScreen(petId = petId)
            }
        }

        // --- Dialogs ---
        if (showAddStatusDialog) {
            AddStatusUpdateDialog(
                petId = petId,
                onDismiss = { showAddStatusDialog = false },
                onAddStatus = { statusUpdateData, imageFiles ->
                    viewModel.addPetStatusUpdate(petId, statusUpdateData, imageFiles)
                    showAddStatusDialog = false
                }
            )
        }

        if (showAddReminderDialog) {
            AddReminderDialog(
                petId = petId,
                onDismiss = { showAddReminderDialog = false },
                onAddReminder = { reminderData ->
                    viewModel.addReminder(reminderData)
                    showAddReminderDialog = false
                }
            )
        }

        if (showAddHealthRecordDialog) {
            AddHealthRecordDialog(
                petId = petId,
                onDismiss = { showAddHealthRecordDialog = false },
                onAddRecord = { healthRecordData ->
                    viewModel.addHealthRecord(healthRecordData)
                    showAddHealthRecordDialog = false
                }
            )
        }
    }
} 