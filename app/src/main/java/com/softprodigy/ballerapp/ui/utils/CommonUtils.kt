package com.softprodigy.ballerapp.ui.utils

import android.annotation.SuppressLint
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommonUtils {
    companion object {
        fun getPlayerTabs(list: ArrayList<Player>): ArrayList<Player> {
            var count = 0
            for (item in list) {
                item.uniqueId = count
                count += 1
            }
            return list
        }

        fun getSelectedList(teamLeaderBoard: List<TeamLeaderBoard>): Boolean {
            var count = 0
            for (item in teamLeaderBoard) {
                if (item.status) {
                    count += 1
                }
            }
            return count == teamLeaderBoard.size
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDate(startDate: String, endDate: String): String {
            val startTime =
                if (startDate.contains("T")) startDate.substring(
                    0,
                    startDate.indexOf("T")
                ) else startDate
            val endTime =
                if (endDate.contains("T")) endDate.substring(
                    0,
                    endDate.indexOf("T")
                ) else endDate
            val inputPattern = "yyyy-MM-dd"
            val outputStartPattern = "MMM dd"
            val outputPattern = "MMM dd, yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)

            val startFormat = SimpleDateFormat(outputStartPattern)
            val endFormat = SimpleDateFormat(outputPattern)

            val firstDate: Date
            val lastDate: Date
            var str = ""

            try {
                firstDate = inputFormat.parse(startTime)
                lastDate = inputFormat.parse(endTime)
                str = startFormat.format(firstDate) + " - " + endFormat.format(lastDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDateSingle(startDate: String): String {
            val startTime =
                if (startDate.contains("T")) startDate.substring(
                    0,
                    startDate.indexOf("T")
                ) else startDate
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "MMM dd, yyyy"
            val inputFormat = SimpleDateFormat(inputPattern)
            val endFormat = SimpleDateFormat(outputPattern)

            val firstDate: Date
            var str = ""

            try {
                firstDate = inputFormat.parse(startTime)
                str = endFormat.format(firstDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }
    }
}