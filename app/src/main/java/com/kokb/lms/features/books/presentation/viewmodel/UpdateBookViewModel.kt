package com.kokb.lms.features.books.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.util.Resource
import com.kokb.lms.features.books.domain.model.BookStatus
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.usecase.UpdateBookUseCase
import com.kokb.lms.features.books.domain.usecase.GetBookByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class UpdateBookState(
    val id: UUID? = null,
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val genre: String = "",
    val copies: Int = 1,
    val status: BookStatus = BookStatus.AVAILABLE,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class UpdateBookViewModel @Inject constructor(
    private val updateBookUseCase: UpdateBookUseCase,
    private val getBookByIdUseCase: GetBookByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UpdateBookState())
    val state: StateFlow<UpdateBookState> = _state.asStateFlow()

    fun setInitialBook(id: UUID, isbn: String, title: String, author: String, genre: String, copies: Int, status: BookStatus) {
        _state.update { 
            it.copy(
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

    fun onEvent(event: UpdateBookEvent) {
        when (event) {
            is UpdateBookEvent.OnIsbnChange -> {
                _state.update { it.copy(isbn = event.value) }
            }
            is UpdateBookEvent.OnTitleChange -> {
                _state.update { it.copy(title = event.value) }
            }
            is UpdateBookEvent.OnAuthorChange -> {
                _state.update { it.copy(author = event.value) }
            }
            is UpdateBookEvent.OnGenreChange -> {
                _state.update { it.copy(genre = event.value) }
            }
            is UpdateBookEvent.OnCopiesChange -> {
                _state.update { it.copy(copies = event.value) }
            }
            is UpdateBookEvent.OnUpdateBook -> {
                updateBook()
            }
        }
    }

    private fun updateBook() {
        val currentState = state.value
        val bookId = currentState.id ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, success = false) }
            
            updateBookUseCase(
                id = bookId,
                isbn = currentState.isbn,
                title = currentState.title,
                author = currentState.author,
                genre = currentState.genre,
                copies = currentState.copies,
                status = currentState.status
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, success = true) }
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    fun getBookById(id: UUID) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getBookByIdUseCase(id).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val book = resource.data
                        if (book != null) {
                            _state.update {
                                it.copy(
                                    id = book.id,
                                    isbn = book.isbn,
                                    title = book.title,
                                    author = book.author,
                                    genre = book.genre,
                                    copies = book.copies,
                                    status = book.status,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun isFormValid(): Boolean {
        return state.value.isbn.isNotBlank() &&
                state.value.title.isNotBlank() &&
                state.value.author.isNotBlank() &&
                state.value.genre.isNotBlank() &&
                state.value.copies > 0
    }
}

sealed class UpdateBookEvent {
    data class OnIsbnChange(val value: String) : UpdateBookEvent()
    data class OnTitleChange(val value: String) : UpdateBookEvent()
    data class OnAuthorChange(val value: String) : UpdateBookEvent()
    data class OnGenreChange(val value: String) : UpdateBookEvent()
    data class OnCopiesChange(val value: Int) : UpdateBookEvent()
    object OnUpdateBook : UpdateBookEvent()
} 