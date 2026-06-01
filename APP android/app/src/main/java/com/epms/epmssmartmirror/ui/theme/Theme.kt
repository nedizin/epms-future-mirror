package com.epms.epmssmartmirror.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EPMSColorScheme = darkColorScheme(
    primary = Navy,
    secondary = EPMSOrange,
    tertiary = Amber,
    background = DeepNavy,
    surface = Surface,
    surfaceVariant = SurfaceAlt,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onTertiary = TextColor,
    onBackground = TextColor,
    onSurface = TextColor,
    onSurfaceVariant = MutedText
)

@Composable
fun EPMSSmartMirrorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EPMSColorScheme,
        typography = Typography,
        content = content
    )
}
