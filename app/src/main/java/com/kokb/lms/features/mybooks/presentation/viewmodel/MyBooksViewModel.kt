package com.kokb.lms.features.mybooks.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.features.mybooks.domain.model.UserBorrow
import com.kokb.lms.features.mybooks.domain.use_case.GetUserBorrowsUseCase
import com.kokb.lms.features.mybooks.domain.use_case.ReturnBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val getUserBorrowsUseCase: GetUserBorrowsUseCase,
    private val returnBookUseCase: ReturnBookUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _state = MutableStateFlow(MyBooksState())
    val state: StateFlow<MyBooksState> = _state.asStateFlow()

    private val _showHistory = mutableStateOf(false)
    val showHistory = _showHistory

    fun loadUserBorrows() {
        val user = authManager.getLoginState()
        Log.d("MyBooksDebug", "Loading borrows for user: ${user?.id}")
        user?.id?.let { userId ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                getUserBorrowsUseCase(userId)
                    .onSuccess { borrows ->
                        Log.d("MyBooksDebug", "Received ${borrows.size} borrows from API")
                        borrows.forEach { borrow ->
                            Log.d("MyBooksDebug", "Borrow: id=${borrow.id}, bookId=${borrow.bookId}, status=${borrow.status}, book=${borrow.book?.title}")
                        }
                        _state.update {
                            it.copy(
                                isLoading = false,
                                borrows = borrows,
                                error = null
                            )
                        }
                    }
                    .onFailure { error ->
                        Log.e("MyBooksDebug", "Error loading borrows", error)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "Unknown error occurred"
                            )
                        }
                    }
            }
        }
    }

    fun returnBook(transactionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            returnBookUseCase(transactionId)
                .onSuccess {
                    // Reload the borrows list after successful return
                    loadUserBorrows()
                }
                .onFailure { error ->
                    Log.e("MyBooksDebug", "Error returning book", error)
                    val errorMessage = error.message ?: "Failed to return book"
                    if (errorMessage.contains("Response from com.kokb.lms.features.mybooks.data.remote.MyBooksApi.returnBook was null but response body type was declared as non-null")) {
                        // Bypass the error and refresh the page
                        loadUserBorrows()
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                    }
                }
        }
    }

    fun toggleHistoryView(showHistory: Boolean) {
        _showHistory.value = showHistory
    }

    fun isUserLoggedIn(): Boolean = authManager.isLoggedIn()
}

data class MyBooksState(
    val isLoading: Boolean = false,
    val borrows: List<UserBorrow> = emptyList(),
    val error: String? = null
) 