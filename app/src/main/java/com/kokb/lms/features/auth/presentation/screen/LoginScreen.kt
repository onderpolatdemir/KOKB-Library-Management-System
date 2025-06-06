//package com.kokb.lms.features.auth.presentation.screen
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.kokb.lms.features.auth.presentation.viewmodel.AuthUiState
//import com.kokb.lms.features.auth.presentation.viewmodel.AuthViewModel
//import android.util.Patterns
//import com.kokb.lms.navigation.Routes
//
//@Composable
//fun LoginScreen(
//    navController: NavController,
//    onLoginSuccess: () -> Unit = {},
//    onErrorDismiss: () -> Unit = {},
//    viewModel: AuthViewModel = hiltViewModel()
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    val uiState by viewModel.uiState.collectAsState()
//    var showErrorDialog by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf("") }
//    var showSuccessDialog by remember { mutableStateOf(false) }
//
//    // Email validation
//    val isEmailValid = remember(email) {
//        Patterns.EMAIL_ADDRESS.matcher(email).matches()
//    }
//
//    LaunchedEffect(uiState) {
//        when (uiState) {
//            is AuthUiState.Success -> {
//                showSuccessDialog = true
//            }
//            is AuthUiState.Error -> {
//                errorMessage = (uiState as AuthUiState.Error).message
//                showErrorDialog = true
//            }
//            else -> {}
//        }
//    }
//
//    if (showErrorDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                showErrorDialog = false},
//            title = { Text("Login Error") },
//            text = { Text(errorMessage) },
//            confirmButton = {
//                TextButton(onClick = { showErrorDialog = false }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    if (showSuccessDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                showSuccessDialog = false
//                onLoginSuccess()
//            },
//            title = { Text("Success") },
//            text = { Text("Login successful!") },
//            confirmButton = {
//                TextButton(onClick = {
//                    showSuccessDialog = false
//                    onLoginSuccess()
//                }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(32.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Email") },
//                singleLine = true,
//                isError = email.isNotBlank() && !isEmailValid,
//                supportingText = {
//                    if (email.isNotBlank() && !isEmailValid) {
//                        Text("Please enter a valid email address")
//                    }
//                },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Next
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                singleLine = true,
//                visualTransformation = PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Password,
//                    imeAction = ImeAction.Done
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = { viewModel.login(email, password) },
//                enabled = isEmailValid && password.isNotBlank() && uiState !is AuthUiState.Loading,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (uiState is AuthUiState.Loading) {
//                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
//                } else {
//                    Text("Login")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            TextButton(onClick = {
//                navController.navigate(Routes.Signup.route)
//            }) {
//                Text("Don't have an account? Register")
//            }
//        }
//    }
//}

package com.kokb.lms.features.auth.presentation.screen

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.R
import com.kokb.lms.features.auth.presentation.viewmodel.AuthUiState
import com.kokb.lms.features.auth.presentation.viewmodel.AuthViewModel
import com.kokb.lms.navigation.Routes
import com.kokb.lms.features.auth.domain.model.UserRole

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit = {},
    onErrorDismiss: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Email validation
    val isEmailValid = remember(email) {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                val user = (uiState as AuthUiState.Success).user
                when (user.role) {
                    UserRole.LIBRARIAN -> {
                        navController.navigate(Routes.LibrarianHomePage.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    UserRole.ADMIN -> {
                        navController.navigate(Routes.AdminHomePage.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    UserRole.READER -> {
                        navController.navigate(Routes.UserHomePage.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            is AuthUiState.Error -> {
                errorMessage = (uiState as AuthUiState.Error).message
                showErrorDialog = true
            }
            else -> {}
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                onErrorDismiss()
            },
            title = { Text("Login Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showErrorDialog = false
                    onErrorDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onLoginSuccess()
            },
            title = { Text("Success") },
            text = { Text("Login successful!") },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onLoginSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logotext),
                contentDescription = "Logo Text",
                modifier = Modifier
                    .width(161.dp)
                    .height(87.dp)
            )

            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                isError = email.isNotBlank() && !isEmailValid,
                supportingText = {
                    if (email.isNotBlank() && !isEmailValid) {
                        Text("Please enter a valid email address")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email, password) },
                enabled = isEmailValid && password.isNotBlank() && uiState !is AuthUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                navController.navigate(Routes.Signup.route)
            }) {
                Text("Don't have an account? Register")
            }
        }
    }
}
