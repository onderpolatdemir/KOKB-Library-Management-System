package com.kokb.lms.features.librarian.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrarianBookLoanViewModel @Inject constructor(
    private val bookManager: BookManager
) : ViewModel() {

    private val _state = MutableStateFlow(LibrarianBookLoanState())
    val state: StateFlow<LibrarianBookLoanState> = _state.asStateFlow()

    private val _showAvailable = mutableStateOf(true)
    val showAvailable: State<Boolean> = _showAvailable

    init {
        loadBooks()
    }

    private fun loadBooks() {
        viewModelScope.launch {
            _state.value = LibrarianBookLoanState(isLoading = true)
            try {
                val books = bookManager.getBooks()
                _state.value = LibrarianBookLoanState(books = books)
            } catch (e: Exception) {
                _state.value = LibrarianBookLoanState(error = e.message ?: "Failed to load books")
            }
        }
    }

    fun refreshBooks() {
        loadBooks()
    }

    fun toggleAvailabilityFilter(showAvailable: Boolean) {
        _showAvailable.value = showAvailable
    }

    fun getFilteredBooks(): List<Book> {
        return state.value.books.filter { book ->
            if (showAvailable.value) {
                book.status == BookStatus.AVAILABLE
            } else {
                book.status == BookStatus.BORROWED
            }
        }
    }
}

data class LibrarianBookLoanState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
) 