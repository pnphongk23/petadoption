package com.projects.hanoipetadoption.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.projects.hanoipetadoption.R
import com.projects.hanoipetadoption.presentation.components.PetDetailCard
import com.projects.hanoipetadoption.ui.model.Pet
import com.projects.hanoipetadoption.ui.model.PetCategory
import com.projects.hanoipetadoption.ui.model.PetGender
import com.projects.hanoipetadoption.ui.model.SamplePetsData

enum class AgeFilter(val displayName: String, val predicate: (String) -> Boolean) {
    UNDER_ONE_YEAR("Dưới 1 năm", { age -> age.contains("month", ignoreCase = true) || age.startsWith("1 month", ignoreCase = true) }),
    ONE_TO_THREE_YEARS("1-3 năm", { age -> 
        val numericValue = age.split(" ")[0].toIntOrNull() ?: 0
        val isYear = age.contains("year", ignoreCase = true)
        isYear && numericValue in 1..3
    }),
    OVER_THREE_YEARS("Trên 3 năm", { age -> 
        val numericValue = age.split(" ")[0].toIntOrNull() ?: 0
        val isYear = age.contains("year", ignoreCase = true)
        isYear && numericValue > 3
    })
}

enum class SizeFilter(val displayName: String) {
    SMALL("Nhỏ"),
    MEDIUM("Trung bình"),
    LARGE("Lớn")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PetsScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val allPets = remember { SamplePetsData.getPets() }
    var filteredPets by remember { mutableStateOf(allPets) }
    
    // Filter states
    val selectedCategories = remember { mutableStateListOf<PetCategory>() }
    val selectedBreeds = remember { mutableStateListOf<String>() }
    val selectedAgeFilters = remember { mutableStateListOf<AgeFilter>() }
    val selectedGenders = remember { mutableStateListOf<PetGender>() }
    val selectedSizes = remember { mutableStateListOf<SizeFilter>() }
    
    var showFilters by remember { mutableStateOf(false) }
    
    // Get unique breeds from all pets
    val allBreeds = remember {
        allPets.map { it.breed }.distinct().sorted()
    }
    
    // Filter function
    fun applyFilters() {
        filteredPets = allPets
        
        // Apply category filter
        if (selectedCategories.isNotEmpty()) {
            filteredPets = filteredPets.filter { pet -> selectedCategories.contains(pet.category) }
        }
        
        // Apply breed filter
        if (selectedBreeds.isNotEmpty()) {
            filteredPets = filteredPets.filter { pet -> selectedBreeds.contains(pet.breed) }
        }
        
        // Apply age filter
        if (selectedAgeFilters.isNotEmpty()) {
            filteredPets = filteredPets.filter { pet -> 
                selectedAgeFilters.any { ageFilter -> ageFilter.predicate(pet.age) }
            }
        }
        
        // Apply gender filter
        if (selectedGenders.isNotEmpty()) {
            filteredPets = filteredPets.filter { pet -> selectedGenders.contains(pet.gender) }
        }
          // Apply search query filter
        if (searchQuery.isNotEmpty()) {
            filteredPets = filteredPets.filter { pet ->
                com.projects.hanoipetadoption.util.StringUtils.containsIgnoreDiacritics(pet.name, searchQuery) ||
                com.projects.hanoipetadoption.util.StringUtils.containsIgnoreDiacritics(pet.breed, searchQuery)
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.pets),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { 
                    searchQuery = it
                    applyFilters()
                },
                onSearch = { 
                    isSearchActive = false
                    applyFilters()
                },
                active = false,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text("Tìm kiếm theo tên hoặc giống") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Search suggestions would go here
            }
            
            // Filter chips
            if (showFilters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Phân loại thú cưng",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PetCategory.entries.forEach { category ->
                            val isSelected = selectedCategories.contains(category)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedCategories.remove(category)
                                    } else {
                                        selectedCategories.add(category)
                                    }
                                    applyFilters()
                                },
                                label = { Text(category.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Giống",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allBreeds.take(5).forEach { breed ->
                            val isSelected = selectedBreeds.contains(breed)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedBreeds.remove(breed)
                                    } else {
                                        selectedBreeds.add(breed)
                                    }
                                    applyFilters()
                                },
                                label = { Text(breed) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Tuổi",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AgeFilter.entries.forEach { ageFilter ->
                            val isSelected = selectedAgeFilters.contains(ageFilter)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedAgeFilters.remove(ageFilter)
                                    } else {
                                        selectedAgeFilters.add(ageFilter)
                                    }
                                    applyFilters()
                                },
                                label = { Text(ageFilter.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Giới tính",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PetGender.entries.forEach { gender ->
                            val isSelected = selectedGenders.contains(gender)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedGenders.remove(gender)
                                    } else {
                                        selectedGenders.add(gender)
                                    }
                                    applyFilters()
                                },
                                label = { Text(gender.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Kích thước",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SizeFilter.entries.forEach { size ->
                            val isSelected = selectedSizes.contains(size)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) {
                                        selectedSizes.remove(size)
                                    } else {
                                        selectedSizes.add(size)
                                    }
                                    applyFilters()
                                },
                                label = { Text(size.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }
            
            // Results count
            Text(
                text = "${filteredPets.size} thú cưng tìm thấy",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                textAlign = TextAlign.Start
            )
            
            // Pets grid
            if (filteredPets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Không tìm thấy thú cưng nào",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hãy thử thay đổi bộ lọc tìm kiếm",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredPets) { pet ->
                        PetDetailCard(
                            pet = pet,
                            onClick = { navController.navigate("pet_detail/${pet.id}") }
                        )
                    }
                }
            }
        }
    }
}