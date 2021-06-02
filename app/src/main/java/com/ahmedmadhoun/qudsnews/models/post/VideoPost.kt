package com.ahmedmadhoun.qudsnews.data

import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class VideoPost(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val created: Long = System.currentTimeMillis()
) : Parcelable {

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}