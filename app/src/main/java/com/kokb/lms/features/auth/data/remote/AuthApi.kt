package com.kokb.lms.features.auth.data.remote

import com.kokb.lms.features.auth.data.model.AuthResponse
import com.kokb.lms.features.auth.data.model.LoginRequest
import com.kokb.lms.features.auth.data.model.RegisterRequest
import com.kokb.lms.features.auth.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @POST("api/Account/authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): AuthResponse

    @POST("api/Account/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<String>

    @GET("api/v1/User/all")
    suspend fun getAllUsers(): List<UserDto>

    @PUT("api/v1/User/{userId}/role")
    suspend fun updateUserRole(
        @Path("userId") userId: String,
        @Body role: String
    ): Response<Unit>

// suspend fun register(@Body registerRequest: RegisterRequest): AuthResponse
//    @POST("api/Account/register")
//    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}