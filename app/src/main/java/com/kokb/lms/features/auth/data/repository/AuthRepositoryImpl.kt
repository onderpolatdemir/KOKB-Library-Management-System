package com.kokb.lms.features.auth.data.repository

import android.util.Log
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.features.auth.data.remote.AuthApi
import com.kokb.lms.features.auth.data.mapper.toDomain
import com.kokb.lms.features.auth.data.model.LoginRequest
import com.kokb.lms.features.auth.data.model.RegisterRequest
import com.kokb.lms.features.auth.data.model.UserDto
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.model.UserRole
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: AuthApi,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            val user = response.toDomain()
            Log.d("AuthRepository", "User after login: $user")
            // Save user to AuthManager
            authManager.saveLoginState(user)
            Result.success(user)
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> Result.failure(Exception("Login request was cancelled"))
                is SocketTimeoutException -> Result.failure(Exception("Connection timed out. Please check your internet connection."))
                is UnknownHostException -> Result.failure(Exception("Could not connect to the server. Please check your internet connection."))
                is IOException -> Result.failure(Exception("Network error occurred. Please try again."))
                else -> Result.failure(e)
            }
        }
    }

    override suspend fun register(name: String, surname: String, email: String, password: String, confirmPassword: String): Result<User> {
        return try {
            val request = RegisterRequest(
                firstName = name,
                lastName = surname,
                userName = email,
                email = email,
                password = password,
                confirmPassword = confirmPassword
            )
            val response = apiService.register(request)
            val verificationLink = response.body()
            // Log the response for debugging
            Log.d("AuthRepository", "Registration response code: ${response.code()}")
            Log.d("AuthRepository", "Registration response body: ${response.body()}")
            Log.d("AuthRepository", "Verification link: $verificationLink")
            if (response.isSuccessful) {
                // Create a temporary user object for success state
                val user = User(
                    id = "",
                    email = email,
                    name = name,
                    surname = surname,
                    role = UserRole.READER
                )
                // Save user to AuthManager after successful registration
                authManager.saveLoginState(user)
                Result.success(user)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Registration failed with error: $errorBody")
                Result.failure(Exception(errorBody ?: "Registration failed"))
            }
        } catch (e: Exception) {
            // If we get a 200 OK response but JSON parsing fails, consider it a success
            if (e.message?.contains("JSON document was not fully consumed") == true) {
                Log.d("AuthRepository", "Registration successful but JSON parsing failed - this is expected")
                val user = User(
                    id = "",
                    email = email,
                    name = name,
                    surname = surname,
                    role = UserRole.READER
                )
                // Save user to AuthManager even when JSON parsing fails but response is 200
                authManager.saveLoginState(user)
                Result.success(user)
            } else {
                when (e) {
                    is CancellationException -> Result.failure(Exception("Registration request was cancelled"))
                    is SocketTimeoutException -> Result.failure(Exception("Connection timed out. Please check your internet connection."))
                    is UnknownHostException -> Result.failure(Exception("Could not connect to the server. Please check your internet connection."))
                    is IOException -> Result.failure(Exception("Network error occurred. Please try again."))
                    else -> Result.failure(e)
                }
            }
        }
    }

    override suspend fun logout() = withContext(Dispatchers.Main) {
        authManager.clearLoginState()
    }

    override fun getCurrentUser(): Flow<User?> = flow {
        emit(authManager.getLoginState())
    }

    override suspend fun isUserLoggedIn(): Boolean = authManager.isLoggedIn()

    override suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> Result.failure(Exception("Get users request was cancelled"))
                is SocketTimeoutException -> Result.failure(Exception("Connection timed out. Please check your internet connection."))
                is UnknownHostException -> Result.failure(Exception("Could not connect to the server. Please check your internet connection."))
                is IOException -> Result.failure(Exception("Network error occurred. Please try again."))
                else -> Result.failure(e)
            }
        }
    }

    override suspend fun updateUserRole(userId: String, role: String): Result<Unit> {
        return try {
            val response = apiService.updateUserRole(userId, role)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update user role"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Extension function to convert UserDto to User domain model
fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name,
        surname = "", // Since the API doesn't provide surname
        role = when (role) {
            "Reader" -> UserRole.READER
            "Admin" -> UserRole.ADMIN
            "Librarian" -> UserRole.LIBRARIAN
            else -> UserRole.READER // Default to READER if unknown role
        }
    )
} 