package com.example.adminmovile.presentation.screens.land_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.adminmovile.presentation.components.BottomNavigationBar
import com.example.adminmovile.presentation.components.MainTopAppBar
import com.example.adminmovile.presentation.components.NotificationHost
import com.example.adminmovile.presentation.components.NotificationState
import com.example.adminmovile.presentation.components.PullToRefreshComponent
import com.example.adminmovile.presentation.navigation.Routes
import com.example.adminmovile.presentation.screens.navigation.LoadingScreen
import com.example.adminmovile.presentation.theme.AppDimensions
import com.example.adminmovile.presentation.theme.LocalAppDimens

@Composable
fun BaseExternalLayout(
    title: String,
    isSearchVisible: Boolean,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onToggleSearch: () -> Unit,
    onCloseSearch: () -> Unit,
    onClickExplorer: () -> Unit,
    onStartClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    navController: NavHostController,
    notificationState: MutableState<NotificationState>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    // Scaffold estático, no se destruye en cambios de screen
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(
                title = title,
                isSearchVisible = isSearchVisible,
                searchQuery = searchQuery,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                onToggleSearch = onToggleSearch,
                onCloseSearch = onCloseSearch,
                onClickExplorer = onClickExplorer,
                onStartClick = onStartClick,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Mostrar solo en screens públicas
            if (currentRoute !in listOf(Routes.LOGIN, Routes.REGISTER, Routes.SPLASH)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->

        // 🔹 Solo el contenido es recomposable
        Box(modifier = Modifier.fillMaxSize()) {
            // Notifications y PullToRefresh internos
            NotificationHost(state = notificationState) {
                PullToRefreshComponent(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    content(innerPadding)
                }
            }
        }
    }
}



val MaterialTheme.dimens: AppDimensions
    @Composable get() = LocalAppDimens.current

@Composable
fun BaseScreenLayout(
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.dimens.screenPadding.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        content()

        // Loading overlay animado solo cuando isLoading = true
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingScreen("Cargando datos, por favor espere...")
            }
        }
    }
}



