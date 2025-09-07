package com.example.adminmovile.data.remote.api.base

import com.example.adminmovile.data.local.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.request.*

open class BaseApiService(
    protected val client: HttpClient,
    protected open val sessionManager: SessionManager
) {
    private var cachedToken: String? = null

    // No suspend, solo añade el header con token ya cargado
    protected suspend fun HttpRequestBuilder.addAuthHeader() {
        val storedToken = sessionManager.getAuthToken()
        if (storedToken != cachedToken) {
            cachedToken = storedToken
        }
        cachedToken?.let {
            header("Authorization", "Bearer $it")
        } ?: throw IllegalStateException("No auth token available. Please login first.")
    }

    fun updateAuthToken(token: String) {  // Cambia el nombre
        this.cachedToken  = token
    }
    fun clearAuthToken() {
        this.cachedToken  = null
    }


    open suspend fun loadAuthTokenFromStorage() {
        cachedToken  = sessionManager.getAuthToken() ?: sessionManager.getUser()?.token
    }
}
