package com.kokb.lms.features.librarian.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.features.books.presentation.viewmodel.UpdateBookEvent
import com.kokb.lms.features.books.presentation.viewmodel.UpdateBookViewModel
import com.kokb.lms.features.search.domain.model.BookCategory
import com.kokb.lms.navigation.Routes
import com.kokb.lms.ui.theme.OrangePrimary
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarianUpdateBookScreen(
    navController: NavController,
    viewModel: UpdateBookViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Get the book ID from the navigation arguments
    val bookId = navController.currentBackStackEntry
        ?.arguments
        ?.getString("bookId")
        ?.let { UUID.fromString(it) }

    // Load the book data when the screen is first displayed
    LaunchedEffect(bookId) {
        bookId?.let { id ->
            viewModel.getBookById(id)
        }
    }

    // Handle success state
    LaunchedEffect(state.success) {
        if (state.success) {
            dialogMessage = "Book has been successfully updated."
            showDialog = true
        }
    }

    // Handle error state
    LaunchedEffect(state.error) {
        state.error?.let {
            dialogMessage = it
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                if (state.success) {
                    navController.navigate(Routes.LibrarianCataloging.route) {
                        popUpTo(Routes.LibrarianCataloging.route) {
                            inclusive = true
                        }
                    }
                }
            },
            title = { Text(if (state.success) "Success" else "Error") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        if (state.success) {
                            navController.navigate(Routes.LibrarianCataloging.route) {
                                popUpTo(Routes.LibrarianCataloging.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "UPDATE BOOK",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD6D6F5),
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { viewModel.onEvent(UpdateBookEvent.OnUpdateBook) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangePrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    enabled = viewModel.isFormValid() && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Update", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Book Name
                    Text("Book Name", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { viewModel.onEvent(UpdateBookEvent.OnTitleChange(it)) },
                        placeholder = { Text("Enter Book Name") },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            cursorColor = OrangePrimary
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Author Name
                    Text("Author Name", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.author,
                        onValueChange = { viewModel.onEvent(UpdateBookEvent.OnAuthorChange(it)) },
                        placeholder = { Text("Enter Author Name") },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            cursorColor = OrangePrimary
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // ISBN Number
                    Text("ISBN Number", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = state.isbn,
                        onValueChange = { newValue ->
                            // Only allow digits
                            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                viewModel.onEvent(UpdateBookEvent.OnIsbnChange(newValue))
                            }
                        },
                        placeholder = { Text("Enter ISBN Number") },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            cursorColor = OrangePrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Category
                    Text("Category", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        BookCategory.values().forEach { category ->
                            FilterChip(
                                selected = state.genre == category.name,
                                onClick = {
                                    viewModel.onEvent(UpdateBookEvent.OnGenreChange(category.name))
                                },
                                label = { Text(category.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OrangePrimary,
                                    containerColor = Color.LightGray
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Copies
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Copies", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Box {
                            OutlinedButton(
                                onClick = { expanded = true },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(state.copies.toString())
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                (1..20).forEach { count ->
                                    DropdownMenuItem(
                                        text = { Text(count.toString()) },
                                        onClick = {
                                            viewModel.onEvent(UpdateBookEvent.OnCopiesChange(count))
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 