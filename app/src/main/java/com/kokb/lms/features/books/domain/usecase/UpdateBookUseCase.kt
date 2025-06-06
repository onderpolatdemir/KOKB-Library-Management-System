package com.kokb.lms.features.books.domain.usecase

import com.kokb.lms.features.books.domain.model.BookStatus
import com.kokb.lms.features.books.domain.repository.BookRepository
import java.util.UUID
import javax.inject.Inject

class UpdateBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        id: UUID,
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int,
        status: BookStatus
    ): Result<Unit> {
        return repository.updateBook(
            id = id,
            isbn = isbn,
            title = title,
            author = author,
            genre = genre,
            copies = copies,
            status = status
        )
    }
} 