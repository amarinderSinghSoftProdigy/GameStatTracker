package com.allballapp.android.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.allballapp.android.R
import com.allballapp.android.data.response.AllUser
import com.allballapp.android.data.response.UserRoles
import com.allballapp.android.data.response.team.Player
import com.allballapp.android.data.response.team.Team
import com.allballapp.android.data.response.team.TeamLeaderBoard
import com.allballapp.android.data.response.team.TeamParent
import com.allballapp.android.ui.features.components.UserType
import com.allballapp.android.ui.features.components.getRoleList
import com.allballapp.android.ui.features.home.events.FilterPreference
import com.allballapp.android.ui.features.home.events.Participation
import com.google.android.gms.maps.model.LatLng
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

        fun getUsersList(list: ArrayList<AllUser>, key: UserType): ArrayList<AllUser> {
            val listing = arrayListOf<AllUser>()
            if (key == UserType.PARENT) {
                for (item in list) {
                    if (UserType.PROGRAM_STAFF.key != item.role && UserType.COACH.key != item.role && UserType.PLAYER.key != item.role) {
                        listing.add(item)
                    }
                }
            } else {
                for (item in list) {
                    if (key.key == item.role) {
                        listing.add(item)
                    }
                }
            }
            return listing
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
            val result: LinkedHashMap<String, ArrayList<FilterPreference>> = LinkedHashMap()
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
                firstDate = inputFormat.parse(startTime)!!
                lastDate = inputFormat.parse(endTime)!!
                str = startFormat.format(firstDate) + " - " + endFormat.format(lastDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun getDateString(startDate: String): String {
            return if (startDate.contains("T")) startDate.substring(
                0,
                startDate.indexOf("T")
            ) else startDate
        }


        @SuppressLint("SimpleDateFormat")
        fun formatDateSingle(startDate: String, format: String = ""): String {
            val startTime =
                if (startDate.contains("T")) startDate.substring(
                    0,
                    startDate.indexOf("T")
                ) else startDate
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = format.ifEmpty { "MMM dd, yyyy" }
            val inputFormat = SimpleDateFormat(inputPattern)
            val endFormat = SimpleDateFormat(outputPattern)

            val firstDate: Date
            var str = ""

            try {
                firstDate = inputFormat.parse(startTime)!!
                str = endFormat.format(firstDate)
            } catch (e: ParseException) {

            }
            return str
        }


        @SuppressLint("SimpleDateFormat")
        fun formatDateRequest(startDate: Date): String {
            val outputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val endFormat = SimpleDateFormat(outputPattern)
            var str = ""
            try {
                str = endFormat.format(startDate)
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
                if (item.teamId != null)
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

        fun getUserRole(role: String, list: List<UserRoles>): UserRoles {
            for (item in list) {
                if (item.key == role) {
                    return item
                }
            }
            return UserRoles()
        }

        fun openMaps(context: Context, location: LatLng) {
            val gmmIntentUri: Uri =
                Uri.parse("geo:" + location.latitude + "," + location.longitude + "?q=loc:" + location.latitude + "," + location.longitude + " (" + "Location" + ")")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }
    }
}