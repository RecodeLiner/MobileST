package com.rcl.mobilest.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.rcl.mobilest.di.theme.ThemeManager

val darkColor = darkColorScheme(
    primary = Color(0xFFA4C9FF),
    onPrimary = Color(0xFF003060),
    primaryContainer = Color(0xFF17487D),
    onPrimaryContainer = Color(0xFFD3E3FF),
    inversePrimary = Color(0xFF355F97),
    secondary = Color(0xFFBCC7DB),
    onSecondary = Color(0xFF263141),
    secondaryContainer = Color(0xFF3D4758),
    onSecondaryContainer = Color(0xFFD8E3F8),
    tertiary = Color(0xFFDABCE2),
    onTertiary = Color(0xFF3D2846),
    tertiaryContainer = Color(0xFF553F5D),
    onTertiaryContainer = Color(0xFFF7D9FF),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF1B1B1B),
    onBackground = Color(0xFFE2E2E2),
    surface = Color(0xFF1B1B1B),
    onSurface = Color(0xFFE2E2E2),
    inverseSurface = Color(0xFFE2E2E2),
    inverseOnSurface = Color(0xFF303030),
    surfaceVariant = Color(0xFF474747),
    onSurfaceVariant = Color(0xFFC6C6C6),
    outline = Color(0xFF919191)
)

val lightColor = lightColorScheme(
    primary = Color(0xFF715859),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFCDBDB),
    onPrimaryContainer = Color(0xFF281717),
    inversePrimary = Color(0xFFDFBFC0),
    secondary = Color(0xFF6B5B5B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4DDDE),
    onSecondaryContainer = Color(0xFF241919),
    tertiary = Color(0xFF775658),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD9DB),
    onTertiaryContainer = Color(0xFF2C1516),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFFFBFA),
    onBackground = Color(0xFF1E1B1B),
    surface = Color(0xFFFFFBFA),
    onSurface = Color(0xFF1E1B1B),
    inverseSurface = Color(0xFF332F2F),
    inverseOnSurface = Color(0xFFF7EFEE),
    surfaceVariant = Color(0xFFE9E0E0),
    onSurfaceVariant = Color(0xFF4A4645),
    outline = Color(0xFF7A7574)
)

@Composable
fun MobileSTTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val managerDynamic by themeManager.isDynamicColor.collectAsState()
    val managerDark by themeManager.isDarkTheme.collectAsState()

    // Determine if dark theme should be used
    val isDark = managerDark == true || (managerDark == null && isSystemInDarkTheme())

    // Check if dynamic color scheme should be used (Android 12+)
    val isSystemDynamic = managerDynamic != false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    // Check if system dark theme should be used (Android 10+)
    val isSystemDarkTheme = managerDark == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    // Get the appropriate color scheme
    val colorScheme = getColorScheme(isDark, isSystemDynamic, isSystemDarkTheme)

    // Update window settings if not in edit mode
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            configureWindow(view, isDark)
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
private fun getColorScheme(
    isDark: Boolean,
    isSystemDynamic: Boolean,
    isSystemDarkTheme: Boolean
): ColorScheme {
    val context = LocalContext.current
    return when {
        isSystemDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isSystemDarkTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            if (isDark) darkColor else lightColor
        }

        else -> {
            if (isDark) darkColor else lightColor
        }
    }
}

//Method for prettier window in compose
private fun configureWindow(view: View, isDark: Boolean) {
    val window = (view.context as Activity).window
    window.statusBarColor = Color.Transparent.toArgb()
    window.navigationBarColor = Color.Transparent.toArgb()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isStatusBarContrastEnforced = false
        window.isNavigationBarContrastEnforced = false
    }

    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
}