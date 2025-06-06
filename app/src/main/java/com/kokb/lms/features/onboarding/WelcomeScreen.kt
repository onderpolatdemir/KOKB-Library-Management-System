package com.kokb.lms.presentation.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.kokb.lms.navigation.Routes
import kotlinx.coroutines.launch

data class OnboardingPage(
    val imageUrl: String = "",
    val title: String,
    val description: String,
    val backgroundColor: Color = Color(0xFFF5F5F5)
)

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val onboardingPages = listOf(
        OnboardingPage(
            imageUrl = "https://example.com/onboarding1.jpg",
            title = "Need a Book?",
            description = "Don't worry! Our library is here to help you find books and other resources.",
            backgroundColor = Color(0xFFFFF3E0)
        ),
        OnboardingPage(
            imageUrl = "https://example.com/onboarding2.jpg",
            title = "Need to Return?",
            description = "Don't worry! Our library is here to help you return books and other resources.",
            backgroundColor = Color(0xFFE3F2FD)
        ),
        OnboardingPage(
            imageUrl = "https://example.com/onboarding3.jpg",
            title = "KOKB Library",
            description = "Join our library. Together, we make our library a better place!",
            backgroundColor = Color(0xFFE8F5E9)
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPage(onboardingPages[page])
        }

        TextButton(
            onClick = { 
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Welcome.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                "Skip",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == iteration) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == iteration)
                                    Color(0xFF27667B)
                                else
                                    Color(0xFF27667B).copy(alpha = 0.5f)
                            )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = pagerState.currentPage > 0,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Text("Back", color = Color(0xFF27667B))
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            navController.navigate(Routes.Login.route) {
                                popUpTo(Routes.Welcome.route) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27667B)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .widthIn(min = 140.dp)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pagerState.pageCount - 1) 
                            "Get Started" 
                        else 
                            "Next",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingPage(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = page.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(0.4f)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF0D171C),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color(0xFF0D171C).copy(alpha = 0.8f)
            )
        }
    }
}