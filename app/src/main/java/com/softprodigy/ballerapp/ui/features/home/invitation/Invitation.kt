package com.softprodigy.ballerapp.ui.features.home.invitation

import com.google.gson.annotations.SerializedName
import com.softprodigy.ballerapp.data.response.User
import com.softprodigy.ballerapp.data.response.team.Team

data class Invitation(
    @SerializedName("_id") val id: String = "",
    @SerializedName("memberId") val memberId: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("emailId") val emailId: String = "",
    @SerializedName("teamId") val team: Team = Team(),
    @SerializedName("status") val status: String = "",
    @SerializedName("createdBy") val createdBy: User = User(),
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("jersey") val jersey: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("role") val role: String = ""
)

enum class InvitationStatus(val status: String) {
    ACCEPT("Accept"),
    ACCEPTED("Accepted"),
    REJECT("Reject"),
    DECLINED("Declined"),
    PENDING("Pending")
}