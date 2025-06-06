package com.kokb.lms.features.mybooks.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.R
import com.kokb.lms.features.mybooks.domain.model.UserBorrow
import com.kokb.lms.features.mybooks.presentation.viewmodel.MyBooksViewModel
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.navigation.Routes
import javax.inject.Inject
import java.time.format.DateTimeFormatter
import com.kokb.lms.ui.theme.OrangePrimary
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.kokb.lms.navigation.BottomNavigationItems


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBooksScreen(
    navController: NavController,
    viewModel: MyBooksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val showHistory by viewModel.showHistory

    val (pendingReturn, setPendingReturn) = remember { mutableStateOf<UserBorrow?>(null) }

    if (pendingReturn != null) {
        AlertDialog(
            onDismissRequest = { setPendingReturn(null) },
            title = { Text("Return Book") },
            text = { Text("Are you sure to return the ${pendingReturn.book?.title ?: "book"} book?") },
            confirmButton = {
                TextButton(onClick = {
                    setPendingReturn(null)
                    viewModel.returnBook(pendingReturn.id.toString())
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { setPendingReturn(null) }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Check login and load borrows when screen becomes active
    LaunchedEffect(Unit) {
        if (!viewModel.isUserLoggedIn()) {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.MyBooksScreen.route) { inclusive = true }
            }
        }
    }

    // Use rememberUpdatedState to track when the screen becomes active
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentLifecycleOwner by rememberUpdatedState(lifecycleOwner)

    // Load borrows whenever the screen becomes active
    DisposableEffect(currentLifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (viewModel.isUserLoggedIn()) {
                    viewModel.loadUserBorrows()
                }
            }
        }

        currentLifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            currentLifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "MY BOOKS",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(BottomNavigationItems.Home.route) {
                            popUpTo(BottomNavigationItems.Home.route) { inclusive = true }
                        }
                        //navController.navigateUp()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
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
                    onClick = { viewModel.toggleHistoryView(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showHistory) OrangePrimary else Color.Transparent,
                        contentColor = if (!showHistory) Color.White else Color.Black
                    ),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24), 
                            contentDescription = "Active"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Active")
                    }
                }

                Button(
                    onClick = { viewModel.toggleHistoryView(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showHistory) OrangePrimary else Color.Transparent,
                        contentColor = if (showHistory) Color.White else Color.Black
                    ),
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_check_circle_outline_24), 
                            contentDescription = "History"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("History")
                    }
                }
            }

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                state.error != null -> {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                else -> {
                    val filteredBorrows = if (showHistory) {
                        Log.d("MyBooksDebug", "Filtering for history view, total borrows: ${state.borrows.size}")
                        state.borrows.filter { it.returnedAt != null }.also { filtered ->
                            Log.d("MyBooksDebug", "Found ${filtered.size} returned books")
                        }
                    } else {
                        Log.d("MyBooksDebug", "Filtering for active view, total borrows: ${state.borrows.size}")
                        state.borrows.filter { it.returnedAt == null }.also { filtered ->
                            Log.d("MyBooksDebug", "Found ${filtered.size} active books")
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredBorrows) { borrow ->
                            BorrowedBookCard(
                                borrow = borrow,
                                showHistory = showHistory,
                                onReturnClick = { setPendingReturn(borrow) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BorrowedBookCard(
    borrow: UserBorrow,
    showHistory: Boolean,
    onReturnClick: () -> Unit
) {
    Log.d("MyBooksDebug", "Rendering BorrowedBookCard - bookTitle: ${borrow.book?.title}, status: ${borrow.status}, returnedAt: ${borrow.returnedAt}, dueDate: ${borrow.dueDate}")
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = if (showHistory) painterResource(R.drawable.baseline_check_circle_outline_24) else painterResource(R.drawable.baseline_access_time_24),
                contentDescription = if (showHistory) "Returned" else "Active",
                tint = if (showHistory) MaterialTheme.colorScheme.primary else Color.Gray
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = borrow.book?.title ?: "Unknown Book",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!showHistory) {
                        Button(
                            onClick = onReturnClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Return")
                        }
                    }
                }
                Text(
                    text = borrow.book?.author ?: "Unknown Author",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Borrowed Date",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = dateFormatter.format(borrow.borrowedAt),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Column {
                        Text(
                            text = if (showHistory) "Returned Date" else "Due Date",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = dateFormatter.format(if (showHistory) borrow.returnedAt!! else borrow.dueDate),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
} 