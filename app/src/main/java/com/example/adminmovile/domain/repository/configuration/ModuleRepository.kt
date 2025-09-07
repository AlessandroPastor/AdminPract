package com.example.adminmovile.domain.repository.configuration

import com.example.adminmovile.data.remote.dto.configuracion.ModuleCreateDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleSelectedDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleResponse

interface ModuleRepository {
    suspend fun getModules(page: Int = 0, size: Int = 10, name: String? = null): Result<ModuleResponse>
    suspend fun getModuleById(id: String): Result<ModuleDTO> // UUID representado como String
    suspend fun createModule(module: ModuleCreateDTO): Result<ModuleDTO>
    suspend fun updateModule(id: String, module: ModuleCreateDTO): Result<ModuleDTO> // UUID representado como String
    suspend fun deleteModule(id: String): Result<Unit> // UUID representado como String
    suspend fun getModulesSelected(roleId: String, parentModuleId: String): Result<List<ModuleSelectedDTO>>
}