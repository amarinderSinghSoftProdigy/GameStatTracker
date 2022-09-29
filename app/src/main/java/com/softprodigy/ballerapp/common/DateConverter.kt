package com.softprodigy.ballerapp.common

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/* Convert type "2022-09-20T02:30:00.000Z" date to type "Tue, Sep 01"*/
fun apiToUIDateFormat(apiDate: String): String {
    if(apiDate.isNotEmpty()){
    val originalFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("EEE, MMM dd") //Tue, Sep 22
    val date: Date = originalFormat.parse(apiDate)
    val formattedDate: String = targetFormat.format(date)
    Log.i("apiToUIDateFormat", "apiToUIDateFormat: $formattedDate")
    return formattedDate
    }
    else return apiDate
}

/* Convert type "2022-09-20T02:30:00.000Z" date to type "May 15, 1999"*/
fun apiToUIDateFormat2(apiDate: String): String {
    if(apiDate.isNotEmpty()){
        val originalFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy") //May 15, 1999
        val date: Date = originalFormat.parse(apiDate)
        val formattedDate: String = targetFormat.format(date)
        Log.i("apiToUIDateFormat2", "apiToUIDateFormat: $formattedDate")
        return formattedDate
    }
    else return apiDate
}