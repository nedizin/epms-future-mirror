package com.epms.epmssmartmirror.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradients {
    val homeScrim: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to Color(0x66000000),
            0.45f to Color(0x99000814),
            1f to Color(0xF20B1F3B)
        )
    )

    val deepNavyFade: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0B1F3B), Color(0xFF050C1A))
    )

    fun accentCard(accent: Color): Brush = Brush.linearGradient(
        colors = listOf(
            accent.copy(alpha = 0.22f),
            Color(0xCC0B1F3B),
            Color(0xFF050C1A)
        ),
        start = Offset(0f, 0f),
        end = Offset(900f, 900f)
    )

    fun monogramHalo(accent: Color): Brush = Brush.radialGradient(
        colors = listOf(accent.copy(alpha = 0.95f), accent.copy(alpha = 0.20f)),
        radius = 220f
    )

    val glassPanel: Brush = Brush.verticalGradient(
        colors = listOf(Color(0xCC101F38), Color(0xE60B1A33))
    )
}
