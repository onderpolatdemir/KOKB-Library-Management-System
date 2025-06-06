package com.kokb.lms.navigation

sealed class UserRoutes(val route: String) {
    object UserHome : UserRoutes("user_home")
    object UserSearch : UserRoutes("user_search")
    object UserBorrows : UserRoutes("user_borrows")
    object UserProfile : UserRoutes("user_profile")
}

sealed class UserNavigationGraph(val route: String) {
    object UserGraph : UserNavigationGraph("user_graph")
} 