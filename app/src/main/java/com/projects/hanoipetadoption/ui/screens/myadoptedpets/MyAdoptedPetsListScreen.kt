package com.projects.hanoipetadoption.ui.screens.myadoptedpets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.projects.hanoipetadoption.R
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets.AdoptedPetsState
import com.projects.hanoipetadoption.ui.viewmodel.myadoptedpets.MyAdoptedPetsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Screen displaying a list of pets adopted by the current user
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdoptedPetsListScreen(
    navController: NavController,
    viewModel: MyAdoptedPetsViewModel = koinViewModel()
) {
    val adoptedPetsState by viewModel.adoptedPetsState.collectAsState()
    
    LaunchedEffect(key1 = Unit) {
        viewModel.loadAdoptedPets()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.my_adopted_pets),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = adoptedPetsState) {
                is AdoptedPetsState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AdoptedPetsState.Success -> {
                    if (state.pets.isEmpty()) {
                        EmptyAdoptedPetsMessage()
                    } else {
                        AdoptedPetsList(
                            pets = state.pets,
                            onPetClick = { pet ->
                                navController.navigate("adopted_pet_hub/${pet.id}")
                            }
                        )
                    }
                }
                is AdoptedPetsState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadAdoptedPets() }
                    )
                }
            }
        }
    }
}

/**
 * List of adopted pets
 */
@Composable
fun AdoptedPetsList(
    pets: List<Pet>,
    onPetClick: (Pet) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        content = {
            items(pets) { pet ->
                AdoptedPetItem(
                    pet = pet,
                    onClick = { onPetClick(pet) }
                )
            }
        }
    )
}

/**
 * Individual pet item in the list
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AdoptedPetItem(
    pet: Pet,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = pet.imageRes,
                contentDescription = stringResource(R.string.pet_adoption_slogan, pet.name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {

                it.apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .transition(DrawableTransitionOptions.withCrossFade())
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pet.breed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

/**
 * Message displayed when no adopted pets are available
 */
@Composable
fun EmptyAdoptedPetsMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = null,
            modifier = Modifier.padding(bottom = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Bạn chưa nhận nuôi thú cưng nào",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Hãy xem danh sách và nhận nuôi ngay hôm nay!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * Error message
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Đã xảy ra lỗi: $message",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        androidx.compose.material3.Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Thử lại")
        }
    }
} 