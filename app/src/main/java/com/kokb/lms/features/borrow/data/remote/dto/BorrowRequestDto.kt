package com.kokb.lms.features.borrow.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID
 
data class BorrowRequestDto(
    @SerializedName("UserId")
    val userId: String,
    @SerializedName("BookId")
    val bookId: UUID,
    @SerializedName("DayCount")
    val dayCount: Int
) 