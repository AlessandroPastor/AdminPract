package com.example.adminmovile.presentation.screens.land_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.adminmovile.presentation.components.rememberNotificationState
import com.example.adminmovile.presentation.navigation.Routes
import com.example.adminmovile.presentation.theme.ThemeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.example.adminmovile.R

@Composable
fun DefaultScreenExternal(
    title: String,
    navController: NavHostController,
    viewModel: LangPageViewModel? = null,
    onStartClick: () -> Unit,
) {
    val themeViewModel: ThemeViewModel = koinInject()
    val notificationState = rememberNotificationState()
    val coroutineScope = rememberCoroutineScope()
    var isBottomNavVisible by remember { mutableStateOf(true) }
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    val showWhatsAppButton = remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    var previousScrollOffset by remember { mutableStateOf(0) }
    var scrollDirection by remember { mutableStateOf(LangPageViewModel.ScrollDirection.NONE) }

    // Detectar scroll para ocultar/mostrar bottom nav
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
            .collect { offset ->
                val diff = offset - previousScrollOffset
                scrollDirection = when {
                    diff > 50 -> LangPageViewModel.ScrollDirection.DOWN
                    diff < -50 -> LangPageViewModel.ScrollDirection.UP
                    else -> scrollDirection
                }
                isBottomNavVisible = when {
                    lazyListState.firstVisibleItemIndex == 0 && offset < 50 -> true
                    scrollDirection == LangPageViewModel.ScrollDirection.UP -> true
                    scrollDirection == LangPageViewModel.ScrollDirection.DOWN -> false
                    else -> isBottomNavVisible
                }
                previousScrollOffset = offset
            }
    }

    val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle(
        initialValue = false,
        lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    )
    // Mostrar animaciones iniciales
    LaunchedEffect(Unit) {
        delay(500)
        visible = true
        delay(1500)
        showWhatsAppButton.value = true
    }

    BaseExternalLayout(
        title = title,
        isSearchVisible = isSearchVisible,
        searchQuery = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = { },
        onToggleSearch = { isSearchVisible = true },
        onCloseSearch = { isSearchVisible = false },
        onClickExplorer = { navController.navigate(Routes.EXPLORATE) },
        onStartClick = onStartClick,
        isDarkMode = isDarkMode,
        onToggleTheme = { themeViewModel.toggleTheme() },
        navController = navController,
        notificationState = notificationState,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                viewModel?.loadService()
                isRefreshing = false
            }
        }
    ) { innerPadding ->

        // 👇 ESTE BOX YA OCUPA TODO EL ESPACIO DISPONIBLE DEL SCAFFOLD
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (visible) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cono),
                        contentDescription = "En construcción",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(96.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pantalla en desarrollo",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Estamos trabajando para traerte algo increíble",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}