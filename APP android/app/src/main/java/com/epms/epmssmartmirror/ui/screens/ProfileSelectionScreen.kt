package com.epms.epmssmartmirror.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.ui.theme.Amber
import com.epms.epmssmartmirror.ui.theme.Gradients
import com.epms.epmssmartmirror.ui.theme.MutedText
import com.epms.epmssmartmirror.ui.theme.TextColor
import kotlinx.coroutines.delay

@Composable
fun ProfileSelectionScreen(
    onProfileSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gradients.deepNavyFade)
    ) {
        // Hero header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A1A30))
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = TextColor
                )
            }
            Spacer(Modifier.size(4.dp))
            Column {
                Text(
                    text = "Escolhe o teu futuro",
                    color = TextColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = "Seleciona um curso para começar a experiência",
                    color = MutedText,
                    fontSize = 12.sp
                )
            }
        }

        // Accent stripe under header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Amber.copy(alpha = 0.55f))
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(AppData.profiles) { index, profile ->
                ProfileCard(
                    index = index,
                    name = profile.name,
                    headline = profile.headline,
                    accent = profile.accent,
                    emoji = profile.emoji,
                    onClick = { onProfileSelected(index) }
                )
            }
        }
    }
}

@Composable
private fun ProfileCard(
    index: Int,
    name: String,
    headline: String,
    accent: Color,
    emoji: String,
    onClick: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(60L * index)
        entered = true
    }

    AnimatedVisibility(
        visible = entered,
        enter = fadeIn(tween(360)) + slideInVertically(tween(360)) { it / 4 }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Gradients.accentCard(accent))
                .border(
                    width = 1.dp,
                    color = accent.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(18.dp)
                )
                .clickable(onClick = onClick)
        ) {
            // Top accent bar flush with the card edge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(accent)
                    .align(Alignment.TopStart)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 18.dp)
            ) {
                // Emoji badge
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.22f))
                        .border(
                            width = 1.5.dp,
                            color = accent.copy(alpha = 0.55f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 26.sp
                    )
                }

                Text(
                    text = name,
                    color = TextColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    lineHeight = 19.sp
                )
                Text(
                    text = headline,
                    color = MutedText,
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

