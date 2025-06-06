package com.kokb.lms.features.userdashboard.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.R
import com.kokb.lms.features.auth.domain.model.User
import com.kokb.lms.features.userdashboard.domain.model.UserDashboard
import com.kokb.lms.features.userdashboard.presentation.viewmodel.UserHomeViewModel
import com.kokb.lms.features.userdashboard.presentation.viewmodel.UserHomeUiState
import com.kokb.lms.navigation.Routes
import com.kokb.lms.ui.theme.PurpleGrey40
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    navController: NavController,
    viewModel: UserHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadUserHome()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HOME") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UserHomeUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UserHomeUiState.Success -> {
                    val successState = uiState as UserHomeUiState.Success
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        UserHomeContent(
                            user = successState.user,
                            dashboard = successState.dashboard,
                            navController = navController,
                        )
                    }
                }
                is UserHomeUiState.Error -> {
                    val error = (uiState as UserHomeUiState.Error).message
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserHomeContent(
    user: User,
    dashboard: UserDashboard?,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Hi, ${user.name}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Reading Process Card with Pie Chart
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.book_icon),
                        contentDescription = null,
                        tint = PurpleGrey40,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reading Process",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                dashboard?.let { dash ->
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val total = dash.statistics.totalBorrowed.toFloat()
                            val notReturned = dash.statistics.notReturned.toFloat()
                            
                            if (total > 0) {
                                val notReturnedAngle = (notReturned / total) * 360f
                                val returnedAngle = 360f - notReturnedAngle
                                
                                // Draw not returned section (blue)
                                drawArc(
                                    color = Color(0xFF4285F4),
                                    startAngle = 0f,
                                    sweepAngle = notReturnedAngle,
                                    useCenter = true,
                                    size = Size(size.width, size.height)
                                )
                                
                                // Draw returned section (navy)
                                drawArc(
                                    color = Color(0xFF0F1B4C),
                                    startAngle = notReturnedAngle,
                                    sweepAngle = returnedAngle,
                                    useCenter = true,
                                    size = Size(size.width, size.height)
                                )
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Total Borrowed indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFF0F1B4C))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Total Borrowed: ${dash.statistics.totalBorrowed}")
                        }
                        
                        // Not Returned indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(Color(0xFF4285F4))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Not Returned: ${dash.statistics.notReturned}")
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Statistics Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Visitors Card
            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.book_icon),
                        contentDescription = null,
                        tint = PurpleGrey40,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Total Visitors",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "${dashboard?.statistics?.totalVisitors ?: 0}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            
            // Borrowed Books Card
            Card(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.book_icon),
                        contentDescription = null,
                        tint = PurpleGrey40,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Borrowed Books",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "${dashboard?.statistics?.borrowedBooks ?: 0}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Loan Reminder Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Loan Reminder",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = {navController.navigate(Routes.MyBooksScreen.route)}) {
                        Text("See All")
                    }
                }
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                // Dummy loan data
                Column {
                    LoanItem(
                        title = "Software Engineering",
                        author = "Ian Sommerville",
                        returnDate = "April 24, 2025"
                    )
                    LoanItem(
                        title = "Clean Code",
                        author = "Robert C. Martin",
                        returnDate = "May 15, 2025"
                    )
                }
            }
        }
    }
}

@Composable
private fun LoanItem(
    title: String,
    author: String,
    returnDate: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = author,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = returnDate,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
} 