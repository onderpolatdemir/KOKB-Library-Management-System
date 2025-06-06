package com.kokb.lms.features.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val role: UserRole
)

enum class UserRole {
    ADMIN,
    LIBRARIAN,
    READER
} 