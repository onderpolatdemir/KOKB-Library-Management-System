package com.kokb.lms.features.books.data.model

import com.google.gson.annotations.SerializedName

data class UpdateBookRequest(
    @SerializedName("id")
    val id: String,
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
    val copies: Int,
    @SerializedName("status")
    val status: Int
)