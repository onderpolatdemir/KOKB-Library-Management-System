package com.kokb.lms.features.librarian.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.R
import com.kokb.lms.features.books.domain.model.Book
import com.kokb.lms.features.books.domain.model.BookStatus
import com.kokb.lms.features.librarian.presentation.viewmodel.LibrarianBookLoanViewModel
import com.kokb.lms.navigation.BottomNavigationItems
import com.kokb.lms.ui.theme.OrangePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarianBookLoanScreen(
    navController: NavController,
    viewModel: LibrarianBookLoanViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val showAvailable by viewModel.showAvailable

    // Add LaunchedEffect to refresh books when screen is revisited
    LaunchedEffect(Unit) {
        viewModel.refreshBooks()
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
                            text = "LIBRARY LOAN",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(BottomNavigationItems.LibrarianHome.route) {
                            popUpTo(BottomNavigationItems.LibrarianHome.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Toggle buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { viewModel.toggleAvailabilityFilter(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showAvailable) OrangePrimary else Color.Transparent,
                        contentColor = if (showAvailable) Color.White else Color.Black
                    ),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Available")
                }

                Button(
                    onClick = { viewModel.toggleAvailabilityFilter(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showAvailable) OrangePrimary else Color.Transparent,
                        contentColor = if (!showAvailable) Color.White else Color.Black
                    ),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Reserved")
                }
            }

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
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.getFilteredBooks()) { book ->
                            BookCard(book = book)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCard(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Icon
            Icon(
                painter = painterResource(id = R.drawable.book_icon),
                contentDescription = "Book",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            // Vertical Divider
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 12.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            // Book Information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "ISBN: ${book.isbn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            // Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (book.status == BookStatus.AVAILABLE) 
                        Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = "Status",
                    tint = if (book.status == BookStatus.AVAILABLE) 
                        Color(0xFF4CAF50) else Color(0xFFE57373)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (book.status == BookStatus.AVAILABLE) "Available" else "Borrowed",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (book.status == BookStatus.AVAILABLE) 
                        Color(0xFF4CAF50) else Color(0xFFE57373)
                )
            }
        }
    }
} 