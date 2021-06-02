package com.ahmedmadhoun.qudsnews.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Parcelize
data class TextPost(
    val id: String,
    val title: String,
    val description: String,
    val author: String,
    val created: Long = System.currentTimeMillis()
) : Parcelable {

    constructor() : this("", "", "", "", 0)

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}
