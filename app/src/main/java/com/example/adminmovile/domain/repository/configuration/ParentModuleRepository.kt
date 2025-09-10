package com.example.adminmovile.domain.repository.configuration

import com.example.adminmovile.data.remote.dto.configuracion.ParentModule
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleDetail
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleListResponse

interface ParentModuleRepository {

    suspend fun getParentModules(
        page: Int = 0,
        size: Int = 1,
        name: String? = null
    ): Result<ParentModuleListResponse>

    suspend fun getParentModuleById(id: Int): Result<ParentModule> // 🔹 cambiado a Int

    suspend fun createParentModule(parentModule: ParentModule): Result<ParentModule>

    suspend fun updateParentModule(id: Int, parentModule: ParentModule): Result<ParentModule> // 🔹 cambiado a Int

    suspend fun deleteParentModule(id: Int): Result<Unit> // 🔹 cambiado a Int

    suspend fun getParentModuleList(): Result<List<ParentModule>>

    suspend fun getParentModuleDetailList(): Result<List<ParentModuleDetail>>
}