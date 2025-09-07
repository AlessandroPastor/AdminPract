package com.example.adminmovile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.presentation.navigation.NavigationGraph
import com.example.adminmovile.presentation.screens.viewmodel.ProvideHomeViewModel
import com.example.adminmovile.presentation.navigation.Routes
import com.example.adminmovile.presentation.theme.AppTheme
import com.example.adminmovile.presentation.theme.ThemeViewModel
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import kotlinx.coroutines.launch



// 🔹 2. App Composable optimizado con manejo de System UI
@Composable
fun App() {
    val navController = rememberNavController()
    val sessionManager: SessionManager = koinInject()
    val scope = rememberCoroutineScope()

    // 🔹 Aquí obtienes el estado del tema desde tu ThemeViewModel
    val themeViewModel: ThemeViewModel = koinInject()
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    KoinContext {
        ProvideHomeViewModel {
            // 🔹 El AppTheme envuelve TODO
            AppTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(
                        navController = navController,
                        onLogout = {
                            scope.launch {
                                sessionManager.clearSession()
                                navController.navigate(Routes.LAND_PAGE) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
