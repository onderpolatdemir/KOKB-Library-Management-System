

package com.kokb.lms.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kokb.lms.features.auth.domain.model.UserRole

@Composable
fun BottomBar(
    navController: NavHostController,
    userRole: UserRole,
    modifier: Modifier = Modifier
) {
    val screens = when (userRole) {
        UserRole.READER -> BottomNavItemsByRole.readerItems
        UserRole.LIBRARIAN -> BottomNavItemsByRole.librarianItems
        UserRole.ADMIN -> BottomNavItemsByRole.adminItems
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (val icon = screen.icon) {
                        is NavIcon.VectorIcon -> Icon(
                            imageVector = icon.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                        is NavIcon.PainterIcon -> Icon(
                            painter = icon.icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF27667B),
                    selectedTextColor = Color(0xFF27667B),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
