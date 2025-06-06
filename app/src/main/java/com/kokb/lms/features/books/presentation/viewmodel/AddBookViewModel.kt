package com.kokb.lms.features.books.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.features.books.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBookState(
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val genre: String = "",
    val copies: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddBookState())
    val state: StateFlow<AddBookState> = _state.asStateFlow()

    fun onEvent(event: AddBookEvent) {
        when (event) {
            is AddBookEvent.OnIsbnChange -> {
                _state.update { it.copy(isbn = event.value) }
            }
            is AddBookEvent.OnTitleChange -> {
                _state.update { it.copy(title = event.value) }
            }
            is AddBookEvent.OnAuthorChange -> {
                _state.update { it.copy(author = event.value) }
            }
            is AddBookEvent.OnGenreChange -> {
                _state.update { it.copy(genre = event.value) }
            }
            is AddBookEvent.OnCopiesChange -> {
                _state.update { it.copy(copies = event.value) }
            }
            is AddBookEvent.OnAddBook -> {
                addBook()
            }
        }
    }

    private fun addBook() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, success = false) }
            
            addBookUseCase(
                isbn = state.value.isbn,
                title = state.value.title,
                author = state.value.author,
                genre = state.value.genre,
                copies = state.value.copies
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

    fun isFormValid(): Boolean {
        return state.value.isbn.isNotBlank() &&
                state.value.title.isNotBlank() &&
                state.value.author.isNotBlank() &&
                state.value.genre.isNotBlank() &&
                state.value.copies > 0
    }
}

sealed class AddBookEvent {
    data class OnIsbnChange(val value: String) : AddBookEvent()
    data class OnTitleChange(val value: String) : AddBookEvent()
    data class OnAuthorChange(val value: String) : AddBookEvent()
    data class OnGenreChange(val value: String) : AddBookEvent()
    data class OnCopiesChange(val value: Int) : AddBookEvent()
    object OnAddBook : AddBookEvent()
} 