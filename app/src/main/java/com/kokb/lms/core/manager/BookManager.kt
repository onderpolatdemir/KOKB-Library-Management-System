package com.kokb.lms.core.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kokb.lms.features.books.domain.model.Book
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveBooks(books: List<Book>) {
        Log.d("BookManager", "Saving ${books.size} books to SharedPreferences")
        val booksJson = gson.toJson(books)
        prefs.edit().putString(KEY_BOOKS, booksJson).apply()
        Log.d("BookManager", "Books saved successfully")
    }

    fun getBooks(): List<Book> {
        val booksJson = prefs.getString(KEY_BOOKS, null)
        Log.d("BookManager", "Retrieved books JSON from SharedPreferences: ${booksJson?.take(100)}...")
        return if (booksJson != null) {
            try {
                val type = object : TypeToken<List<Book>>() {}.type
                val books = gson.fromJson<List<Book>>(booksJson, type)
                Log.d("BookManager", "Successfully parsed ${books.size} books from JSON")
                books
            } catch (e: Exception) {
                Log.e("BookManager", "Error parsing books from JSON: ${e.message}")
                emptyList()
            }
        } else {
            Log.d("BookManager", "No books found in SharedPreferences")
            emptyList()
        }
    }

    fun clearBooks() {
        Log.d("BookManager", "Clearing all books from SharedPreferences")
        prefs.edit().remove(KEY_BOOKS).apply()
    }

    fun updateBook(updatedBook: Book) {
        Log.d("BookManager", "Updating book with ID: ${updatedBook.id}")
        val currentBooks = getBooks().toMutableList()
        val index = currentBooks.indexOfFirst { it.id == updatedBook.id }
        
        if (index != -1) {
            currentBooks[index] = updatedBook
            saveBooks(currentBooks)
            Log.d("BookManager", "Book updated successfully")
        } else {
            Log.w("BookManager", "Book with ID: ${updatedBook.id} not found in stored books")
        }
    }

    companion object {
        private const val PREFS_NAME = "book_manager_prefs"
        private const val KEY_BOOKS = "books"
    }
} 