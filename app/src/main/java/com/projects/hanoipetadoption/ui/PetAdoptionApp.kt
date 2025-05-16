package com.projects.hanoipetadoption.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.projects.hanoipetadoption.R
import com.projects.hanoipetadoption.ui.screens.AboutScreen
import com.projects.hanoipetadoption.ui.screens.AdoptionApplicationScreen
import com.projects.hanoipetadoption.ui.screens.DonateScreen
import com.projects.hanoipetadoption.ui.screens.HomeScreen
import com.projects.hanoipetadoption.ui.screens.PetDetailScreen
import com.projects.hanoipetadoption.ui.screens.PetsScreen
import com.projects.hanoipetadoption.ui.screens.myadoptedpets.AdoptedPetHubScreen
import com.projects.hanoipetadoption.ui.screens.myadoptedpets.MyAdoptedPetsListScreen

@Composable
fun PetAdoptionApp() {
    val navController = rememberNavController()
    val navigationItems = listOf(
//        NavigationItem(
//            route = "home",
//            selectedIcon = Icons.Filled.Home,
//            unselectedIcon = Icons.Outlined.Home,
//            titleRes = R.string.home
//        ),
        NavigationItem(
            route = "pets",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite,
            titleRes = R.string.pets
        ),
        NavigationItem(
            route = "my_adopted_pets",
            selectedIcon = Icons.Filled.Pets,
            unselectedIcon = Icons.Outlined.Pets,
            titleRes = R.string.my_adopted_pets
        ),
//        NavigationItem(
//            route = "donate",
//            selectedIcon = Icons.Filled.Favorite,
//            unselectedIcon = Icons.Outlined.Favorite,
//            titleRes = R.string.donate
//        ),
        NavigationItem(
            route = "about",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            titleRes = R.string.about
        ),
    )
    
    var showBottomBar by rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
      // Hide bottom bar on detail screens
    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "pet_detail/{petId}" -> false
        "adoption_application/{petId}/{petName}" -> false
        "adopted_pet_hub/{petId}" -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination
                    
                    navigationItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = null
                                ) 
                            },
                            label = { Text(text = stringResource(id = item.titleRes)) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->        
        NavHost(
            navController = navController,
            startDestination = "pets",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("pets") {
                PetsScreen(navController)
            }
            composable("about") {
                AboutScreen(navController)
            }
            composable(
                route = "pet_detail/{petId}",
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId") ?: ""
                PetDetailScreen(navController, petId)
            }
            composable(
                route = "adoption_application/{petId}/{petName}",
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId") ?: ""
                val petName = backStackEntry.arguments?.getString("petName") ?: ""
                AdoptionApplicationScreen(navController, petId, petName)
            }
            
            // New routes for post-adoption features
            composable("my_adopted_pets") {
                MyAdoptedPetsListScreen(navController)
            }
            composable(
                route = "adopted_pet_hub/{petId}",
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId")?.toIntOrNull() ?: -1
                if (petId != -1) {
                    AdoptedPetHubScreen(navController, petId)
                }
            }
        }
    }
}