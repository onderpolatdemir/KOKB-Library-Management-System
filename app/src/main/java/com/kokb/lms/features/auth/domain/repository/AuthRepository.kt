package com.kokb.lms.features.auth.domain.repository

import com.kokb.lms.features.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, surname: String, email: String, password: String, confirmPassword: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getAllUsers(): Result<List<User>>
    suspend fun updateUserRole(userId: String, role: String): Result<Unit>
} 