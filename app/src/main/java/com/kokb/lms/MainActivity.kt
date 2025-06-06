//package com.kokb.lms
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.kokb.lms.navigation.BottomBar
//import com.kokb.lms.navigation.NavigationGraph
//import com.kokb.lms.navigation.Routes
//import com.kokb.lms.ui.theme.LMSTheme
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
////        installSplashScreen()
//        val startDestination = Routes.Welcome.route
//
//        setContent {
//            LMSTheme {
//                MainContent(startDestination)
//            }
//        }
//    }
//
//    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//    @Composable
//    private fun MainContent(startDestination: String) {
//        val navController: NavHostController = rememberNavController()
//        var buttonsVisible by remember { mutableStateOf(false) }
//
//        Scaffold(
//            bottomBar = {
//                if (buttonsVisible) {
//                    BottomBar(navController = navController)
//                }
//            }
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(bottom = if (buttonsVisible) 76.dp else 0.dp)
//            ) {
//                NavigationGraph(
//                    navController = navController,
//                    startDestination = startDestination
//                ) { isVisible ->
//                    buttonsVisible = isVisible
//                }
//            }
//        }
//    }
//}

package com.kokb.lms

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.kokb.lms.core.manager.AuthManager
import com.kokb.lms.features.auth.domain.model.UserRole
import com.kokb.lms.navigation.BottomBar
import com.kokb.lms.navigation.NavigationGraph
import com.kokb.lms.navigation.Routes
import com.kokb.lms.ui.theme.LMSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authManager = AuthManager(this)
        val startDestination = Routes.Welcome.route

        setContent {
            LMSTheme {
                MainContent(startDestination)
            }
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    private fun MainContent(startDestination: String) {
        val navController: NavHostController = rememberNavController()
        val authManager = AuthManager(this)

        // Track auth state
        var isLoggedIn by remember { mutableStateOf(authManager.isLoggedIn()) }
        var userRole by remember { mutableStateOf(authManager.getLoginState()?.role ?: UserRole.READER) }

        // Get current route
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Update auth state when it changes
        LaunchedEffect(Unit) {
            snapshotFlow { authManager.isLoggedIn() to authManager.getLoginState()?.role }
                .collect { (loggedIn, role) ->
                    isLoggedIn = loggedIn
                    userRole = role ?: UserRole.READER
                }
        }

        // Reset navigation when logging out
        LaunchedEffect(isLoggedIn) {
            if (!isLoggedIn) {
                navController.navigate(Routes.Welcome.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }

        // Determine if bottom bar should be visible based on current route and user role
        val shouldShowBottomBar = remember(currentRoute, userRole, isLoggedIn) {
            when {
                !isLoggedIn -> false
                currentRoute == null -> false
                currentRoute in listOf(Routes.Welcome.route, Routes.Login.route, Routes.Signup.route) -> false
                userRole == UserRole.LIBRARIAN && currentRoute in listOf(
                    Routes.LibrarianHomePage.route,
                    Routes.LibrarianBookLoan.route,
                    Routes.LibrarianCataloging.route,
                    Routes.LibrarianMembers.route
                ) -> true
                userRole == UserRole.ADMIN && currentRoute in listOf(
                    Routes.AdminHomePage.route
                ) -> true
                userRole == UserRole.READER && currentRoute in listOf(
                    Routes.UserHomePage.route,
                    Routes.SearchScreen.route,
                    Routes.SearchResults.route,
                    Routes.MyBooksScreen.route,
                    Routes.ProfileScreen.route
                ) -> true
                else -> false
            }
        }

        // Debug logging
        LaunchedEffect(currentRoute, userRole, isLoggedIn, shouldShowBottomBar) {
            println("Debug: Current Route = $currentRoute")
            println("Debug: User Role = $userRole")
            println("Debug: Is Logged In = $isLoggedIn")
            println("Debug: Should Show Bottom Bar = $shouldShowBottomBar")
        }

        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar) {
                    BottomBar(
                        navController = navController,
                        userRole = userRole
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (shouldShowBottomBar) 76.dp else 0.dp)
            ) {
                NavigationGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}

