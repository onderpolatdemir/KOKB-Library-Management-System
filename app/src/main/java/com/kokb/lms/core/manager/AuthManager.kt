//package com.kokb.lms.core.manager
//
//import android.content.Context
//import android.content.SharedPreferences
//import androidx.core.content.edit
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class AuthManager @Inject constructor(
//    @ApplicationContext private val context: Context
//) {
//    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//    var isUserLoggedIn: Boolean
//        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
//        set(value) = prefs.edit() { putBoolean(KEY_IS_LOGGED_IN, value) }
//
//    var accessToken: String?
//        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
//        set(value) = prefs.edit() { putString(KEY_ACCESS_TOKEN, value) }
//
//    var accessTokenExpiry: Long
//        get() = prefs.getLong(KEY_ACCESS_TOKEN_EXPIRY, 0)
//        set(value) = prefs.edit() { putLong(KEY_ACCESS_TOKEN_EXPIRY, value) }
//
//    var userId: String?
//        get() = prefs.getString(KEY_USER_ID, null)
//        set(value) = prefs.edit() { putString(KEY_USER_ID, value) }
//
//    fun saveLoginState(
//        username: String,
//        accessToken: String,
//        //expiresIn: Long?,
//        userId: String?
//    ) {
//        isUserLoggedIn = true
//        this.accessToken = accessToken
//
//        // Set token expiry time (current time + expiry duration)
////        this.accessTokenExpiry = if (expiresIn != null) {
////            System.currentTimeMillis() + (expiresIn * 1000)
////        } else {
////            0
////        }
//
//        this.userId = userId
//    }
//
//    fun clearLoginState() {
//        isUserLoggedIn = false
//        accessToken = null
//        //accessTokenExpiry = 0
//        userId = null
//    }
//
//    // Helper function to check token status for debugging
//    fun getAuthDebugInfo(): Map<String, String?> {
//        return mapOf(
//            "isUserLoggedIn" to isUserLoggedIn.toString(),
//            "userId" to userId,
//            "accessToken" to (accessToken?.take(20) + "..." + accessToken?.takeLast(20)),
//            "accessTokenExpiry" to if (accessTokenExpiry > 0) {
//                val remainingMillis = accessTokenExpiry - System.currentTimeMillis()
//                if (remainingMillis > 0) {
//                    "Expires in ${remainingMillis / 1000} seconds"
//                } else {
//                    "EXPIRED ${-remainingMillis / 1000} seconds ago"
//                }
//            } else {
//                "Not set"
//            }
//        )
//    }
//
//    companion object {
//        private const val PREFS_NAME = "lms_prefs"
//        private const val KEY_IS_LOGGED_IN = "is_logged_in"
//        private const val KEY_ACCESS_TOKEN = "access_token"
//        private const val KEY_ACCESS_TOKEN_EXPIRY = "access_token_expiry"
//        private const val KEY_USER_ID = "user_id"
//    }
//}

package com.kokb.lms.core.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.kokb.lms.features.auth.domain.model.User

class AuthManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    // Save login state
    fun saveLoginState(user: User) {
        Log.d("AuthManager", "Saving login state for user: ${user.id}, ${user.email}, ${user.name}")
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString("logged_in_user", userJson).apply()
    }

    // Get login state
    fun getLoginState(): User? {
        val userJson = sharedPreferences.getString("logged_in_user", null)
        Log.d("AuthManager", "Retrieved user JSON: $userJson")
        return userJson?.let {
            try {
                gson.fromJson(it, User::class.java)?.also { user ->
                    Log.d("AuthManager", "Parsed user: ${user.id}, ${user.email}, ${user.name}")
                }
            } catch (e: Exception) {
                Log.e("AuthManager", "Error parsing user JSON: ${e.message}")
                null
            }
        }
    }

    // Clear login state
    fun clearLoginState() {
        sharedPreferences.edit().remove("logged_in_user").apply()
    }

    // Optional: Check if user is logged in
    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("logged_in_user")
    }
}
