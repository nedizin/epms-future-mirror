package com.epms.epmssmartmirror.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epms.epmssmartmirror.R
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.ui.theme.Amber
import com.epms.epmssmartmirror.ui.theme.EPMSOrange
import com.epms.epmssmartmirror.ui.theme.Gradients
import com.epms.epmssmartmirror.ui.theme.MutedText
import com.epms.epmssmartmirror.ui.theme.TextColor
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(onStart: () -> Unit) {
    var messageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3500)
            messageIndex = (messageIndex + 1) % AppData.homeMessages.size
        }
    }

    val infinite = rememberInfiniteTransition(label = "home")
    val haloAlpha by infinite.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )
    val ctaScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cta"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onStart() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gradients.homeScrim)
        )

        // Amber glow halo — drawBehind with explicit radius so gradient is a true
        // soft circle (no hard clipped edge)
        Box(
            modifier = Modifier
                .size(380.dp)
                .align(Alignment.Center)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Amber.copy(alpha = haloAlpha),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.minDimension / 2f
                        )
                    )
                }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_epms),
                contentDescription = "EPMS Logo",
                modifier = Modifier.height(104.dp)
            )

            Text(
                text = AppData.APP_TITLE,
                color = TextColor,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.2.sp
            )

            // AnimatedContent keeps the layout height stable across message changes
            // so the logo and button never shift position
            AnimatedContent(
                targetState = messageIndex,
                transitionSpec = {
                    (fadeIn(tween(420)) + slideInVertically(tween(420)) { it / 3 })
                        .togetherWith(fadeOut(tween(280)) + slideOutVertically(tween(280)) { -it / 3 })
                },
                label = "message",
                modifier = Modifier.heightIn(min = 52.dp)
            ) { index ->
                Text(
                    text = AppData.homeMessages[index],
                    color = Amber,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EPMSOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .scale(ctaScale)
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(50.dp),
                        ambientColor = EPMSOrange,
                        spotColor = EPMSOrange
                    )
            ) {
                Text(
                    text = "Toca para começar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 36.dp, vertical = 10.dp),
                    letterSpacing = 0.5.sp
                )
            }
        }

        Text(
            text = AppData.WEBSITE,
            color = MutedText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 22.dp)
        )
    }
}
