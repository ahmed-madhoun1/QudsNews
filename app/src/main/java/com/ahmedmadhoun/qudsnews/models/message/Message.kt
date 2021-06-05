package com.ahmedmadhoun.qudsnews.models.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class Message(
    val id: String,
    val title: String,
    val message: String,
    val created: Long = System.currentTimeMillis()
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}