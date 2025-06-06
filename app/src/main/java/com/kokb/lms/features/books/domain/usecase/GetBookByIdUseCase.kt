package com.kokb.lms.features.books.domain.usecase

import com.kokb.lms.core.util.Resource
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class GetBookByIdUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(id: UUID): Flow<Resource<Book>> = flow {
        try {
            emit(Resource.Loading())
            val book = repository.getBookById(id)
            emit(Resource.Success(book))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
} 