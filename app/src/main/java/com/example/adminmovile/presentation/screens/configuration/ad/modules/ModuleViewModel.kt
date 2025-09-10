package com.example.adminmovile.presentation.screens.configuration.ad.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adminmovile.data.remote.dto.configuracion.ModuleCreateDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleState
import com.example.adminmovile.domain.repository.configuration.ModuleRepository
import com.example.adminmovile.domain.repository.configuration.ParentModuleRepository
import com.example.adminmovile.presentation.components.NotificationState
import com.example.adminmovile.presentation.components.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ModuleViewModel(
    private val repository: ModuleRepository,
    private val modulerepository: ParentModuleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ModuleState())
    val state = _state.asStateFlow()

    init {
        loadModules()
        loadParentModules()
    }

    fun loadModules(page: Int = 0, searchQuery: String? = null) {
        viewModelScope.launch {
            println("📥 [LOAD MODULES] Página=$page | Búsqueda=${searchQuery ?: "ninguna"}")
            _state.value = _state.value.copy(isLoading = true)
            try {
                repository.getModules(page = page, size = 20, name = searchQuery) // ← Aquí establecemos el límite
                    .onSuccess { response ->
                        println("✅ [LOAD MODULES] Se cargaron ${response.content.size} módulos.")
                        _state.value = _state.value.copy(
                            items = response.content,
                            currentPage = response.currentPage,
                            totalPages = response.totalPages,
                            isLoading = false,
                            error = null
                        )
                    }
                    .onFailure { error ->
                        println("❌ [LOAD MODULES] Error: ${error.message}")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = error.message,
                            notification = NotificationState(
                                message = error.message ?: "Error al cargar módulos",
                                type = NotificationType.ERROR,
                                isVisible = true
                            )
                        )
                    }
            } catch (e: Exception) {
                println("❌ [LOAD MODULES] Excepción inesperada: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message,
                    notification = NotificationState(
                        message = e.message ?: "Error inesperado",
                        type = NotificationType.ERROR,
                        isVisible = true
                    )
                )
            }
        }
    }

    fun loadParentModules(page: Int = 0, searchQuery: String? = null) {
        viewModelScope.launch {
            println("📥 [LOAD PARENT MODULES] Página=$page | Búsqueda=${searchQuery ?: "ninguna"}")
            _state.value = _state.value.copy(isLoading = true)
            modulerepository.getParentModules(page = page, size = 20, name = searchQuery)
                .onSuccess { response ->
                    println("✅ [LOAD PARENT MODULES] Se cargaron ${response.content.size} parentModules.")
                    _state.value = _state.value.copy(
                        parentModules = response.content,
                        currentPage = response.currentPage,
                        totalPages = response.totalPages,
                        totalElements = response.totalElements,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { error ->
                    println("❌ [LOAD PARENT MODULES] Error: ${error.message}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message,
                        notification = NotificationState(
                            message = error.message ?: "Error al cargar módulos",
                            type = NotificationType.ERROR,
                            isVisible = true
                        )
                    )
                }
        }
    }

    fun createModule(module: ModuleDTO) {
        viewModelScope.launch {
            println("📤 [CREATE MODULE] Intentando crear módulo: ${module.title}")
            _state.value = _state.value.copy(isLoading = true)

            val dto = ModuleCreateDTO(
                title = module.title,
                subtitle = module.subtitle,
                type = module.type,
                icon = module.icon,
                status = module.status,
                selected = true,
                moduleOrder = module.moduleOrder,
                link = module.link,
                parentModuleId = module.parentModule?.id
            )

            println("📦 [CREATE MODULE] DTO enviado: $dto")

            repository.createModule(dto)
                .onSuccess {
                    println("✅ [CREATE MODULE] Módulo creado correctamente")
                    loadModules()
                    _state.value = _state.value.copy(
                        isDialogOpen = false,
                        selectedItem = null,
                        notification = NotificationState(
                            message = "Módulo creado exitosamente",
                            type = NotificationType.SUCCESS,
                            isVisible = true
                        )
                    )
                }
                .onFailure { error ->
                    println("❌ [CREATE MODULE] Error: ${error.message}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        notification = NotificationState(
                            message = error.message ?: "Error al crear módulo",
                            type = NotificationType.ERROR,
                            isVisible = true
                        )
                    )
                }
        }
    }

    fun updateModule(module: ModuleDTO) {
        if (module.id == null) return

        viewModelScope.launch {
            println("✏️ [UPDATE MODULE] Editando módulo con ID=${module.id}")
            _state.value = _state.value.copy(isLoading = true)

            val dto = ModuleCreateDTO(
                id = module.id,
                title = module.title,
                subtitle = module.subtitle,
                type = module.type,
                icon = module.icon,
                status = module.status,
                selected = true,
                moduleOrder = module.moduleOrder,
                link = module.link,
                parentModuleId = module.parentModule?.id
            )

            println("📦 [UPDATE MODULE] DTO enviado: $dto")

            repository.updateModule(module.id, dto)
                .onSuccess {
                    println("✅ [UPDATE MODULE] Módulo actualizado.")
                    loadModules()
                    _state.value = _state.value.copy(
                        isDialogOpen = false,
                        selectedItem = null,
                        notification = NotificationState(
                            message = "Módulo actualizado exitosamente",
                            type = NotificationType.SUCCESS,
                            isVisible = true
                        )
                    )
                }
                .onFailure { error ->
                    println("❌ [UPDATE MODULE] Error: ${error.message}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        notification = NotificationState(
                            message = error.message ?: "Error al actualizar módulo",
                            type = NotificationType.ERROR,
                            isVisible = true
                        )
                    )
                }
        }
    }

    fun deleteModule(id: Int) {
        viewModelScope.launch {
            println("🗑️ [DELETE MODULE] Eliminando módulo con ID=$id")
            _state.value = _state.value.copy(isLoading = true)

            repository.deleteModule(id)
                .onSuccess {
                    println("✅ [DELETE MODULE] Módulo eliminado.")
                    loadModules()
                    _state.value = _state.value.copy(
                        notification = NotificationState(
                            message = "Módulo eliminado exitosamente",
                            type = NotificationType.SUCCESS,
                            isVisible = true
                        )
                    )
                }
                .onFailure { error ->
                    println("❌ [DELETE MODULE] Error: ${error.message}")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        notification = NotificationState(
                            message = error.message ?: "Error al eliminar módulo",
                            type = NotificationType.ERROR,
                            isVisible = true
                        )
                    )
                }
        }
    }

    fun setSelectedModule(module: ModuleDTO?) {
        if (module?.id == null) {
            println("🆕 [SET MODULE] Módulo nuevo. Mostrando diálogo vacío.")
            _state.update { it.copy(selectedItem = module, isDialogOpen = true) }
            return
        }

        if (module != null) {
            println("🔍 [SET MODULE] Buscando módulo ID=${module.id}")
        }

        viewModelScope.launch {
            if (module != null) {
                repository.getModuleById(module.id)
                    .onSuccess { updatedModule ->
                        println("✅ [SET MODULE] Módulo cargado correctamente.")
                        _state.update {
                            it.copy(
                                selectedItem = updatedModule,
                                isDialogOpen = true
                            )
                        }
                    }
                    .onFailure { error ->
                        println("❌ [SET MODULE] Error al obtener módulo: ${error.message}")
                        _state.update {
                            it.copy(
                                isDialogOpen = true,
                                notification = NotificationState(
                                    message = "Error al cargar módulo: ${error.message}",
                                    type = NotificationType.ERROR,
                                    isVisible = true
                                )
                            )
                        }
                    }
            }
        }
    }

    fun closeDialog() {
        _state.value = _state.value.copy(
            isDialogOpen = false,
            selectedItem = null
        )
    }

    fun nextPage() {
        if (_state.value.currentPage + 1 < _state.value.totalPages) {
            loadModules(_state.value.currentPage + 1)
        }
    }

    fun previousPage() {
        if (_state.value.currentPage > 0) {
            loadModules(_state.value.currentPage - 1)
        }
    }
}
