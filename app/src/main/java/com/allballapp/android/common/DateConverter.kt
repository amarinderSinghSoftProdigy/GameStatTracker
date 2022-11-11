package com.allballapp.android.common

import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/* Convert type "2022-09-20T02:30:00.000Z" date to type "Tue, Sep 01"*/
fun apiToUIDateFormat(apiDate: String): String {
    if (apiDate.isNotEmpty()) {
        val originalFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH) //Tue, Sep 22
        val date: Date = originalFormat.parse(apiDate) ?: Date()
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    } else return apiDate
}

/* Convert type "2022-09-20T02:30:00.000Z" date to type "May 15, 1999"*/
fun apiToUIDateFormat2(apiDate: String): String {
    if (apiDate.isNotEmpty()) {
        val originalFormat: DateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val targetFormat: DateFormat =
            SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH) //May 15, 1999
        val date: Date = originalFormat.parse(apiDate) ?: Date()
        val formattedDate: String = targetFormat.format(date)
        return formattedDate
    } else return apiDate
}

/* Convert type "2022-09-20T02:30:00.000Z" date to Date object*/
fun convertServerUtcDateToLocal(serverUtcDate: String): Date? {
    if (serverUtcDate.isNotEmpty()) {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            sdf.timeZone =
                TimeZone.getTimeZone("UTC") /*considering server time in UTC, so it will convert server time zone to local time zone*/
            val date = sdf.parse(serverUtcDate)
            Timber.i("getDateFromString--$date")

            return date
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        return null
    }
}

/* Convert type "May 15, 1999 5:45:22 PM" date, to Date object*/
fun uiToAPiDate(uiDate: String): Date? {
    if (uiDate.trim().isNotEmpty()) {
        return try {
            val originalFormat: DateFormat =
                SimpleDateFormat("MMM dd, yyyy hh:mm aa", Locale.getDefault())
            val targetFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date? = originalFormat.parse(uiDate)
            date?.let {
                val formattedDate: String = targetFormat.format(date)
                Timber.i("formattedDate-- $formattedDate")
                date
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        return null
    }
}
/* Convert type "2022-09-20T02:30:00.000Z" date to long*/

fun convertStringDateToLong(stringDate: String): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date: Date? = formatter.parse(stringDate)
    val timeInMillis= date?.time ?: 0
    Timber.i("timeInMillis--$timeInMillis")
    return  timeInMillis
}