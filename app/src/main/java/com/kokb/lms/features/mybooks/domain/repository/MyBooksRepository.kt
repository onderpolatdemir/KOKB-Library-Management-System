package com.kokb.lms.features.mybooks.domain.repository

import com.kokb.lms.features.mybooks.domain.model.UserBorrow
 
interface MyBooksRepository {
    suspend fun getUserBorrows(userId: String): Result<List<UserBorrow>>
    suspend fun returnBook(transactionId: String): Result<Unit>
} 