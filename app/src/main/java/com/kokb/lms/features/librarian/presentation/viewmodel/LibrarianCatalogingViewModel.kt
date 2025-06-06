package com.kokb.lms.features.librarian.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibrarianCatalogingState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LibrarianCatalogingViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LibrarianCatalogingState())
    val state: StateFlow<LibrarianCatalogingState> = _state.asStateFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val books = bookRepository.getAllBooks()
                _state.update { it.copy(books = books, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "An error occurred", isLoading = false) }
            }
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val allBooks = bookRepository.getAllBooks()
                val filteredBooks = if (query.isBlank()) {
                    allBooks
                } else {
                    allBooks.filter { book ->
                        book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true) ||
                        book.isbn.contains(query, ignoreCase = true)
                    }
                }
                _state.update { it.copy(books = filteredBooks, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "An error occurred", isLoading = false) }
            }
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val isSuccess = bookRepository.deleteBook(java.util.UUID.fromString(bookId))
                if (isSuccess) {
                    // Only refresh if deletion was successful (204 response)
                    loadBooks()
                } else {
                    _state.update { it.copy(error = "Failed to delete book", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to delete book", isLoading = false) }
            }
        }
    }
} 