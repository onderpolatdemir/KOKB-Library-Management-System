package com.kokb.lms.features.userdashboard.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import com.kokb.lms.features.books.domain.use_case.GetAllBooksUseCase
import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import com.kokb.lms.features.userdashboard.domain.repository.UserDashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDashboardRepository: UserDashboardRepository,
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val bookManager: BookManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserHomeUiState>(UserHomeUiState.Loading)
    val uiState: StateFlow<UserHomeUiState> = _uiState

    fun loadUserHome() {
        viewModelScope.launch {
            try {
                Log.d("UserHomeViewModel", "Starting to load user home")
                authRepository.getCurrentUser()
                    .catch { e ->
                        Log.e("UserHomeViewModel", "Error loading user: ${e.message}")
                        _uiState.value = UserHomeUiState.Error(e.message ?: "Failed to load user data")
                    }
                    .collect { user ->
                        if (user != null) {
                            Log.d("UserHomeViewModel", "User loaded successfully: ${user.id}")
                            // Load dashboard data for the user
                            userDashboardRepository.getUserDashboard(user.id)
                                .catch { e ->
                                    Log.e("UserHomeViewModel", "Error loading dashboard: ${e.message}")
                                    _uiState.value = UserHomeUiState.Error(e.message ?: "Failed to load dashboard data")
                                }
                                .collect { dashboard ->
                                    Log.d("UserHomeViewModel", "Dashboard loaded successfully")
                                    // After loading dashboard, load books
                                    loadBooks(user, dashboard)
                                }
                        } else {
                            Log.e("UserHomeViewModel", "No user found")
                            _uiState.value = UserHomeUiState.Error("User not found")
                        }
                    }
            } catch (e: Exception) {
                Log.e("UserHomeViewModel", "Unexpected error: ${e.message}")
                _uiState.value = UserHomeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadBooks(user: User, dashboard: UserDashboard) {
        viewModelScope.launch {
            Log.d("UserHomeViewModel", "Starting to load books")
            getAllBooksUseCase().collect { result ->
                _uiState.value = when (result) {
                    is com.kokb.lms.core.util.Resource.Success -> {
                        val books = result.data ?: emptyList()
                        Log.d("UserHomeViewModel", "Books loaded successfully. Count: ${books.size}")
                        // Save books to BookManager for use in search
                        bookManager.saveBooks(books)
                        // Verify books were saved
                        val savedBooks = bookManager.getBooks()
                        Log.d("UserHomeViewModel", "Books saved to BookManager. Saved count: ${savedBooks.size}")
                        UserHomeUiState.Success(user, dashboard, books)
                    }
                    is com.kokb.lms.core.util.Resource.Error -> {
                        Log.e("UserHomeViewModel", "Error loading books: ${result.message}")
                        UserHomeUiState.Error(result.message ?: "Failed to load books")
                    }
                    is com.kokb.lms.core.util.Resource.Loading -> {
                        Log.d("UserHomeViewModel", "Loading books...")
                        UserHomeUiState.Loading
                    }
                }
            }
        }
    }
}

sealed class UserHomeUiState {
    data object Loading : UserHomeUiState()
    data class Success(
        val user: User,
        val dashboard: UserDashboard? = null,
        val books: List<com.kokb.lms.features.books.domain.model.Book> = emptyList()
    ) : UserHomeUiState()
    data class Error(val message: String) : UserHomeUiState()
}