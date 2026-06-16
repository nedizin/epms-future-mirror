package com.epms.epmssmartmirror.ui.screens

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epms.epmssmartmirror.R
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.ui.theme.Amber
import com.epms.epmssmartmirror.ui.theme.DeepNavy
import com.epms.epmssmartmirror.ui.theme.EPMSOrange
import com.epms.epmssmartmirror.ui.theme.MutedText
import com.epms.epmssmartmirror.ui.theme.Navy
import com.epms.epmssmartmirror.ui.theme.SurfaceAlt
import com.epms.epmssmartmirror.ui.theme.TextColor
import com.epms.epmssmartmirror.utils.PRINT_HEIGHT_PX
import com.epms.epmssmartmirror.utils.PRINT_SAFE_INSET_FRACTION
import com.epms.epmssmartmirror.utils.PRINT_WIDTH_PX
import com.epms.epmssmartmirror.utils.createCompositeBitmap
import com.epms.epmssmartmirror.utils.generateQrBitmap
import com.epms.epmssmartmirror.utils.printPhoto
import com.epms.epmssmartmirror.utils.saveToGallery
import java.io.File

@Composable
fun ResultScreen(
    profileIndex: Int,
    onRetake: () -> Unit,
    onNewExperience: () -> Unit
) {
    val context = LocalContext.current
    val profile = AppData.profiles[profileIndex]
    val photoFile = File(context.cacheDir, "epms_capture.jpg")

    val photoBitmap = remember {
        if (photoFile.exists()) BitmapFactory.decodeFile(photoFile.absolutePath) else null
    }
    val qrBitmap = remember { generateQrBitmap(AppData.QR_LINK, 240) }

    if (photoBitmap == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepNavy),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    "Não foi possível carregar a fotografia.",
                    color = TextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Button(
                    onClick = onRetake,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EPMSOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Tentar novamente", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {
        Image(
            bitmap = photoBitmap.asImageBitmap(),
            contentDescription = "Fotografia",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top glass bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.78f), Color.Transparent)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo_epms),
                contentDescription = "EPMS Logo",
                modifier = Modifier.height(40.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    AppData.APP_TITLE,
                    color = TextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp
                )
                Text(AppData.WEBSITE, color = MutedText, fontSize = 11.sp)
            }
        }

        // Bottom panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(profile.accent)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xE60B1F3B), Color(0xF2050C1A))
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(profile.name, color = profile.accent, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text(profile.headline, color = TextColor, fontSize = 12.sp)
                    Text(profile.futureLine, color = Amber, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text(AppData.FUTURE_MESSAGE, color = MutedText, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                qrBitmap?.let { qr ->
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .shadow(8.dp, RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = qr.asImageBitmap(),
                            contentDescription = "QR Code EPMS",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF050C1A))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    text = "Repetir",
                    containerColor = SurfaceAlt,
                    modifier = Modifier.weight(1f),
                    onClick = onRetake
                )
                ActionButton(
                    text = "Guardar",
                    containerColor = Navy,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val composite = createCompositeBitmap(
                            context, photoBitmap, profile,
                            outputWidth = PRINT_WIDTH_PX,
                            outputHeight = PRINT_HEIGHT_PX,
                            safeInsetFraction = PRINT_SAFE_INSET_FRACTION
                        )
                        val saved = saveToGallery(context, composite) != null
                        val msg = if (saved) "Guardado na galeria!" else "Erro ao guardar."
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
                ActionButton(
                    text = "Imprimir",
                    containerColor = Color(0xFF1A4A2E),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val printable = createCompositeBitmap(
                            context, photoBitmap, profile,
                            outputWidth = PRINT_WIDTH_PX,
                            outputHeight = PRINT_HEIGHT_PX,
                            safeInsetFraction = PRINT_SAFE_INSET_FRACTION
                        )
                        printPhoto(context, printable, portrait = false)
                    }
                )
                ActionButton(
                    text = "Nova Experiência",
                    containerColor = EPMSOrange,
                    contentColor = Color.White,
                    modifier = Modifier.weight(1.4f),
                    onClick = onNewExperience,
                    glow = true
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = TextColor,
    glow: Boolean = false,
    onClick: () -> Unit
) {
    val buttonModifier = if (glow) {
        modifier.shadow(
            elevation = 14.dp,
            shape = RoundedCornerShape(14.dp),
            ambientColor = containerColor,
            spotColor = containerColor
        )
    } else modifier

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
        modifier = buttonModifier
    ) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
