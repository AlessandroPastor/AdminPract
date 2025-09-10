package com.example.adminmovile.data.remote.api.configuracion

import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.data.remote.api.ApiConstants
import com.example.adminmovile.data.remote.api.base.BaseApiService
import com.example.adminmovile.data.remote.dto.configuracion.ModuleCreateDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleDTO
import com.example.adminmovile.data.remote.dto.configuracion.ModuleResponse
import com.example.adminmovile.data.remote.dto.configuracion.ModuleSelectedDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class ModuleApiService(
    client: HttpClient,
    sessionManager: SessionManager
) : BaseApiService(client, sessionManager) {

    suspend fun getModules(page: Int = 0, size: Int = 20, name: String?): ModuleResponse {
        return client.get(ApiConstants.Configuration.MODULES) {
            parameter("page", page)
            parameter("size", size)
            name?.let { parameter("name", it) }
            addAuthHeader()
        }.body()
    }

    suspend fun getModuleById(id: Int): ModuleDTO {
        return client.get(ApiConstants.Configuration.MODULE_BY_ID.replace("{id}", id.toString())) {
            addAuthHeader()
        }.body()
    }

    suspend fun createModule(module: ModuleCreateDTO): ModuleDTO {
        return client.post(ApiConstants.Configuration.MODULES) {
            addAuthHeader()
            contentType(ContentType.Application.Json)
            setBody(module)
        }.body()
    }

    suspend fun updateModule(id: Int, module: ModuleCreateDTO): ModuleDTO {
        return client.put(ApiConstants.Configuration.MODULE_BY_ID.replace("{id}", id.toString())) {
            addAuthHeader()
            contentType(ContentType.Application.Json)
            setBody(module)
        }.body()
    }

    suspend fun deleteModule(id: Int) {
        client.delete(ApiConstants.Configuration.MODULE_BY_ID.replace("{id}", id.toString())) {
            addAuthHeader()
        }
    }

    suspend fun getModulesSelected(roleId: Int, parentModuleId: Int): List<ModuleSelectedDTO> {
        val url = ApiConstants.Configuration.MODULE_SELECTED
            .replace("{roleId}", roleId.toString())
            .replace("{parentModuleId}", parentModuleId.toString())
        return client.get(url) {
            addAuthHeader()
        }.body()
    }
}
