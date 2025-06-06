package com.kokb.lms.features.search.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kokb.lms.R
import com.kokb.lms.features.search.domain.model.BookAvailability
import com.kokb.lms.features.search.domain.model.BookCategory
import com.kokb.lms.features.search.domain.model.SearchFilters
import com.kokb.lms.features.search.presentation.viewmodel.SearchViewModel
import com.kokb.lms.navigation.BottomNavigationItems
import com.kokb.lms.navigation.Routes
import com.kokb.lms.ui.theme.OrangePrimary
        
        
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {
    var bookName by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<BookCategory?>(null) }
    var selectedAvailability by remember { mutableStateOf<BookAvailability?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SEARCH",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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

        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(32.dp)
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
                        value = bookName,
                        onValueChange = { bookName = it },
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
                        value = authorName,
                        onValueChange = { authorName = it },
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

                    // ISBN
                    Text("ISBN Number", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = isbn,
                        onValueChange = { isbn = it },
                        placeholder = { Text("Enter ISBN") },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            cursorColor = OrangePrimary
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Categories
                    Text("Category", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        BookCategory.values().forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = {
                                    selectedCategory = if (selectedCategory == category) null else category
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

                    // Availability
                    Text("Availability", style = MaterialTheme.typography.labelLarge, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BookAvailability.values().forEach { availability ->
                            FilterChip(
                                selected = selectedAvailability == availability,
                                onClick = {
                                    selectedAvailability = if (selectedAvailability == availability) null else availability
                                },
                                label = { Text(availability.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OrangePrimary,
                                    containerColor = Color.LightGray
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val filters = SearchFilters(
                        bookName = bookName,
                        authorName = authorName,
                        isbn = isbn,
                        category = selectedCategory,
                        availability = selectedAvailability
                    )
                    viewModel.search(filters)
                    navController.navigate(Routes.SearchResults.route)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangePrimary,
                ),
            ) {
                Text("Search", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: Dp = 0.dp,
    crossAxisSpacing: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0))
            
            if (currentRowWidth + placeable.width + mainAxisSpacing.roundToPx() > constraints.maxWidth) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            
            currentRow.add(placeable)
            currentRowWidth += placeable.width + mainAxisSpacing.roundToPx()
        }
        
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        val height = if (rows.isNotEmpty()) {
            rows.size * (rows.first().first().height) +
                    ((rows.size - 1) * crossAxisSpacing.roundToPx())
        } else {
            0
        }

        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.place(x, y)
                    x += placeable.width + mainAxisSpacing.roundToPx()
                }
                y += row.first().height + crossAxisSpacing.roundToPx()
            }
        }
    }
} 