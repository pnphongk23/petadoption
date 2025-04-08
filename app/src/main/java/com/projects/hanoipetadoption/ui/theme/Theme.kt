package com.projects.hanoipetadoption.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFC107),           // Primary brand color - Amber
    onPrimary = Color(0xFF000000),         // Black text on amber for better contrast
    primaryContainer = Color(0xFFFFECB3),  // Light amber for containers
    onPrimaryContainer = Color(0xFF362A00), // Dark brown text on light amber containers
    secondary = Color(0xFFFA6348),         // Accent color - warm orange for adoption actions
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDBD3), // Lighter variant of secondary for containers
    onSecondaryContainer = Color(0xFF3E0100), // Text on secondary container
    tertiary = Color(0xFF7D5700),          // Complementary color - dark amber
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDEA1), // Light amber variant for tertiary containers
    onTertiaryContainer = Color(0xFF271900), // Text on tertiary container
    background = Color(0xFFFFFBFF),        // Off-white background
    onBackground = Color(0xFF1A1C1E),      // Text on background
    surface = Color.White,                 // Surface color
    onSurface = Color(0xFF1A1C1E),         // Text on surface
    error = Color(0xFFBA1A1A),             // Error color
    outline = Color(0xFF72787E),           // Border color
    surfaceVariant = Color(0xFFE7E0EC),    // Alternative surface color
    onSurfaceVariant = Color(0xFF49454F),  // Text on alternative surface
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD54F),           // Lighter amber for dark mode
    onPrimary = Color(0xFF3F2E00),         // Dark brown text on amber
    primaryContainer = Color(0xFF5A4200),  // Darker amber for containers in dark mode
    onPrimaryContainer = Color(0xFFFFDF94), // Light amber text on dark containers
    secondary = Color(0xFFFFB4A1),         // Lighter orange for dark mode
    onSecondary = Color(0xFF5C1900),       // Dark text on secondary
    secondaryContainer = Color(0xFF802800), // Darker variant of secondary for containers
    onSecondaryContainer = Color(0xFFFFDBD3), // Light text on secondary container
    tertiary = Color(0xFFEDC148),          // Gold color for dark mode
    onTertiary = Color(0xFF412D00),        // Dark text on tertiary
    tertiaryContainer = Color(0xFF5B4300), // Dark amber for tertiary containers
    onTertiaryContainer = Color(0xFFFFDEA1), // Light text on tertiary container
    background = Color(0xFF1A1C1E),        // Dark background
    onBackground = Color(0xFFE2E2E6),      // Light text on background
    surface = Color(0xFF101114),           // Dark surface
    onSurface = Color(0xFFE2E2E6),         // Light text on surface
    error = Color(0xFFFFB4AB),             // Light red for errors in dark mode
    outline = Color(0xFF8C9198),           // Border color for dark mode
    surfaceVariant = Color(0xFF49454F),    // Alternative dark surface
    onSurfaceVariant = Color(0xFFCAC4D0),  // Light text on alternative surface
)

@Composable
fun PetAdoptionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to use our custom color scheme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}