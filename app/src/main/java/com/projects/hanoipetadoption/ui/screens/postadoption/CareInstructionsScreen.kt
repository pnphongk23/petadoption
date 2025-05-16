package com.projects.hanoipetadoption.ui.screens.postadoption

import android.content.Context
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.projects.hanoipetadoption.data.model.postadoption.CareInstructions
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.CareInstructionsState
import com.projects.hanoipetadoption.ui.viewmodel.postadoption.CareInstructionsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Care Instructions Screen for displaying pet care documents and guides
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareInstructionsScreen(
    petId: Int,
    viewModel: CareInstructionsViewModel = koinViewModel()
) {
    val careInstructionsState by viewModel.careInstructionsState.asFlow().collectAsState(initial = CareInstructionsState.Loading)
    val context = LocalContext.current

    LaunchedEffect(petId) {
        viewModel.loadCareInstructions(petId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Care Instructions") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = careInstructionsState) {
                is CareInstructionsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is CareInstructionsState.Success -> {
                    if (state.instructions.isEmpty()) {
                        EmptyInstructionsMessage()
                    } else {
                        InstructionsList(
                            instructions = state.instructions,
                            context = context,
                            onOpenDocument = { instruction ->
                                viewModel.openDocument(context, instruction) { success ->
                                    // Handle open document result
                                }
                            }
                        )
                    }
                }
                is CareInstructionsState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadCareInstructions(petId) }
                    )
                }
            }
        }
    }
}

/**
 * List of care instructions
 */
@Composable
fun InstructionsList(
    instructions: List<CareInstructions>,
    context: Context,
    onOpenDocument: (CareInstructions) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(instructions) { instruction ->
            InstructionItem(
                instruction = instruction,
                onOpenDocument = onOpenDocument
            )
        }
    }
}

/**
 * Item representing a single care instruction document
 */
@Composable
fun InstructionItem(
    instruction: CareInstructions,
    onOpenDocument: (CareInstructions) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
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
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = instruction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                instruction.description?.let { description ->
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
              IconButton(
                onClick = { onOpenDocument(instruction) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.InsertDriveFile,
                    contentDescription = "Open Document",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Message displayed when no instructions are available
 */
@Composable
fun EmptyInstructionsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No care instructions available for this pet",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
