package com.softprodigy.ballerapp.common

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun apiToUIDateFormat(apiDate: String): String {
    val originalFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("EEE, MMM dd") //Tue, Sep 22
    val date: Date = originalFormat.parse(apiDate)
    val formattedDate: String = targetFormat.format(date)
    Log.i("apiToUIDateFormat", "apiToUIDateFormat: $formattedDate")
    return formattedDate
}