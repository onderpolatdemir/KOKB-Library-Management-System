//package com.kokb.lms.features.auth.presentation.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.kokb.lms.features.auth.domain.model.User
//import com.kokb.lms.features.auth.domain.usecase.LoginUseCase
//import com.kokb.lms.features.auth.domain.usecase.RegisterUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val loginUseCase: LoginUseCase,
//    private val registerUseCase: RegisterUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
//    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
//
//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            loginUseCase(email, password)
//                .onSuccess { user ->
//                    _uiState.value = AuthUiState.Success(user)
//                }
//                .onFailure { error ->
//                    _uiState.value = AuthUiState.Error(error.message ?: "Unknown error")
//                }
//        }
//    }
//
//    fun register(name: String, surname: String, email: String, password: String, confirmPassword: String) {
//        viewModelScope.launch {
//            _uiState.value = AuthUiState.Loading
//            registerUseCase(name, surname, email, password, confirmPassword)
//                .onSuccess { user ->
//                    _uiState.value = AuthUiState.Success(user)
//                }
//                .onFailure { error ->
//                    _uiState.value = AuthUiState.Error(error.message ?: "Unknown error")
//                }
//        }
//    }
//}
//
//sealed class AuthUiState {
//    object Initial : AuthUiState()
//    object Loading : AuthUiState()
//    data class Success(val user: User) : AuthUiState()
//    data class Error(val message: String) : AuthUiState()
//}
package com.kokb.lms.features.auth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.usecase.LoginUseCase
import com.kokb.lms.features.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                loginUseCase(email.trim(), password.trim())
                    .onSuccess { user ->
                        _uiState.value = AuthUiState.Success(user)
                    }
                    .onFailure { error ->
                        _uiState.value = AuthUiState.Error(error.message ?: "Unknown error")
                    }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Unexpected error")
            }
        }
    }

    fun register(name: String, surname: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            if (password.trim() != confirmPassword.trim()) {
                _uiState.value = AuthUiState.Error("Passwords do not match")
                return@launch
            }
            try {
                registerUseCase(name.trim(), surname.trim(), email.trim(), password.trim(), confirmPassword.trim())
                    .onSuccess { user ->
                        Log.d("AuthViewModel", "Registration successful for user: ${user.email}")
                        _uiState.value = AuthUiState.Success(user)
                    }
                    .onFailure { error ->
                        Log.e("AuthViewModel", "Registration failed: ${error.message}")
                        _uiState.value = AuthUiState.Error(error.message ?: "Registration failed")
                    }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error: ${e.message}")
                _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Initial
    }
}

sealed interface AuthUiState {
    object Initial : AuthUiState
    object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

