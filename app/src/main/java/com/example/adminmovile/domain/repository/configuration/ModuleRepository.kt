package com.example.adminmovile.domain.repository.configuration

import com.example.adminmovile.data.remote.dto.configuracion.ModuleCreateDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleSelectedDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleResponse

interface ModuleRepository {
    suspend fun getModules(
        page: Int = 0,
        size: Int = 10,
        name: String? = null
    ): Result<ModuleResponse>

    suspend fun getModuleById(id: Int): Result<ModuleDTO>

    suspend fun createModule(module: ModuleCreateDTO): Result<ModuleDTO>

    suspend fun updateModule(id: Int, module: ModuleCreateDTO): Result<ModuleDTO>

    suspend fun deleteModule(id: Int): Result<Unit>

    suspend fun getModulesSelected(
        roleId: Int,
        parentModuleId: Int
    ): Result<List<ModuleSelectedDTO>>
}
