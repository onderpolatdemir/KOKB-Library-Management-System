package com.kokb.lms.features.books.domain.usecase

import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    suspend operator fun invoke(
        isbn: String,
        title: String,
        author: String,
        genre: String,
        copies: Int
    ): Result<Book> {
        return repository.addBook(
            isbn = isbn,
            title = title,
            author = author,
            genre = genre,
            copies = copies
        )
    }
} 