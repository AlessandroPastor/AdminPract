package com.example.adminmovile.domain.repository

import com.example.adminmovile.data.remote.dto.MenuItem
import com.example.adminmovile.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getUserDetails(): Result<User>
    suspend fun getMenuItems(): Result<List<MenuItem>>
    suspend fun loadAuthToken()
    suspend fun logout()
}