package com.kokb.lms.features.auth.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: String
)

data class UsersResponse(
    @SerializedName("users")
    val users: List<UserDto>,
    @SerializedName("totalCount")
    val totalCount: Int
) 