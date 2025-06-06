package com.kokb.lms.features.mybooks.domain.use_case

import com.kokb.lms.features.mybooks.domain.repository.MyBooksRepository
import javax.inject.Inject

class ReturnBookUseCase @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend operator fun invoke(transactionId: String): Result<Unit> {
        return repository.returnBook(transactionId)
    }
} 