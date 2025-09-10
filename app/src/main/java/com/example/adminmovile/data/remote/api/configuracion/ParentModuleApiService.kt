package com.example.adminmovile.data.remote.api.configuracion

import com.example.adminmovile.data.local.SessionManager
import com.example.adminmovile.data.remote.api.ApiConstants
import com.example.adminmovile.data.remote.api.base.BaseApiService
import com.example.adminmovile.data.remote.dto.configuracion.ParentModule
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleDetail
import com.example.adminmovile.data.remote.dto.configuracion.ParentModuleListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class ParentModuleApiService(
    client: HttpClient,
    sessionManager: SessionManager
) : BaseApiService(client, sessionManager) {

    suspend fun getParentModules(page: Int = 0, size: Int = 20, name: String? = null): ParentModuleListResponse {
        return client.get(ApiConstants.Configuration.GET_PARENT_MODULE) {
            parameter("page", page)
            parameter("size", size)
            name?.let { parameter("name", it) }
            addAuthHeader()
        }.body()
    }

    // ✅ Cambiar String → Int
    suspend fun getParentModuleById(id: Int): ParentModule {
        return client.get(ApiConstants.Configuration.GET_PARENT_MODULE_BY_ID.replace("{id}", id.toString())) {
            addAuthHeader()
        }.body()
    }

    suspend fun createParentModule(parentModule: ParentModule): ParentModule {
        return client.post(ApiConstants.Configuration.CREATE_PARENT_MODULE) {
            addAuthHeader()
            contentType(ContentType.Application.Json)
            setBody(parentModule)
        }.body()
    }

    // ✅ Cambiar String → Int
    suspend fun updateParentModule(id: Int, parentModule: ParentModule): ParentModule {
        return client.put(ApiConstants.Configuration.UPDATE_PARENT_MODULE.replace("{id}", id.toString())) {
            addAuthHeader()
            contentType(ContentType.Application.Json)
            setBody(parentModule)
        }.body()
    }

    // ✅ Cambiar String → Int
    suspend fun deleteParentModule(id: Int) {
        client.delete(ApiConstants.Configuration.DELETE_PARENT_MODULE.replace("{id}", id.toString())) {
            addAuthHeader()
        }
    }

    suspend fun getParentModuleList(): List<ParentModule> {
        return client.get(ApiConstants.Configuration.GET_PARENT_MODULE_LIST) {
            addAuthHeader()
        }.body()
    }

    suspend fun getParentModuleDetailList(): List<ParentModuleDetail> {
        return client.get(ApiConstants.Configuration.GET_PARENT_MODULE_DETAIL_LIST) {
            addAuthHeader()
        }.body()
    }
}

