package com.example.synora.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ── Extra dark-mode surface tokens ───────────────────────────────────────────
// Material3 dark surfaces follow an elevation-based tonal system.
// We define explicit levels so cards/bubbles lift off the background.
private val DarkBackground    = Color(0xFF0F0F14) // deepest — window bg
private val DarkSurface       = Color(0xFF1A1A24) // cards, sheets
private val DarkSurfaceVariant = Color(0xFF252535) // input fields, chips
private val DarkOutline       = Color(0xFF3A3A50) // dividers, borders

private val LightColorScheme = lightColorScheme(
    primary              = Indigo500,
    onPrimary            = Color.White,
    primaryContainer     = Indigo100,
    onPrimaryContainer   = Indigo600,
    secondary            = Purple500,
    onSecondary          = Color.White,
    secondaryContainer   = Purple100,
    onSecondaryContainer = Purple600,
    tertiary             = Cyan500,
    onTertiary           = Color.White,
    tertiaryContainer    = Cyan100,
    onTertiaryContainer  = Cyan600,
    background           = Gray50,
    onBackground         = Gray900,
    surface              = Color.White,
    onSurface            = Gray900,
    surfaceVariant       = Gray100,
    onSurfaceVariant     = Gray600,
    error                = ErrorRed,
    onError              = Color.White,
    outline              = Gray200,
    outlineVariant       = Gray100,
)

private val DarkColorScheme = darkColorScheme(
    primary              = Indigo500,        // keep brand Indigo — not washed-out light
    onPrimary            = Color.White,
    primaryContainer     = Indigo600,
    onPrimaryContainer   = Indigo100,
    secondary            = Purple500,
    onSecondary          = Color.White,
    secondaryContainer   = Purple600,
    onSecondaryContainer = Purple100,
    tertiary             = Cyan500,
    onTertiary           = Color.White,
    tertiaryContainer    = Cyan600,
    onTertiaryContainer  = Cyan100,
    background           = DarkBackground,
    onBackground         = Gray50,
    surface              = DarkSurface,      // cards / bubbles lift off background
    onSurface            = Gray50,
    surfaceVariant       = DarkSurfaceVariant, // input fields, chips
    onSurfaceVariant     = Gray400,
    error                = ErrorRed,
    onError              = Color.White,
    outline              = DarkOutline,
    outlineVariant       = DarkSurfaceVariant,
)

@Composable
fun SynoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = SynoraTypography,
        shapes      = SynoraShapes,
        content     = content,
    )
}
