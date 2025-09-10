package com.example.adminmovile.data.repository.configuration

import com.example.adminmovile.data.remote.api.configuracion.ParentModuleApiService
import com.example.adminmovile.data.remote.dto.configuracion.ParentModule
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleDetail
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleListResponse
import com.example.adminmovile.domain.repository.configuration.ParentModuleRepository

class ParentModuleRepositoryImpl(
    private val apiService: ParentModuleApiService
) : ParentModuleRepository {

    override suspend fun getParentModules(page: Int, size: Int, name: String?): Result<ParentModuleListResponse> {
        return try {
            Result.success(apiService.getParentModules(page, size, name))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ id cambiado a Int
    override suspend fun getParentModuleById(id: Int): Result<ParentModule> {
        return try {
            Result.success(apiService.getParentModuleById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createParentModule(parentModule: ParentModule): Result<ParentModule> {
        return try {
            Result.success(apiService.createParentModule(parentModule))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ id cambiado a Int
    override suspend fun updateParentModule(id: Int, parentModule: ParentModule): Result<ParentModule> {
        return try {
            Result.success(apiService.updateParentModule(id, parentModule))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ✅ id cambiado a Int
    override suspend fun deleteParentModule(id: Int): Result<Unit> {
        return try {
            apiService.deleteParentModule(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getParentModuleList(): Result<List<ParentModule>> {
        return try {
            Result.success(apiService.getParentModuleList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getParentModuleDetailList(): Result<List<ParentModuleDetail>> {
        return try {
            Result.success(apiService.getParentModuleDetailList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
