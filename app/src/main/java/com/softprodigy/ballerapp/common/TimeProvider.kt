package com.softprodigy.ballerapp.common

import android.annotation.SuppressLint
import androidx.compose.ui.text.toUpperCase
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun get24HoursTimeWithAMPM(time: String): String {
    val _24HourSDF = SimpleDateFormat("HH:mm")
    val _12HourSDF = SimpleDateFormat("hh:mm a")
    val _24HourDt: Date = _24HourSDF.parse(time)!!

    return _12HourSDF.format(_24HourDt).toUpperCase()
}