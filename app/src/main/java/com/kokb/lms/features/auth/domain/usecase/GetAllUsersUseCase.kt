package com.kokb.lms.features.auth.domain.usecase

import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getAllUsers()
    }
} 