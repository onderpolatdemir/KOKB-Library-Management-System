//package com.kokb.lms.navigation
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.painterResource
//import com.kokb.lms.R
//
//sealed class NavIcon {
//    data class VectorIcon(val icon: ImageVector) : NavIcon()
//    data class PainterIcon(val icon: Painter) : NavIcon()
//}
//
//sealed class BottomNavigationItems(
//    val route: String,
//    val title: String,
////    val selectedIcon: Int,
////    val unselectedIcon: Int
//    val icon: NavIcon
//) {
//    object Home : BottomNavigationItems(
//        route = Routes.UserHomePage.route,
//        title = "Home",
//        icon = NavIcon.VectorIcon(Icons.Default.Home)
////        selectedIcon = R.drawable.home,
////        unselectedIcon = R.drawable.home_light
//    )
//
////    object UserDashboard : BottomNavigationItems(
////        route = "user_dashboard",
////        title = "Dashboard",
////        selectedIcon = R.drawable.dashboard,
////        unselectedIcon = R.drawable.dashboard_light
////    )
//
//    object Search : BottomNavigationItems(
//        route = Routes.SearchScreen.route,
//        title = "Search",
//        icon = NavIcon.VectorIcon(Icons.Default.Search)
//
////        selectedIcon = R.drawable.search,
////        unselectedIcon = R.drawable.search
//    )
////
////    object AddItem : BottomNavigationItems(
////        route = "add_item",
////        title = "Post",
////        selectedIcon = R.drawable.add_light,
////        unselectedIcon = R.drawable.add
////    )
//
//    object MapItem : BottomNavigationItems(
//        route = Routes.MyBooksScreen.route,
//        title = "My Books",
//        icon = NavIcon.VectorIcon(Icons.Default.DateRange)
//
////        selectedIcon = R.drawable.map_logo,
////        unselectedIcon = R.drawable.map_logo
//    )
//
//    object Profile : BottomNavigationItems(
//        route = Routes.ProfileScreen.route,
//        title = "Profile",
//        icon = NavIcon.VectorIcon(Icons.Default.Person)
//    )
//}

package com.kokb.lms.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.kokb.lms.R

sealed class NavIcon {
    data class VectorIcon(val icon: ImageVector) : NavIcon()
    data class PainterIcon(val icon: Painter) : NavIcon()
}

sealed class BottomNavigationItems(
    val route: String,
    val title: String,
    val icon: NavIcon
) {
    // Reader Screens
    object Home : BottomNavigationItems(
        route = Routes.UserHomePage.route,
        title = "Home",
        icon = NavIcon.VectorIcon(Icons.Default.Home)
    )

    object Search : BottomNavigationItems(
        route = Routes.SearchScreen.route,
        title = "Search",
        icon = NavIcon.VectorIcon(Icons.Default.Search)
    )

    object MapItem : BottomNavigationItems(
        route = Routes.MyBooksScreen.route,
        title = "My Books",
        icon = NavIcon.VectorIcon(Icons.Default.DateRange)
    )

    object Profile : BottomNavigationItems(
        route = Routes.ProfileScreen.route,
        title = "Profile",
        icon = NavIcon.VectorIcon(Icons.Default.Person)
    )

    // Librarian Screens
    object LibrarianHome : BottomNavigationItems(
        route = Routes.LibrarianHomePage.route,
        title = "Home",
        icon = NavIcon.VectorIcon(Icons.Default.Home)
    )

    object BookLoan : BottomNavigationItems(
        route = Routes.LibrarianBookLoan.route,
        title = "Book Loan",
        icon = NavIcon.VectorIcon(Icons.Default.Refresh)
    )

    object Cataloging : BottomNavigationItems(
        route = Routes.LibrarianCataloging.route,
        title = "Cataloging",
        icon = NavIcon.VectorIcon(Icons.Default.List) // Change icon if needed
    )

    object Members : BottomNavigationItems(
        route = Routes.LibrarianMembers.route,
        title = "Members",
        icon = NavIcon.VectorIcon(Icons.Default.Person) // Change icon if needed
    )


    object AdminHome : BottomNavigationItems(
        route = Routes.AdminHomePage.route,
        title = "Home",
        icon = NavIcon.VectorIcon(Icons.Default.Home) // Change icon if needed
    )
}

// Grouped Lists by Role
object BottomNavItemsByRole {
    val readerItems = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.Search,
        BottomNavigationItems.MapItem,
        BottomNavigationItems.Profile
    )

    val librarianItems = listOf(
        BottomNavigationItems.LibrarianHome,
        BottomNavigationItems.BookLoan,
        BottomNavigationItems.Cataloging,
        BottomNavigationItems.Members
    )

    val adminItems = listOf(
        BottomNavigationItems.AdminHome
    )
}
