package com.epms.epmssmartmirror.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epms.epmssmartmirror.R
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.data.Profile
import com.epms.epmssmartmirror.ui.theme.Amber
import com.epms.epmssmartmirror.ui.theme.MutedText
import com.epms.epmssmartmirror.ui.theme.TextColor
import com.epms.epmssmartmirror.utils.generateQrBitmap

@Composable
fun ThematicOverlay(
    profile: Profile,
    modifier: Modifier = Modifier
) {
    val qr = remember { generateQrBitmap(AppData.QR_LINK, 240) }
    Box(modifier = modifier.fillMaxSize()) {
        // Top-left logo badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 88.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_epms),
                contentDescription = "EPMS",
                modifier = Modifier.height(28.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(AppData.WEBSITE, color = TextColor, fontSize = 12.sp)
        }

        // Bottom moldure (accent bar + info + QR)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(profile.accent)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xDD0B1F3B))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(profile.accent.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = profile.emoji, fontSize = 20.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        profile.name,
                        color = profile.accent,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(profile.headline, color = TextColor, fontSize = 11.sp)
                    Text(profile.futureLine, color = Amber, fontSize = 11.sp)
                    Text(AppData.FUTURE_MESSAGE, color = MutedText, fontSize = 10.sp)
                }
                Spacer(Modifier.width(10.dp))
                qr?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR EPMS",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}
