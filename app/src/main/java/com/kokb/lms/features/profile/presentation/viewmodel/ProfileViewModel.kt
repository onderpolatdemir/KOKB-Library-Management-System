package com.kokb.lms.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.BookManager
import com.kokb.lms.features.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookManager: BookManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                authRepository.getCurrentUser().collect { user ->
                    if (user != null) {
                        _uiState.value = ProfileUiState.UserInfo(
                            name = "${user.name} ${user.surname}",
                            id = user.id,
                            email = user.email
                        )
                    } else {
                        _uiState.value = ProfileUiState.Error("User not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to load user info")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                bookManager.clearBooks()
                authRepository.logout()
                _uiState.value = ProfileUiState.LoggedOut
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Logout failed")
            }
        }
    }
}

sealed class ProfileUiState {
    object Initial : ProfileUiState()
    object Loading : ProfileUiState()
    object LoggedOut : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
    data class UserInfo(
        val name: String,
        val id: String,
        val email: String
    ) : ProfileUiState()
} 