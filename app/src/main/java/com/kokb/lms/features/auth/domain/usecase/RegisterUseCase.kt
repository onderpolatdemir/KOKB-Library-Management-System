package com.kokb.lms.features.auth.domain.usecase

import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        surname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        return repository.register(name, surname, email, password, confirmPassword)
    }
} 