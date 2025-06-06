package com.kokb.lms.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.kokb.lms.features.admin.screens.AdminHomeScreen
import com.kokb.lms.features.auth.presentation.screen.LoginScreen
import com.kokb.lms.features.auth.presentation.screen.RegisterScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianAddBookScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianBookLoanScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianCatalogingScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianHomeScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianMembersScreen
import com.kokb.lms.features.librarian.presentation.screen.LibrarianUpdateBookScreen
import com.kokb.lms.features.profile.presentation.screen.ProfileScreen
import com.kokb.lms.features.search.presentation.viewmodel.SearchViewModel
import com.kokb.lms.features.userdashboard.presentation.screen.*
import com.kokb.lms.navigation.Routes
import com.kokb.lms.presentation.onboarding.WelcomeScreen
import com.kokb.lms.features.search.presentation.screen.SearchScreen
import com.kokb.lms.features.search.presentation.screen.SearchResultsScreen
import com.kokb.lms.features.mybooks.presentation.screen.MyBooksScreen
import com.kokb.lms.features.profile.presentation.viewmodel.ProfileViewModel
import com.kokb.lms.features.librarian.presentation.viewmodel.LibrarianHomeViewModel
import com.kokb.lms.features.mybooks.presentation.viewmodel.MyBooksViewModel
import com.kokb.lms.features.userdashboard.presentation.viewmodel.UserHomeViewModel
import com.kokb.lms.features.librarian.presentation.viewmodel.LibrarianMembersViewModel
import com.kokb.lms.features.librarian.presentation.viewmodel.LibrarianBookLoanViewModel
import com.kokb.lms.features.books.presentation.viewmodel.UpdateBookViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    onBottomBarVisibility: (Boolean) -> Unit = { }  // Keep the default for backward compatibility
) {
    NavHost(navController, startDestination = startDestination) {
        composable(Routes.Welcome.route) {
            onBottomBarVisibility(false)  // Hide bottom bar
            WelcomeScreen(navController = navController)
        }

        composable(
            Routes.UserHomePage.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<UserHomeViewModel>()
            UserHomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.SearchScreen.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.SearchResults.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Routes.SearchScreen.route)
            }
            val viewModel = hiltViewModel<SearchViewModel>(parentEntry)
            SearchResultsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.MyBooksScreen.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<MyBooksViewModel>()
            MyBooksScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.ProfileScreen.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.Signup.route,
            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToLeft
        ) {
            onBottomBarVisibility(false)  // Hide bottom bar
            RegisterScreen(navController = navController)
        }

        composable(
            Routes.Login.route,
            enterTransition = ::slideInToLeft,
            exitTransition = ::slideOutToLeft
        ) {
            onBottomBarVisibility(false)  // Hide bottom bar
            LoginScreen(
                navController = navController,
                onLoginSuccess = { },  // Success is handled in LoginScreen itself
                onErrorDismiss = { }   // Error is handled in LoginScreen itself
            )
        }

        composable(
            Routes.LibrarianHomePage.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<LibrarianHomeViewModel>()
            LibrarianHomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.LibrarianBookLoan.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<LibrarianBookLoanViewModel>()
            LibrarianBookLoanScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.LibrarianCataloging.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            //val viewModel = hiltViewModel<LibrarianCatalogingViewModel>()
            LibrarianCatalogingScreen(
                navController = navController,
                //viewModel = viewModel
            )
        }

        composable(
            Routes.LibrarianAddBook.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)
            LibrarianAddBookScreen(navController = navController)
        }

        composable(
            route = Routes.LibrarianUpdateBook.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) { backStackEntry ->
            onBottomBarVisibility(true)
            val bookId = backStackEntry.arguments?.getString("bookId")
            val viewModel = hiltViewModel<UpdateBookViewModel>()
            LibrarianUpdateBookScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            Routes.LibrarianMembers.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            val viewModel = hiltViewModel<LibrarianMembersViewModel>()
            LibrarianMembersScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Admin Routes
        composable(
            Routes.AdminHomePage.route,
            enterTransition = ::slideInToRight,
            exitTransition = ::slideOutToRight
        ) {
            onBottomBarVisibility(true)  // Show bottom bar
            AdminHomeScreen(navController = navController)
        }
    }
}

fun slideInToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideInToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

fun slideOutToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideOutToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}
