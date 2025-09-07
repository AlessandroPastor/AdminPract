package com.example.adminmovile.di

import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.data.local.SettingsFactory
import com.example.adminmovile.data.remote.api.ApiConstants
import com.example.adminmovile.data.remote.api.base.AuthApiService
import com.example.adminmovile.data.remote.api.base.MenuApiService
import com.example.adminmovile.data.remote.api.configuracion.ModuleApiService
import com.example.adminmovile.data.remote.api.configuracion.ParentModuleApiService
import com.example.adminmovile.data.remote.api.configuracion.RoleApiService
import com.example.adminmovile.data.remote.api.configuracion.ServiceApiService
import com.example.adminmovile.data.repository.AuthRepositoryImpl
import com.example.adminmovile.data.repository.configuration.ModuleRepositoryImpl
import com.example.adminmovile.data.repository.configuration.ParentModuleRepositoryImpl
import com.example.adminmovile.data.repository.configuration.RoleRepositoryImpl
import com.example.adminmovile.domain.repository.AuthRepository
import com.example.adminmovile.domain.repository.configuration.ModuleRepository
import com.example.adminmovile.domain.repository.configuration.ParentModuleRepository
import com.example.adminmovile.domain.repository.configuration.RoleRepository
import com.example.adminmovile.domain.usecase.LoginUseCase
import com.example.adminmovile.domain.usecase.RegisterUseCase
import com.example.adminmovile.presentation.screens.configuration.ad.modules.ModuleViewModel
import com.example.adminmovile.presentation.screens.configuration.ad.modulos_padres.ParentModuleViewModel
import com.example.adminmovile.presentation.screens.configuration.ad.role.RoleViewModel
import com.example.adminmovile.presentation.screens.configuration.ad.service.ServiceViewModel
import com.example.adminmovile.presentation.screens.dashboard.HomeViewModel
import com.example.adminmovile.presentation.screens.land_page.LangPageViewModel
import com.example.adminmovile.presentation.screens.login.LoginViewModel
import com.example.adminmovile.presentation.screens.login.ProfileViewModel
import com.example.adminmovile.presentation.screens.login.RegisterViewModel
import com.example.adminmovile.presentation.theme.ThemeViewModel
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    // =====================================
    // Settings & SessionManager con DataStore
    // =====================================

    single { SettingsFactory(get()).createSettings() } // DataStore<Preferences>
    single { SessionManager(get()) }

    // =====================================
    // HTTP Client
    // =====================================
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            defaultRequest {
                url(ApiConstants.BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }
    }

    // =====================================
    // API Services
    // =====================================
    single { AuthApiService(get(), get()) }
    single { MenuApiService(get(),get()) }
    single { RoleApiService(get(),get()) }
    single { ModuleApiService(get(),get()) }
    single { ParentModuleApiService(get(),get()) }
    single { ServiceApiService(get(),get()) }


    // =====================================
    // Repositories
    // =====================================
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<RoleRepository> { RoleRepositoryImpl(get()) }
    single<ModuleRepository> { ModuleRepositoryImpl(get()) }
    single<ParentModuleRepository> { ParentModuleRepositoryImpl(get()) }

    // =====================================
    // UseCases
    // =====================================
    single { LoginUseCase(get()) }
    single { RegisterUseCase(get(), get()) }


    // =====================================
    // ViewModels
    // =====================================
    viewModel { ThemeViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RoleViewModel(get(), get(), get()) }
    viewModel { ModuleViewModel(get(), get()) }
    viewModel { ParentModuleViewModel(get()) }
    viewModel { ServiceViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LangPageViewModel(get()) }
}