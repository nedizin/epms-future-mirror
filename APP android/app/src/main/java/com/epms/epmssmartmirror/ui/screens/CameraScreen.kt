package com.epms.epmssmartmirror.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.epms.epmssmartmirror.data.AppData
import com.epms.epmssmartmirror.ui.components.ThematicOverlay
import com.epms.epmssmartmirror.ui.theme.DeepNavy
import com.epms.epmssmartmirror.ui.theme.EPMSOrange
import com.epms.epmssmartmirror.ui.theme.MutedText
import com.epms.epmssmartmirror.ui.theme.TextColor
import kotlinx.coroutines.delay
import java.io.File

private enum class CaptureState { IDLE, COUNTDOWN, CAPTURING, DONE }

@Composable
fun CameraScreen(
    profileIndex: Int,
    onPhotoCaptured: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val profile = AppData.profiles[profileIndex]

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!hasCameraPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepNavy),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Precisamos de acesso à câmara.", color = TextColor, fontSize = 18.sp)
                Text(
                    "Por favor, autoriza o acesso nas definições.",
                    color = MutedText,
                    fontSize = 14.sp
                )
            }
        }
        return
    }

    var useFrontCamera by remember { mutableStateOf(true) }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    LaunchedEffect(hasCameraPermission) {
        cameraController.bindToLifecycle(lifecycleOwner)
    }

    LaunchedEffect(useFrontCamera) {
        cameraController.cameraSelector =
            if (useFrontCamera) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
    }

    var state by remember { mutableStateOf(CaptureState.IDLE) }
    var countdown by remember { mutableIntStateOf(AppData.COUNTDOWN_SECONDS) }
    var flash by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state == CaptureState.COUNTDOWN) {
            countdown = AppData.COUNTDOWN_SECONDS
            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            flash = true
            delay(120)
            state = CaptureState.CAPTURING
            capturePhoto(
                context = context,
                cameraController = cameraController,
                onSuccess = {
                    if (state != CaptureState.DONE) {
                        state = CaptureState.DONE
                        onPhotoCaptured()
                    }
                },
                onError = {
                    state = CaptureState.IDLE
                    Toast.makeText(context, "Erro ao tirar a fotografia. Tenta novamente.", Toast.LENGTH_SHORT).show()
                }
            )
            delay(220)
            flash = false
        }
    }

    // Slow pulsing ring around the capture button
    val infinite = rememberInfiniteTransition(label = "shoot")
    val ringScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    controller = cameraController
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Live thematic moldure preview
        ThematicOverlay(profile = profile)

        // Top glass bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.75f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlassChip(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = TextColor
                )
            }
            Text(
                text = profile.name,
                color = profile.accent,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            )
            if (state == CaptureState.IDLE) {
                GlassChip(onClick = { useFrontCamera = !useFrontCamera }) {
                    Text(
                        text = if (useFrontCamera) "Frontal" else "Traseira",
                        color = TextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }
        }

        // Countdown — big number in a translucent circle with halo
        AnimatedVisibility(
            visible = state == CaptureState.COUNTDOWN && countdown > 0,
            enter = fadeIn() + scaleIn(initialScale = 1.6f),
            exit = fadeOut() + scaleOut(targetScale = 0.4f),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                EPMSOrange.copy(alpha = 0.45f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.55f))
                        .border(
                            width = 3.dp,
                            color = EPMSOrange.copy(alpha = 0.8f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = countdown.toString(),
                        color = Color.White,
                        fontSize = 140.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (state == CaptureState.CAPTURING) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.62f))
                    .padding(horizontal = 24.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "A tirar fotografia…",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Shoot button — large circular with pulsing ring
        AnimatedVisibility(
            visible = state == CaptureState.IDLE,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 160.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                // Outer pulsing ring
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .scale(ringScale)
                        .clip(CircleShape)
                        .border(width = 4.dp, color = Color.White.copy(alpha = 0.85f), shape = CircleShape)
                )
                // Inner orange core
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            ambientColor = EPMSOrange,
                            spotColor = EPMSOrange
                        )
                        .clip(CircleShape)
                        .background(EPMSOrange)
                        .clickable { state = CaptureState.COUNTDOWN },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }

        // White flash on capture
        if (flash) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
    }
}

@Composable
private fun GlassChip(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val photoFile = File(context.cacheDir, "epms_capture.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    cameraController.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                onSuccess()
            }

            override fun onError(exception: ImageCaptureException) {
                onError()
            }
        }
    )
}
