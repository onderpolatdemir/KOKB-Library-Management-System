package com.kokb.lms.features.auth.domain.usecase

import com.kokb.lms.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateUserRoleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userId: String, role: String): Result<Unit> {
        return repository.updateUserRole(userId, role)
    }
} 