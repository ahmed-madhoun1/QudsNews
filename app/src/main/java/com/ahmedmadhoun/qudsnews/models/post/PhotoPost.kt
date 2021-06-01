package com.ahmedmadhoun.qudsnews.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class PhotoPost(
    val id: String,
    val photo: String,
    val title: String,
    val article: String,
    val author: String,
    val created: Long = System.currentTimeMillis()
) : Parcelable {

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}
