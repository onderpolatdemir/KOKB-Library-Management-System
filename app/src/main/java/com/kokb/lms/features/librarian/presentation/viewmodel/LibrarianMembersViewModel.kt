package com.kokb.lms.features.librarian.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.usecase.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibrarianMembersViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LibrarianMembersState())
    val state: StateFlow<LibrarianMembersState> = _state.asStateFlow()

    private var allUsers: List<User> = emptyList()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getAllUsersUseCase()
                .onSuccess { users ->
                    allUsers = users
                    _state.value = LibrarianMembersState(
                        users = users,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _state.value = LibrarianMembersState(
                        error = error.message ?: "Failed to load users",
                        isLoading = false
                    )
                }
        }
    }

    fun searchUsers(query: String) {
        if (query.length < 3) {
            _state.value = _state.value.copy(users = allUsers)
            return
        }

        val filteredUsers = allUsers.filter { user ->
            user.name.contains(query, ignoreCase = true)
        }
        _state.value = _state.value.copy(users = filteredUsers)
    }
}

data class LibrarianMembersState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 