package com.kokb.lms.features.mybooks.domain.use_case

import com.kokb.lms.features.mybooks.domain.model.UserBorrow
import com.kokb.lms.features.mybooks.domain.repository.MyBooksRepository
import javax.inject.Inject

class GetUserBorrowsUseCase @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend operator fun invoke(userId: String): Result<List<UserBorrow>> {
        return repository.getUserBorrows(userId)
    }
} 