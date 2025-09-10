package com.example.adminmovile.data.remote.dto.configuracion

import com.example.adminmovile.presentation.components.NotificationState
import kotlinx.serialization.Serializable


@Serializable
data class ModuleResponse(
    val content: List<ModuleDTO>,
    val currentPage: Int,
    val totalPages: Int,
    val totalElements: Int
)


@Serializable
data class ModuleDTO(
    val id: Int? = null,
    val title: String = "",
    val subtitle: String = "",
    val type: String = "",
    val code: String? = null,
    val icon: String? = null,
    val status: Int? = null ,
    val moduleOrder: Int = 0,
    val link: String = "",
    val parentModule: ParentModule? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
data class ModuleCreateDTO(
    val id: Int? = null,
    val title: String = "",
    val subtitle: String = "",
    val type: String = "",
    val icon: String? = null,
    val status: Int? = null ,
    val selected: Boolean = true,
    val moduleOrder: Int = 0,
    val link: String = "",
    val parentModuleId: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)

@Serializable
data class ModuleSelectedDTO(
    val id: Int? = null,
    val title: String,
    val subtitle: String? = null,
    val type: String? = null,
    val icon: String? = null,
    val status: Int? = null ,
    val moduleOrder: Int? = null,
    val link: String,
    val parentModuleId: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
    val selected: Boolean? = null
)



data class ModuleState(
    val items: List<ModuleDTO> = emptyList(),
    val parentModules: List<ParentModule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val selectedItem: ModuleDTO? = null,
    val isDialogOpen: Boolean = false,
    val notification: NotificationState = NotificationState()
)

// 📌 Conversión segura de ModuleCreateDTO a ModuleDTO
fun ModuleCreateDTO.toModuleDTO(): ModuleDTO {
    return ModuleDTO(
        id = null,
        title = this.title,
        subtitle = this.subtitle.takeIf { it.isNotBlank() } ?: "",
        type = this.type.takeIf { it.isNotBlank() } ?: "",
        icon = this.icon?.takeIf { it.isNotBlank() } ?: "",
        status = this.status, // ahora es Int?
        moduleOrder = this.moduleOrder,
        link = this.link,
        parentModule = this.parentModuleId?.let { ParentModule(it) }
    )
}