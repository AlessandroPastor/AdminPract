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
import com.example.adminmovile.presentation.components.BottomNavigationBar
import com.example.adminmovile.presentation.components.MainTopAppBar
import com.example.adminmovile.presentation.components.NotificationHost
import com.example.adminmovile.presentation.components.NotificationState
import com.example.adminmovile.presentation.components.PullToRefreshComponent
import com.example.adminmovile.presentation.navigation.Routes
import com.example.adminmovile.presentation.screens.navigation.LoadingScreen
import com.example.adminmovile.presentation.theme.AppColors
import com.example.adminmovile.presentation.theme.AppDimensions
import com.example.adminmovile.presentation.theme.AppTheme
import com.example.adminmovile.presentation.theme.LocalAppColors
import com.example.adminmovile.presentation.theme.LocalAppDimens
import com.example.adminmovile.presentation.theme.ThemeViewModel
import org.koin.compose.koinInject

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
    currentSection: LangPageViewModel.Sections,
    onSectionSelected: (LangPageViewModel.Sections) -> Unit,
    navController: NavController,
    notificationState: MutableState<NotificationState>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
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
                BottomNavigationBar(
                    currentSection = currentSection,
                    onSectionSelected = onSectionSelected,
                    navController = navController
                )
            }
        ) { innerPadding ->

            // 🔹 Notification + PullToRefresh englobando el contenido
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



val MaterialTheme.dimens: AppDimensions
    @Composable get() = LocalAppDimens.current

val MaterialTheme.extraColors: AppColors
    @Composable get() = LocalAppColors.current

@Composable
fun BaseScreenLayout(
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.dimens.screenPadding.dp), // 👈 ahora usa AppDimensions
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding) // 👈 ya respeta el diseño global
    ) {
        content()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)), // 👈 usa colorScheme
                contentAlignment = Alignment.Center
            ) {
                LoadingScreen("Cargando datos, por favor espere...")
            }
        }
    }
}


