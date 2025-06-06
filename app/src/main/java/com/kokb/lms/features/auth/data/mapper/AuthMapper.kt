package com.kokb.lms.features.auth.data.mapper

import com.kokb.lms.features.auth.data.model.AuthResponse
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.model.UserRole

fun AuthResponse.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = userName.split("@").first(),
        surname = "",
        role = when (roles.firstOrNull()?.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "LIBRARIAN" -> UserRole.LIBRARIAN
            else -> UserRole.READER
        }
    )
} 