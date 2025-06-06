package com.kokb.lms.features.books.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.core.util.Resource
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.use_case.GetAllBooksUseCase
import com.kokb.lms.features.books.domain.use_case.GetBookByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getBookByIdUseCase: GetBookByIdUseCase,
    private val bookManager: BookManager
) : ViewModel() {

    private val _booksState = MutableStateFlow(BooksState())
    val booksState: StateFlow<BooksState> = _booksState

    private val _selectedBookState = MutableStateFlow(SelectedBookState())
    val selectedBookState: StateFlow<SelectedBookState> = _selectedBookState

    init {
        loadStoredBooks()
    }

    private fun loadStoredBooks() {
        viewModelScope.launch {
            val storedBooks = bookManager.getBooks()
            if (storedBooks.isNotEmpty()) {
                _booksState.value = BooksState(books = storedBooks)
            }
            // Always fetch fresh books from API
            getAllBooks()
        }
    }

    fun getAllBooks() {
        getAllBooksUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val books = result.data ?: emptyList()
                    _booksState.value = BooksState(books = books)
                    // Store books in BookManager
                    bookManager.saveBooks(books)
                }
                is Resource.Error -> {
                    _booksState.value = BooksState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _booksState.value = BooksState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getBookById(id: UUID) {
        getBookByIdUseCase(id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _selectedBookState.value = SelectedBookState(book = result.data)
                }
                is Resource.Error -> {
                    _selectedBookState.value = SelectedBookState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _selectedBookState.value = SelectedBookState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun clearStoredBooks() {
        viewModelScope.launch {
            bookManager.clearBooks()
            _booksState.value = BooksState()
        }
    }
}

data class BooksState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String = ""
)

data class SelectedBookState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String = ""
) 