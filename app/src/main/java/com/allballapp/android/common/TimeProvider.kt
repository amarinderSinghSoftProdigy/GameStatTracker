package com.allballapp.android.common

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun get24HoursTimeWithAMPM(time: String): String {
    val _24HourSDF = SimpleDateFormat("HH:mm")
    val _12HourSDF = SimpleDateFormat("hh:mm a")
    val _24HourDt: Date = _24HourSDF.parse(time)!!

    return _12HourSDF.format(_24HourDt).toUpperCase()
}

fun checkTimings(time: String, endTime: String): Boolean {
    val pattern = "HH:mm"
    val sdf = SimpleDateFormat(pattern)
    try {
        val date1 = sdf.parse(time)
        val date2 = sdf.parse(endTime)
        return if(date2.after(date1)) {
            true
        } else {
            false
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return false
}
