package com.example.adminmovile.presentation.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.presentation.screens.configuration.ad.modules.ModuleScreen
import com.example.adminmovile.presentation.screens.configuration.ad.modulos_padres.ParentModuleScreen
import com.example.adminmovile.presentation.screens.configuration.ad.role.RoleScreen
import com.example.adminmovile.presentation.screens.login.LoginScreen
import com.example.adminmovile.presentation.screens.dashboard.HomeViewModel
import com.example.adminmovile.presentation.screens.land_page.DefaultScreenExternal
import com.example.adminmovile.presentation.screens.land_page.EmprendedoresScreen
import com.example.adminmovile.presentation.screens.land_page.EventsScreen
import com.example.adminmovile.presentation.screens.land_page.ExplorerScreen
import com.example.adminmovile.presentation.screens.land_page.LangPageViewModel
import com.example.adminmovile.presentation.screens.land_page.ServiceScreen
import com.example.adminmovile.presentation.screens.land_page.PlacesScreen
import com.example.adminmovile.presentation.screens.land_page.RecommendationsScreen
import com.example.adminmovile.presentation.screens.land_page.WelcomeScreen
import com.example.adminmovile.presentation.screens.login.ProfileEditScreen
import com.example.adminmovile.presentation.screens.login.ProfileViewModel
import com.example.adminmovile.presentation.screens.login.RegisterScreen
import com.example.adminmovile.presentation.screens.navigation.BaseScreenLayout
import com.example.adminmovile.presentation.screens.navigation.DefaultScreen
import com.example.adminmovile.presentation.screens.navigation.OnboardingScreen
import com.example.adminmovile.presentation.screens.navigation.SplashScreen
import com.example.adminmovile.presentation.screens.navigation.TouristInfoScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    sessionManager: SessionManager = koinInject()
) {
    val viewModelLangPage: LangPageViewModel = koinInject()
    val scope = rememberCoroutineScope()
    var pendingNavigation by remember { mutableStateOf<String?>(null) }

    val publicRoutes = setOf(
        Routes.SPLASH,
        Routes.ONBOARDING,
        Routes.LAND_PAGE,
        Routes.LOGIN,
        Routes.EXPLORATE,
        Routes.REGISTER,
        Routes.SERVICES,
        Routes.PLACES,
        Routes.EVENTS,
        Routes.RECOMMENDATIONS,
        Routes.PROGRESS,
        Routes.PRACTICE
    )

    LaunchedEffect(sessionManager) {
        // Solo navega cuando pendingNavigation cambia
        snapshotFlow { pendingNavigation }.collect { route ->
            route?.let {
                if (it != navController.currentBackStackEntry?.destination?.route) {
                    navController.navigate(it) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    pendingNavigation = null
                }
            }
        }
    }
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collectLatest { backStackEntry ->
            scope.launch {
                val route = backStackEntry.destination.route
                val tokenValid = sessionManager.isTokenValid()
                if (tokenValid && (route == Routes.LOGIN || route == Routes.REGISTER)) {
                    navController.navigate(Routes.LAND_PAGE) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
                if (!tokenValid && route !in publicRoutes) {
                    if (route != null) {
                        sessionManager.setPendingRoute(route)
                    }
                    onLogout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LAND_PAGE) { inclusive = false }
                    }
                }
            }
        }
    }



    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // En tu NavGraph o donde tengas la navegación
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    scope.launch {
                        val tokenValid = sessionManager.isTokenValid()
                        val isFirstTime = !sessionManager.isOnboardingCompleted()
                        if (tokenValid) {
                            navController.navigate(Routes.LAND_PAGE) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        } else if (isFirstTime) {
                            navController.navigate(Routes.ONBOARDING) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Routes.LAND_PAGE) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        // Onboarding Screen
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    scope.launch {
                        sessionManager.setOnboardingCompleted(true)
                        navController.navigate(Routes.LAND_PAGE) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Register
        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = { user -> scope.launch {
                    navigateAfterAuth(
                        navController = navController,
                        sessionManager = sessionManager,
                        popUpRoute = Routes.REGISTER
                    )
                }
                },
                onBackPressed = { navController.popBackStack() }
            )
        }


        composable(Routes.UPDATE_PERFIL) {
            val profileViewModel: ProfileViewModel = koinInject()
            val homeViewModel: HomeViewModel = koinInject()
            val sessionManager: SessionManager = koinInject()
            ProfileEditScreen(
                viewModel = profileViewModel,
                sessionManager = sessionManager,
                navController = navController,
                onProfileUpdated = {
                    homeViewModel.refreshUser()
                    navController.popBackStack() // Aquí navegas hacia atrás después de editar
                }
            )
        }

        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { user ->
                    scope.launch {
                            navigateAfterAuth(
                            navController = navController,
                            sessionManager = sessionManager,
                            popUpRoute = Routes.LOGIN
                        )
                    }
                },
                onBackPressed = { navController.navigate(Routes.LAND_PAGE) { popUpTo(Routes.LOGIN) { inclusive = true } } }
            )
        }



        // Explorer Screen
        composable(Routes.EXPLORATE) {
            ExplorerScreen(
            )
        }

        // Home / Menu
        composable(Routes.HOME) {
            BaseScreenLayout(
                navController = navController,
                title = "Inicio",
                onLogout = {
                    scope.launch {
                        sessionManager.clearSession()
                        navController.navigate(Routes.LAND_PAGE) {
                            popUpTo(Routes.LAND_PAGE) { inclusive = true }
                        }
                    }
                }
            ) { paddingValues ->
                DefaultScreen(
                    title = "Inicio",
                    route = Routes.HOME,
                    navController = navController,
                    onLogout = onLogout,
                    paddingValues = paddingValues
                )
            }
        }

        // BottomNavigation INFERIOR

        // Welcome / Land Page
        composable(Routes.LAND_PAGE) {
            WelcomeScreen(
                navController = navController,
                viewModel = viewModelLangPage,
                onStartClick = {
                    scope.launch {
                        val tokenValid = sessionManager.isTokenValid()
                        if (tokenValid) {
                            navController.navigate(Routes.HOME)
                        } else {
                            navController.navigate(Routes.LOGIN)
                        }
                    }
                },
                onClickExplorer = { navController.navigate(Routes.EXPLORATE) }
            )
        }

        // Graficos Estadisticos
        composable(Routes.PROGRESS) {
            DefaultScreenExternal("Gráficos Estadísticos", navController, viewModelLangPage,)
        }

        // Practicas
        composable(Routes.PRACTICE) {
            DefaultScreenExternal("Prácticas", navController, viewModelLangPage)
        }


        // Recomendaciones
        composable(Routes.RECOMMENDATIONS) {
            DefaultScreenExternal("Recomendaciones", navController, viewModelLangPage)
        }


        // Tourist Info
        composable(Routes.DEVICE_INFO) {
            TouristInfoScreen(navController)
        }

        // Navegacion Privada
        setupMenuRoutes(
            navGraphBuilder = this,
            navController = navController,
            onLogout = onLogout
        )
    }
}

private suspend fun navigateAfterAuth(
    navController: NavHostController,
    sessionManager: SessionManager,
    popUpRoute: String
) {
    val pending = sessionManager.getPendingRoute()
    if (!pending.isNullOrEmpty()) {
        sessionManager.clearPendingRoute()
        navController.navigate(pending) {
            popUpTo(popUpRoute) { inclusive = true }
        }
    } else {
        navController.navigate(Routes.LAND_PAGE) {
            popUpTo(popUpRoute) { inclusive = true }
        }
    }
}

private fun setupMenuRoutes(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val implementedRoutes = mapOf(
        Routes.HomeScreen.Setup.MODULE to "Módulos",
        Routes.HomeScreen.Setup.PARENT_MODULE to "Módulos Padres",
        Routes.HomeScreen.Setup.ROLE to "Roles",
        Routes.HomeScreen.Setup.USUARIOS to "Usuarios",
        Routes.HomeScreen.Setup.SEPTIONS to "Secciones",
        Routes.HomeScreen.Docs.INBOX to "Bandeja de Entrada",
        Routes.HomeScreen.Docs.TRAMITES to "Trámites",
        Routes.HomeScreen.Seguimiento.INDICADORES to "Indicadores",
        Routes.HomeScreen.Seguimiento.EVALUACIONES to "Evaluaciones",
    )

    implementedRoutes.forEach { (route, title) ->
        navGraphBuilder.composable(route) {

            BaseScreenLayout(
                navController = navController,
                title = title,
                onLogout = onLogout
            ) { paddingValues ->
                when (route) {
                    Routes.HomeScreen.Setup.ROLE -> RoleScreen(
                        navController = navController,
                        paddingValues = paddingValues
                    )
                    Routes.HomeScreen.Setup.MODULE -> ModuleScreen(
                        navController = navController,
                        paddingValues = paddingValues
                    )
                    Routes.HomeScreen.Setup.PARENT_MODULE -> ParentModuleScreen(
                        navController = navController,
                        paddingValues = paddingValues
                    )
                    else -> DefaultScreen(
                        title = title,
                        route = route,
                        navController = navController,
                        onLogout = onLogout,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}