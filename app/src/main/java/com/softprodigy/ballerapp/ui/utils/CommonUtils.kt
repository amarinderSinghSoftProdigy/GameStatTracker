package com.softprodigy.ballerapp.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard
import com.softprodigy.ballerapp.data.response.team.TeamParent
import com.softprodigy.ballerapp.ui.features.components.getRoleList
import com.softprodigy.ballerapp.ui.features.home.events.FilterPreference
import com.softprodigy.ballerapp.ui.features.home.events.Participation
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

        fun getFilterMap(teamLeaderBoard: List<FilterPreference>): HashMap<String, ArrayList<FilterPreference>> {
            val result: HashMap<String, ArrayList<FilterPreference>> = HashMap()
            val keys: ArrayList<String> = ArrayList()
            for (item in teamLeaderBoard) {
                if (!keys.contains(item.key))
                    keys.add(item.key)
            }
            for (item in keys) {
                val data: ArrayList<FilterPreference> = ArrayList()
                for (it in teamLeaderBoard)
                    if (item == it.key) {
                        data.add(it)
                    }
                result.put(item, data)
            }
            return result
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

            }
            return str
        }

        fun getParticipation(data: Participation, context: Context): String {
            val check = if (data.boysMax.isEmpty() && data.boysMin.isEmpty()) {
                context.getString(R.string.male)
            } else if (data.girlsMax.isEmpty() && data.girlsMin.isEmpty()) {
                context.getString(R.string.female)
            } else {
                context.getString(R.string.all)
            }
            val result = "$check, " + when (check) {
                "Male" -> {
                    data.boysMin + " - " + data.boysMax
                }
                "Female" -> {
                    data.girlsMin + " - " + data.girlsMax
                }
                "All" -> {
                    "M - " + data.boysMin + " - " + data.boysMax + " , " + "F - " + data.girlsMin + " - " + data.girlsMax
                }
                else -> {
                    ""
                }
            }
            return result
        }

        fun getTeams(data: ArrayList<TeamParent>): ArrayList<Team> {
            val result: ArrayList<Team> = ArrayList()
            for (item in data) {
                result.add(item.teamId)
            }
            return result
        }

        fun getRole(role: String): String {
            val list = getRoleList()
            for (item in list) {
                if (item.key == role) {
                    return item.stringId
                }
            }
            return "user_type"
        }

        fun openMaps(context: Context, location: LatLng) {
            val gmmIntentUri: Uri =
                Uri.parse("geo:" + location.latitude + "," + location.longitude + "?q=")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }
    }
}