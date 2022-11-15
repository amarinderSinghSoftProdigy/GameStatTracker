package com.allballapp.android.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.allballapp.android.R
import com.allballapp.android.common.comet_chat.UIKitConstants
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
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.models.*
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
            var str = ""
            try {
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
                val firstDate: Date = inputFormat.parse(startTime)!!
                val lastDate: Date = inputFormat.parse(endTime)!!
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
            var str = ""
            try {
                val startTime = if (startDate.contains("T")) startDate.substring(
                    0,
                    startDate.indexOf("T")
                ) else startDate
                val inputPattern = "yyyy-MM-dd"
                val outputPattern = format.ifEmpty { "MMM dd, yyyy" }
                val inputFormat = SimpleDateFormat(inputPattern)
                val endFormat = SimpleDateFormat(outputPattern)
                val firstDate: Date = inputFormat.parse(startTime)!!
                str = endFormat.format(firstDate)
            } catch (e: ParseException) {

            }
            return str
        }


        @SuppressLint("SimpleDateFormat")
        fun formatDateRequest(startDate: Date): String {
            var str = ""
            try {
                val outputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                val endFormat = SimpleDateFormat(outputPattern)
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
            return "supporting_staff"
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


        fun getLastMessage(context: Context, lastMessage: BaseMessage): String? {
            var message: String? = null
            if (lastMessage.deletedAt == 0L) {
                when (lastMessage.category) {
                    CometChatConstants.CATEGORY_MESSAGE ->
                        if (lastMessage is TextMessage) {
                            if (isLoggedInUser(lastMessage.getSender()))
                                message =
                                    context.getString(R.string.you) + ": " + if (lastMessage.text == null) context.getString(
                                        R.string.this_message_deleted
                                    ) else lastMessage.text
                            else
                                message = lastMessage.getSender().name + ": " + lastMessage.text

                        } else if (lastMessage is MediaMessage) {
                            if (lastMessage.getDeletedAt() == 0L) {
                                if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_IMAGE) message =
                                    context.getString(R.string.message_image) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_VIDEO) message =
                                    context.getString(R.string.message_video) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_FILE) message =
                                    context.getString(R.string.message_file) else if (lastMessage.getType() == CometChatConstants.MESSAGE_TYPE_AUDIO) message =
                                    context.getString(R.string.message_audio)
                            } else message = context.getString(R.string.this_message_deleted)
                        }
                    CometChatConstants.CATEGORY_CUSTOM ->
                        message = if (lastMessage.deletedAt == 0L) {
                            if (lastMessage.type == UIKitConstants.IntentStrings.LOCATION) context.getString(
                                R.string.custom_message_location
                            ) else if (lastMessage.type == UIKitConstants.IntentStrings.POLLS) context.getString(
                                R.string.custom_message_poll
                            ) else if (lastMessage.type.equals(
                                    UIKitConstants.IntentStrings.STICKERS,
                                    ignoreCase = true
                                )
                            ) context.getString(R.string.custom_message_sticker) else if (lastMessage.type.equals(
                                    UIKitConstants.IntentStrings.WHITEBOARD,
                                    ignoreCase = true
                                )
                            ) context.getString(R.string.custom_message_whiteboard) else if (lastMessage.type.equals(
                                    UIKitConstants.IntentStrings.WRITEBOARD,
                                    ignoreCase = true
                                )
                            ) context.getString(R.string.custom_message_document) else if (lastMessage.type.equals(
                                    UIKitConstants.IntentStrings.MEETING,
                                    ignoreCase = true
                                )
                            ) context.getString(R.string.custom_message_meeting) else String.format(
                                context.getString(R.string.you_received),
                                lastMessage.type
                            )
                        } else context.getString(R.string.this_message_deleted)
//                    CometChatConstants.CATEGORY_ACTION -> message = (lastMessage as Action).message
                    CometChatConstants.CATEGORY_ACTION -> if (lastMessage is Action) {
                        if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_JOINED)
                            message =
                                (lastMessage.actioBy as User).name + " " + context.getString(R.string.joined)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED) message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.added) + " " + (lastMessage.actionOn as User).name)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_KICKED) message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.kicked_by) + " " + (lastMessage.actionOn as User).name)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_BANNED) message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.ban) + " " + (lastMessage.actionOn as User).name)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_UNBANNED) message =
                            ((lastMessage.actioBy as User).name + " "
                                    + context.getString(R.string.unban) + " " + (lastMessage.actionOn as User).name)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_LEFT) message =
                            (lastMessage.actioBy as User).name + " " + context.getString(R.string.left)
                        else if (lastMessage.action == CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED)
                            message =
                                if (lastMessage.newScope == CometChatConstants.SCOPE_MODERATOR) {
                                    ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                            + (lastMessage.actionOn as User).name + " " + context.getString(
                                        R.string.moderator
                                    ))
                                } else if (lastMessage.newScope == CometChatConstants.SCOPE_ADMIN) {
                                    ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                            + (lastMessage.actionOn as User).name + " " + context.getString(
                                        R.string.admin
                                    ))
                                } else if (lastMessage.newScope == CometChatConstants.SCOPE_PARTICIPANT) {
                                    ((lastMessage.actioBy as User).name + " " + context.getString(R.string.made) + " "
                                            + (lastMessage.actionOn as User).name + " " + context.getString(
                                        R.string.participant
                                    ))
                                } else lastMessage.message
                    }
                    CometChatConstants.CATEGORY_CALL ->
                        message = if ((lastMessage as Call).callStatus.equals(
                                CometChatConstants.CALL_STATUS_ENDED,
                                ignoreCase = true
                            ) ||
                            lastMessage.callStatus.equals(
                                CometChatConstants.CALL_STATUS_CANCELLED,
                                ignoreCase = true
                            )
                        ) {
                            if (lastMessage.getType()
                                    .equals(CometChatConstants.CALL_TYPE_AUDIO, ignoreCase = true)
                            ) context.getString(R.string.incoming_audio_call) else context.getString(
                                R.string.incoming_video_call
                            )
                        } else if (lastMessage.callStatus.equals(
                                CometChatConstants.CALL_STATUS_ONGOING,
                                ignoreCase = true
                            )
                        ) {
                            context.getString(R.string.ongoing_call)
                        } else if (lastMessage.callStatus.equals(
                                CometChatConstants.CALL_STATUS_CANCELLED,
                                ignoreCase = true
                            ) ||
                            lastMessage.callStatus.equals(
                                CometChatConstants.CALL_STATUS_UNANSWERED,
                                ignoreCase = true
                            ) ||
                            lastMessage.callStatus.equals(
                                CometChatConstants.CALL_STATUS_BUSY,
                                ignoreCase = true
                            )
                        ) {
                            if (lastMessage.getType()
                                    .equals(CometChatConstants.CALL_TYPE_AUDIO, ignoreCase = true)
                            ) context.getString(R.string.missed_voice_call) else context.getString(R.string.missed_video_call)
                        } else lastMessage.callStatus + " " + lastMessage.getType() + " Call"
                    else -> message = context.getString(R.string.tap_to_start_conversation)
                }
                return message
            } else return context.getString(R.string.this_message_deleted)
        }
        fun isLoggedInUser(user: User): Boolean {
            return user.uid == CometChat.getLoggedInUser().uid
        }

    }
}