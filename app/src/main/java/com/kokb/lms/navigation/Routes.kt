//package com.kokb.lms.navigation
//
//sealed class Routes(val route: String) {
//    object Welcome : Routes("welcome")
//    object Signup : Routes("signup")
//    object Login : Routes("login")
//    object UserHomePage : Routes("user_home")
//    object LibrarianHomePage : Routes("librarian_home")
//    object LibrarianBookLoan : Routes("librarian_book_loan")
//    object LibrarianCataloging : Routes("librarian_cataloging")
//    object LibrarianMembers : Routes("librarian_members")
//    object SearchScreen: Routes("search_screen")
//    object SearchResults: Routes("search_results")
//    object MyBooksScreen: Routes("my_books_screen")
//    object ProfileScreen: Routes("profile_screen")
//}


package com.kokb.lms.navigation

sealed class Routes(val route: String) {
    object Welcome : Routes("welcome")
    object Signup : Routes("signup")
    object Login : Routes("login")

    object UserHomePage : Routes("user_home")
    object SearchScreen: Routes("search_screen")
    object SearchResults: Routes("search_results")
    object MyBooksScreen: Routes("my_books_screen")
    object ProfileScreen: Routes("profile_screen")

    object LibrarianHomePage : Routes("librarian_home")
    object LibrarianBookLoan : Routes("librarian_book_loan")
    object LibrarianCataloging : Routes("librarian_cataloging")
    object LibrarianMembers : Routes("librarian_members")
    object LibrarianAddBook : Routes("librarian_add_book")
    object LibrarianUpdateBook : Routes("librarian_update_book/{bookId}") {
        fun createRoute(bookId: String) = "librarian_update_book/$bookId"
    }

    object AdminHomePage : Routes("admin_home")
}
