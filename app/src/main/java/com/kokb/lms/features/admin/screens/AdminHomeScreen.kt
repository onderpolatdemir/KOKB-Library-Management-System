package com.kokb.lms.features.admin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.features.admin.screens.AdminHomeViewModel
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.auth.domain.model.UserRole
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kokb.lms.features.auth.domain.usecase.GetAllUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import com.kokb.lms.features.auth.domain.usecase.UpdateUserRoleUseCase
import androidx.compose.ui.res.painterResource
import com.kokb.lms.R
import com.kokb.lms.features.auth.domain.repository.AuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showLogoutDialog by remember { mutableStateOf(false) }

    // Show success message
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
            viewModel.clearSuccessMessage()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("admin_home") { inclusive = true }
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Admin Home",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showLogoutDialog = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.power_settings_new),
                            contentDescription = "Logout",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFBFC3E6),
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F8E8))
                .padding(horizontal = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchUsers(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search For a User...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Admin",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.users) { user ->
                            AdminUserCard(user = user, onGiveRole = { viewModel.updateUserRole(it, "LIBRARIAN") }, onTakeRole = { viewModel.updateUserRole(it, "READER") })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminUserCard(
    user: User,
    onGiveRole: (User) -> Unit,
    onTakeRole: (User) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf<DialogType?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF222222)
            )
            Divider(
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 12.dp)
                    .width(1.dp),
                color = Color(0xFFBFC3E6)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Text(
                    text = "ID-${user.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = when (user.role) {
                        UserRole.LIBRARIAN -> "Librarian"
                        UserRole.ADMIN -> "Admin"
                        UserRole.READER -> "User"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            when (user.role) {
                UserRole.READER -> Button(
                    onClick = {
                        dialogType = DialogType.GIVE_ROLE
                        showDialog = true
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27667B))
                ) {
                    Text("Give Role", color = Color.White)
                }
                UserRole.LIBRARIAN -> Button(
                    onClick = {
                        dialogType = DialogType.TAKE_ROLE
                        showDialog = true
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Take Role", color = Color.White)
                }
                UserRole.ADMIN -> {}
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Role Change") },
            text = {
                Text(
                    when (dialogType) {
                        DialogType.GIVE_ROLE -> "Are you sure to give a LIBRARIAN role to ${user.name}?"
                        DialogType.TAKE_ROLE -> "Are you sure to take back the LIBRARIAN role from ${user.name}?"
                        null -> ""
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (dialogType) {
                            DialogType.GIVE_ROLE -> onGiveRole(user)
                            DialogType.TAKE_ROLE -> onTakeRole(user)
                            null -> {}
                        }
                        showDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private enum class DialogType {
    GIVE_ROLE,
    TAKE_ROLE
}

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val updateUserRoleUseCase: UpdateUserRoleUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AdminHomeState())
    val state: StateFlow<AdminHomeState> = _state.asStateFlow()

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
                    _state.value = AdminHomeState(
                        users = users,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _state.value = AdminHomeState(
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

    fun updateUserRole(user: User, newRole: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            updateUserRoleUseCase(user.id, newRole)
                .onSuccess {
                    // Reload users after successful update
                    loadUsers()
                    // Show success message
                    _state.value = _state.value.copy(
                        successMessage = when (newRole) {
                            "LIBRARIAN" -> "LIBRARIAN role is successfully given to ${user.name}!"
                            "READER" -> "${user.name}'s role is successfully updated to READER!"
                            else -> "Role updated successfully"
                        }
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Failed to update user role",
                        isLoading = false
                    )
                }
        }
    }

    fun clearSuccessMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

data class AdminHomeState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

