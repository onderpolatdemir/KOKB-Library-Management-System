package com.kokb.lms.features.books.data.model

import com.google.gson.annotations.SerializedName

data class AddBookRequest(
    @SerializedName("isbn")
    val isbn: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("publishDate")
    val publishDate: String,
    @SerializedName("copies")
    val copies: Int
) 