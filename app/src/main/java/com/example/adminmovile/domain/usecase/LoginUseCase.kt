package com.example.adminmovile.domain.usecase

import com.example.adminmovile.domain.model.User
import com.example.adminmovile.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}