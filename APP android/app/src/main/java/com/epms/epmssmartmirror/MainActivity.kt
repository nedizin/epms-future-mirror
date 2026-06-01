package com.epms.epmssmartmirror

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epms.epmssmartmirror.ui.screens.CameraScreen
import com.epms.epmssmartmirror.ui.screens.HomeScreen
import com.epms.epmssmartmirror.ui.screens.ProfileSelectionScreen
import com.epms.epmssmartmirror.ui.screens.ResultScreen
import com.epms.epmssmartmirror.ui.theme.DeepNavy
import com.epms.epmssmartmirror.ui.theme.EPMSSmartMirrorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EPMSSmartMirrorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = DeepNavy) {
                    EPMSNavHost()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}

@Composable
fun EPMSNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onStart = { navController.navigate("profiles") })
        }
        composable("profiles") {
            ProfileSelectionScreen(
                onProfileSelected = { index -> navController.navigate("camera/$index") },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "camera/{profileIndex}",
            arguments = listOf(navArgument("profileIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val profileIndex = backStackEntry.arguments?.getInt("profileIndex") ?: 0
            CameraScreen(
                profileIndex = profileIndex,
                onPhotoCaptured = {
                    navController.navigate("result/$profileIndex") {
                        popUpTo("camera/$profileIndex") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "result/{profileIndex}",
            arguments = listOf(navArgument("profileIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val profileIndex = backStackEntry.arguments?.getInt("profileIndex") ?: 0
            ResultScreen(
                profileIndex = profileIndex,
                onRetake = {
                    navController.navigate("camera/$profileIndex") {
                        popUpTo("result/$profileIndex") { inclusive = true }
                    }
                },
                onNewExperience = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
