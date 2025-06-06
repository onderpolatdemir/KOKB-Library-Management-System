package com.kokb.lms.features.librarian.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import com.kokb.lms.features.auth.domain.usecase.GetAllUsersUseCase
import com.kokb.lms.features.books.domain.use_case.GetAllBooksUseCase
import com.kokb.lms.features.librarian.domain.model.LibrarianDashboard
import com.kokb.lms.features.librarian.domain.model.LibraryStatistics
import com.kokb.lms.features.librarian.domain.repository.LibrarianDashboardRepository
import com.kokb.lms.features.profile.presentation.viewmodel.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrarianHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val librarianDashboardRepository: LibrarianDashboardRepository,
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val bookManager: BookManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibrarianHomeUiState>(LibrarianHomeUiState.Initial)
    val uiState: StateFlow<LibrarianHomeUiState> = _uiState

    fun loadLibrarianHome() {
        viewModelScope.launch {
            try {
                Log.d("LibrarianHomeViewModel", "Starting to load librarian home")
                _uiState.value = LibrarianHomeUiState.Loading
                authRepository.getCurrentUser()
                    .catch { e ->
                        Log.e("LibrarianHomeViewModel", "Error loading user: ${e.message}")
                        _uiState.value = LibrarianHomeUiState.Error(e.message ?: "Failed to load user data")
                    }
                    .collect { user ->
                        if (user != null) {
                            Log.d("LibrarianHomeViewModel", "User loaded successfully: ${user.id}")
                            // Load dashboard data for the librarian
                            librarianDashboardRepository.getLibrarianDashboard(user.id)
                                .catch { e ->
                                    Log.e("LibrarianHomeViewModel", "Error loading dashboard: ${e.message}")
                                    _uiState.value = LibrarianHomeUiState.Error(e.message ?: "Failed to load dashboard data")
                                }
                                .collect { dashboard ->
                                    Log.d("LibrarianHomeViewModel", "Dashboard loaded successfully")
                                    // After loading dashboard, load books and users
                                    loadBooksAndUsers(user, dashboard)
                                }
                        } else {
                            Log.e("LibrarianHomeViewModel", "No user found")
                            _uiState.value = LibrarianHomeUiState.Error("User not found")
                        }
                    }
            } catch (e: Exception) {
                Log.e("LibrarianHomeViewModel", "Unexpected error: ${e.message}")
                _uiState.value = LibrarianHomeUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadBooksAndUsers(user: User, dashboard: LibrarianDashboard) {
        viewModelScope.launch {
            Log.d("LibrarianHomeViewModel", "Starting to load books and users")
            
            // Load users first
            getAllUsersUseCase()
                .onSuccess { users ->
                    Log.d("LibrarianHomeViewModel", "Users loaded successfully. Count: ${users.size}")
                    // Update dashboard with actual user count
                    val updatedDashboard = dashboard.copy(
                        statistics = dashboard.statistics.copy(
                            activeMembers = users.size
                        )
                    )
                    
                    // Now load books
                    getAllBooksUseCase().collect { result ->
                        _uiState.value = when (result) {
                            is com.kokb.lms.core.util.Resource.Success -> {
                                val books = result.data ?: emptyList()
                                Log.d("LibrarianHomeViewModel", "Books loaded successfully. Count: ${books.size}")
                                bookManager.saveBooks(books)
                                LibrarianHomeUiState.Success(user, updatedDashboard, books)
                            }
                            is com.kokb.lms.core.util.Resource.Error -> {
                                Log.e("LibrarianHomeViewModel", "Error loading books: ${result.message}")
                                LibrarianHomeUiState.Error(result.message ?: "Failed to load books")
                            }
                            is com.kokb.lms.core.util.Resource.Loading -> {
                                Log.d("LibrarianHomeViewModel", "Loading books...")
                                LibrarianHomeUiState.Loading
                            }
                        }
                    }
                }
                .onFailure { error ->
                    Log.e("LibrarianHomeViewModel", "Error loading users: ${error.message}")
                    _uiState.value = LibrarianHomeUiState.Error(error.message ?: "Failed to load users")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                Log.d("LibrarianHomeViewModel", "Starting logout process")
                _uiState.value = LibrarianHomeUiState.Loading
                
                // Clear book data
                bookManager.clearBooks()
                
                // Perform logout
                authRepository.logout()
                
                Log.d("LibrarianHomeViewModel", "Logout successful")
                _uiState.value = LibrarianHomeUiState.LoggedOut
            } catch (e: Exception) {
                Log.e("LibrarianHomeViewModel", "Logout failed: ${e.message}")
                _uiState.value = LibrarianHomeUiState.Error(e.message ?: "Logout failed")
            }
        }
    }
}

sealed class LibrarianHomeUiState {
    data object Initial : LibrarianHomeUiState()
    data object Loading : LibrarianHomeUiState()
    data class Success(
        val user: User,
        val dashboard: LibrarianDashboard? = null,
        val books: List<com.kokb.lms.features.books.domain.model.Book> = emptyList()
    ) : LibrarianHomeUiState()
    data object LoggedOut : LibrarianHomeUiState()
    data class Error(val message: String) : LibrarianHomeUiState()
} 