package com.example.adminmovile.presentation.screens.land_page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adminmovile.data.remote.api.configuracion.ServiceApiService
import com.example.adminmovile.data.remote.dto.configuracion.ServiceState
import com.example.adminmovile.presentation.components.NotificationState
import com.example.adminmovile.presentation.components.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LangPageViewModel (
    private val apiserviceService : ServiceApiService,
) : ViewModel() {

    private val _stateService = MutableStateFlow(ServiceState())
    val stateService: StateFlow<ServiceState> = _stateService.asStateFlow()

    private val _categories = mutableStateOf<List<String>>(emptyList())
    val services: State<List<String>> get() = _categories

    init {
        loadService()
    }

    fun loadService(page: Int? = 0, size: Int? = 0 ,search: String? = null, category: String? = null) {
        viewModelScope.launch {
            _stateService.value = _stateService.value.copy(isLoading = true)
            try {
                val response = apiserviceService.getService(page, size, search, category)
                _stateService.value = _stateService.value.copy(
                    items = response.content,
                    currentPage = response.currentPage,
                    totalPages = response.totalPages,
                    totalElements = response.totalElements,
                    isLoading = false,
                    error = null
                )
                extractCategories()
            } catch (e: Exception) {
                println("❌ [API Service] Error al cargar servicios: ${e.message}")
                _stateService.value = _stateService.value.copy(
                    isLoading = false,
                    error = e.message,
                    notification = NotificationState(
                        message = e.message ?: "Error al cargar servicios",
                        type = NotificationType.ERROR,
                        isVisible = true
                    )
                )
            }
        }
    }

    fun extractCategories() {
        val allServices = _stateService.value.items
        val uniqueCategories = allServices.map { it.category }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        _categories.value = listOf("Todos") + uniqueCategories
    }


    // PARA EL SCROLL
    enum class ScrollDirection {
        UP, DOWN, NONE
    }

}