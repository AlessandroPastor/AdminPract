package com.example.adminmovile.presentation.screens.land_page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.adminmovile.presentation.components.BottomNavigationBar
import com.example.adminmovile.presentation.components.MainTopAppBar
import com.example.adminmovile.presentation.components.NotificationHost
import com.example.adminmovile.presentation.components.NotificationType
import com.example.adminmovile.presentation.components.PullToRefreshComponent
import com.example.adminmovile.presentation.components.rememberNotificationState
import com.example.adminmovile.presentation.components.showNotification
import com.example.adminmovile.presentation.theme.AppTheme
import com.example.adminmovile.presentation.theme.ThemeViewModel
import org.koin.compose.koinInject


@Composable
fun EmprendedoresScreen(
    onStartClick: () -> Unit,
    onClickExplorer: () -> Unit,
    navController: NavHostController,
    viewModel: LangPageViewModel,
) {
    val  themeViewModel: ThemeViewModel = koinInject()
    val lazyListState = rememberLazyListState()
    var isBottomNavVisible by remember { mutableStateOf(true) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    var scrollDirection by remember { mutableStateOf(LangPageViewModel.ScrollDirection.NONE) }
    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemScrollOffset
        }.collect { currentScrollOffset ->
            val scrollDifference = currentScrollOffset - previousScrollOffset

            scrollDirection = when {
                scrollDifference > 50 -> LangPageViewModel.ScrollDirection.DOWN // Scroll hacia abajo
                scrollDifference < -50 -> LangPageViewModel.ScrollDirection.UP   // Scroll hacia arriba
                else -> scrollDirection // Mantener dirección actual
            }

            // Controlar visibilidad basado en la dirección y posición
            isBottomNavVisible = when {
                lazyListState.firstVisibleItemIndex == 0 && currentScrollOffset < 50 -> true // Mostrar en el top
                scrollDirection == LangPageViewModel.ScrollDirection.UP -> true  // Mostrar al scroll hacia arriba
                scrollDirection == LangPageViewModel.ScrollDirection.DOWN -> false // Ocultar al scroll hacia abajo
                else -> isBottomNavVisible // Mantener estado actual
            }

            previousScrollOffset = currentScrollOffset
        }
    }

    // Estados
    val notificationState = rememberNotificationState()
    val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle(
        initialValue = false,
        lifecycle = LocalLifecycleOwner.current.lifecycle
    )
    var isRefreshing by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    // Efectos
    LaunchedEffect(Unit) {

        viewModel.loadService()
        // Mostrar notificación de bienvenida
        notificationState.showNotification(
            message = "Productos y Servicios de las familias",
            type = NotificationType.SUCCESS,
            duration = 3500 // Duración de la notificación
        )
    }




    // UI
    NotificationHost(state = notificationState) {
        Scaffold(
            topBar = {
                MainTopAppBar(
                    title = "Servicios",
                    isSearchVisible = isSearchVisible,
                    searchQuery = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { viewModel.loadService(category = searchQuery) },
                    onToggleSearch = { isSearchVisible = !isSearchVisible },
                    onCloseSearch = {
                            isSearchVisible = false
                            searchQuery = ""
                            viewModel.loadService()
                        },
                    onClickExplorer = onClickExplorer,
                    onStartClick = onStartClick,
                    isDarkMode = isDarkMode,
                    onToggleTheme = { themeViewModel.toggleTheme() },
                    searchPlaceholder = "Busca emprendedores"
                    )
                },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    isVisible = isBottomNavVisible
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .padding(innerPadding)
                ) {
                    // Contenido principal con Pull-to-refresh
                    PullToRefreshComponent(
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            isRefreshing = true
                            viewModel.loadService()
                        }
                    ) {
                        //
                        Text(text = "Screen de Emprendedores")
                    }
                }
            }
        }
}
