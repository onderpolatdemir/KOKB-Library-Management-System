package com.kokb.lms.features.auth.data.model

import androidx.compose.ui.semantics.Role
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("ConfirmPassword")
    val confirmPassword: String
)


data class AuthResponse(
    val id: String,
    val userName: String,
    val email: String,
    val roles: List<String>,
    val isVerified: Boolean,
    @SerializedName("jwToken")
    val jwToken: String?,
)