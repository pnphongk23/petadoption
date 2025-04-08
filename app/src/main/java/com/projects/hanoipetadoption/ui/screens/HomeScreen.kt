package com.projects.hanoipetadoption.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.projects.hanoipetadoption.R
import com.projects.hanoipetadoption.ui.components.CarouselItem
import com.projects.hanoipetadoption.ui.components.ImageCarousel
import com.projects.hanoipetadoption.ui.components.NewsCard
import com.projects.hanoipetadoption.ui.components.PetCard
import com.projects.hanoipetadoption.ui.components.SectionHeading
import com.projects.hanoipetadoption.ui.components.StatisticCard
import com.projects.hanoipetadoption.ui.components.SupportCallToAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /* Search functionality */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero carousel
            val carouselItems = listOf(
                CarouselItem(
                    imageRes = androidx.core.R.drawable.ic_call_decline,
                    title = "Adopt, Don't Shop!",
                    description = "Give a home to a pet in need",
                    actionLabel = "View Pets"
                ),
                CarouselItem(
                    imageRes = androidx.core.R.drawable.ic_call_decline,
                    title = "Support Our Mission",
                    description = "Help us save more animals",
                    actionLabel = "Donate Now"
                ),
                CarouselItem(
                    imageRes = androidx.core.R.drawable.ic_call_decline,
                    title = "Join Our Community",
                    description = "Volunteer and make a difference",
                    actionLabel = "Learn More"
                )
            )
            
            ImageCarousel(
                items = carouselItems,
                onActionClick = { index ->
                    when (index) {
                        0 -> navController.navigate("pets")
                        1 -> navController.navigate("donate")
                        2 -> navController.navigate("about")
                    }
                }
            )

            // Services section
            SectionHeading(title = "Our Services")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ServiceIcon(
                    iconRes = androidx.core.R.drawable.ic_call_decline,
                    label = "Adoption",
                    onClick = { navController.navigate("pets") }
                )
                ServiceIcon(
                    iconRes = androidx.core.R.drawable.ic_call_decline,
                    label = "Rescue",
                    onClick = { navController.navigate("about") }
                )
                ServiceIcon(
                    iconRes = androidx.core.R.drawable.ic_call_decline,
                    label = "Pet Care",
                    onClick = { navController.navigate("about") }
                )
                ServiceIcon(
                    iconRes = androidx.core.R.drawable.ic_call_decline,
                    label = "Support",
                    onClick = { navController.navigate("donate") }
                )
            }

            // Statistics
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Our Impact",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatisticCard(
                            value = "500+",
                            label = stringResource(id = R.string.rescued_pets),
                            modifier = Modifier.weight(1f)
                        )
                        StatisticCard(
                            value = "300+",
                            label = stringResource(id = R.string.successful_adoptions),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatisticCard(
                            value = "50+",
                            label = stringResource(id = R.string.active_volunteers),
                            modifier = Modifier.weight(1f)
                        )
                        StatisticCard(
                            value = "5+",
                            label = stringResource(id = R.string.years_experience),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Featured pets section
            SectionHeading(
                title = stringResource(id = R.string.featured_pets),
                actionText = "See All",
                onActionClick = { navController.navigate("pets") }
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PetCard(
                    name = "Luna",
                    imageRes = "https://picsum.photos/200/300",
                    species = "Cat",
                    age = "1 year",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("pets") }
                )
                PetCard(
                    name = "Max",
                    imageRes = "https://picsum.photos/200/300",
                    species = "Dog",
                    age = "2 years",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("pets") }
                )
                PetCard(
                    name = "Bailey",
                    imageRes = "https://picsum.photos/200/300",
                    species = "Dog",
                    age = "3 years",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate("pets") }
                )
            }

            // Latest news section
            SectionHeading(
                title = stringResource(id = R.string.latest_news),
                actionText = "View All",
                onActionClick = { /* Navigate to news section */ }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NewsCard(
                    title = "Adoption Event This Weekend",
                    imageRes = "https://picsum.photos/200/300",
                    date = "April 5, 2025",
                    summary = "Join us this Saturday for our monthly adoption event. Meet our wonderful pets looking for forever homes!",
                    onClick = { /* Navigate to news detail */ }
                )
                
                NewsCard(
                    title = "Pet Care Tips for Summer",
                    imageRes = "https://picsum.photos/200/300",
                    date = "March 28, 2025",
                    summary = "Learn how to keep your pets safe and comfortable during the hot summer months with these essential care tips.",
                    onClick = { /* Navigate to news detail */ }
                )
            }

            // Support call to action
            SupportCallToAction(
                title = stringResource(id = R.string.support_us),
                description = "Help us continue our mission to rescue and rehome animals in need.",
                buttonText = stringResource(id = R.string.donate_now),
                onClick = { navController.navigate("donate") }
            )

            // Footer space
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceIcon(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .size(70.dp)
                .padding(4.dp),
            shape = CircleShape,
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}