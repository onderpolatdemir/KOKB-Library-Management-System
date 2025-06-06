package com.kokb.lms.features.search.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import com.kokb.lms.features.borrow.domain.model.BorrowStatus
import com.kokb.lms.features.search.domain.model.BookAvailability
import com.kokb.lms.features.search.domain.model.BookCategory
import com.kokb.lms.features.search.domain.model.SearchFilters
import com.kokb.lms.features.borrow.domain.use_case.BorrowBookUseCase
import com.kokb.lms.core.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookManager: BookManager,
    private val borrowBookUseCase: BorrowBookUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    fun search(filters: SearchFilters) {
        viewModelScope.launch {
            _searchState.value = SearchState(isLoading = true)
            Log.d("SearchViewModel", "Starting search with filters: $filters")

            val allBooks = bookManager.getBooks()
            Log.d("SearchViewModel", "Retrieved ${allBooks.size} books from BookManager")

            val filteredBooks = allBooks.filter { book ->
                var matches = true

                // Book name filter
                if (filters.bookName.isNotEmpty()) {
                    matches = matches && book.title.contains(filters.bookName, ignoreCase = true)
                    Log.d("SearchViewModel", "Book '${book.title}' title match: $matches")
                }

                // Author name filter
                if (filters.authorName.isNotEmpty()) {
                    matches = matches && book.author.contains(filters.authorName, ignoreCase = true)
                    Log.d("SearchViewModel", "Book '${book.title}' author match: $matches")
                }

                // ISBN filter
                if (filters.isbn.isNotEmpty()) {
                    matches = matches && book.isbn == filters.isbn
                    Log.d("SearchViewModel", "Book '${book.title}' ISBN match: $matches")
                }

                // Category filter
                if (filters.category != null) {
                    matches = matches && book.genre.equals(filters.category.name, ignoreCase = true)
                    Log.d("SearchViewModel", "Book '${book.title}' category match: $matches")
                }

                // Availability filter
                if (filters.availability != null) {
                    matches = matches && when (filters.availability) {
                        BookAvailability.AVAILABLE -> book.status == BookStatus.AVAILABLE
                        BookAvailability.RESERVED -> book.status == BookStatus.RESERVED
                    }
                    Log.d("SearchViewModel", "Book '${book.title}' availability match: $matches")
                }

                matches
            }

            Log.d("SearchViewModel", "Found ${filteredBooks.size} books after filtering")

            _searchState.value = if (filteredBooks.isEmpty() && filters != SearchFilters()) {
                Log.d("SearchViewModel", "No books found matching criteria")
                SearchState(error = "No books found matching your criteria")
            } else {
                Log.d("SearchViewModel", "Search completed successfully")
                SearchState(books = filteredBooks)
            }
        }
    }

    fun borrowBook(bookId: UUID) {
        viewModelScope.launch {
            val user = authManager.getLoginState()
            Log.d("BorrowDebug", "Current user state: ${user?.id}, ${user?.email}, ${user?.name}")
            
            if (user == null) {
                _searchState.update {
                    it.copy(error = "Please log in to borrow books")
                }
                return@launch
            }
            
            Log.d("BorrowDebug", "Starting borrow process - userId: ${user.id}, bookId: $bookId")
            _searchState.update { it.copy(isLoading = true) }
            borrowBookUseCase(user.id, bookId)
                .onSuccess { transaction ->
                    Log.d("BorrowDebug", "Borrow success - transactionId: ${transaction.id}, status: ${transaction.status}")
                    // Update both the search state and BookManager
                    _searchState.update { state ->
                        val updatedBooks = state.books.map { book ->
                            if (book.id == bookId) {
                                Log.d("BorrowDebug", "Updating book status - bookId: ${book.id}, oldStatus: ${book.status}, newStatus: BORROWED")
                                book.copy(status = BookStatus.BORROWED).also { updatedBook ->
                                    // Update the book in BookManager
                                    bookManager.updateBook(updatedBook)
                                }
                            } else {
                                book
                            }
                        }
                        state.copy(
                            isLoading = false,
                            books = updatedBooks,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("BorrowDebug", "Borrow failed - Error: ${error.message}", error)
                    _searchState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to borrow book"
                        )
                    }
                }
        }
    }
}

data class SearchState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)