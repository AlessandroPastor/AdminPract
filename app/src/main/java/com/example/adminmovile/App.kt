package com.example.adminmovile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.presentation.navigation.NavigationGraph
import com.example.adminmovile.presentation.navigation.Routes
import com.example.adminmovile.presentation.screens.viewmodel.ProvideHomeViewModel
import com.example.adminmovile.presentation.theme.AppTheme
import com.example.adminmovile.presentation.theme.ThemeViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.KoinContext

@Composable
fun App() {
    val navController = rememberNavController()
    val sessionManager: SessionManager = koinInject()
    val scope = rememberCoroutineScope()
    val themeViewModel: ThemeViewModel = koinInject()
    val isDarkModeState by themeViewModel.isDarkMode.collectAsState(initial = null)

    if (isDarkModeState == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White))
    } else {
        ProvideHomeViewModel {
            AppTheme(darkTheme = isDarkModeState!!) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController, onLogout = {
                        scope.launch {
                            sessionManager.clearSession()
                            navController.navigate(Routes.LAND_PAGE) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    })
                }
            }
        }
    }
}

