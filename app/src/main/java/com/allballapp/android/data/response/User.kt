package com.allballapp.android.data.response

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val _Id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("phoneVerified") val phoneVerified: Boolean = false,
    @SerializedName("name") val name: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("gender") val gender: String = "",
    @SerializedName("birthdate") val birthdate: String? = null,
    @SerializedName("id") val id: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("teamDetails") val teamDetails: SnapshotStateList<TeamDetails> = mutableStateListOf(),
    @SerializedName("userDetails") var userDetails: UserDetails = UserDetails(),
    @SerializedName("parentDetail") var parentDetails: ArrayList<Parent> = arrayListOf(),
    @SerializedName("kidDetail") val kidDetails: ArrayList<Parent> = arrayListOf(),
    @SerializedName("age") val age: Int? = null,
    @SerializedName("totalGames") val totalGames: Int? = 0,
    @SerializedName("totalHoopGames") val totalHoopsGames: Int? = 0,
    @SerializedName("rating") val rating: Double? = 0.0,
)

data class SaveUser(
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("loginType") val loginType: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
    @SerializedName("refreshTokenExpireAt") val refreshTokenExpireAt: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("phoneVerified") val phoneVerified: Boolean = false,
    @SerializedName("isDelete") val isDelete: Boolean = false,
    @SerializedName("otp") val otp: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("_id") val _id: String = "",
    @SerializedName("__v") val __v: String = "",
)

data class SwapUser(
    @SerializedName("_id") val _Id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("status") val status: String = "",
)

data class ProfileList(
    @SerializedName("profiles") val profiles: ArrayList<SwapUser> = ArrayList(),
    @SerializedName("isAuthorised") val isAuthorised: Boolean = false,
)

data class TeamDetails(

    @SerializedName("_id") val Id: String = "",
    @SerializedName("teamId") val teamId: TeamId = TeamId(),
    @SerializedName("status") val status: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("jersey") val jersey: String = "",
    @SerializedName("role") val role: String = ""
)

data class UserDetails(

    @SerializedName("_id") val Id: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("__v") val _v: String = "",
    @SerializedName("aboutExperience") val aboutExperience: String = "",
    @SerializedName("auuCardNumber") val auuCardNumber: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("funFacts") val funFacts: ArrayList<FunFacts> = arrayListOf(),
    @SerializedName("grade") val grade: String = "",
    @SerializedName("jerseyPerferences") val jerseyPerferences: SnapshotStateList<JerseyPerferences> = mutableStateListOf(),
    @SerializedName("perferredPartner") val preferredPartner: PerferredPartner? = null,
    @SerializedName("positionPlayed") val positionPlayed: ArrayList<String> = arrayListOf(),
    @SerializedName("refereeWithPartner") val refereeWithPartner: Boolean = false,
    @SerializedName("refereeningExperience") val refereeningExperience: String = "",
    @SerializedName("teamAgePerference") val teamAgePerference: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
    @SerializedName("birthCertificate") val birthCertificate: String = "",
    @SerializedName("gradeVerfication") val gradeVerfication: String = "",
    @SerializedName("permissionSlip") val permissionSlip: String = "",
    @SerializedName("auuCard") val auuCard: String = "",
    @SerializedName("waiver") val waiver: String = "",
    @SerializedName("vaccineCard") val vaccineCard: String = "",
    @SerializedName("classOf") val classOf: String = "",
)

data class PerferredPartner(
    @SerializedName("_id") val id: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("name") val name: String = ""
)

data class FunFacts(

    @SerializedName("favCollegeTeam") val favCollegeTeam: String = "",
    @SerializedName("favProfessionalTeam") val favProfessionalTeam: String = "",
    @SerializedName("favActivePlayer") val favActivePlayer: String = "",
    @SerializedName("favAllTimePlayer") val favAllTimePlayer: String = ""
)

data class JerseyPerferences(

    @SerializedName("jerseyNumberPerferences") val jerseyNumberPerferences: SnapshotStateList<String> = mutableStateListOf(),
    @SerializedName("shirtSize") val shirtSize: String = "",
    @SerializedName("waistSize") val waistSize: String = "",
    @SerializedName("gender") val gender: String = ""

)

data class TeamId(

    @SerializedName("_id") val Id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("primaryTeamColor") val primaryTeamColor: String = "",
    @SerializedName("secondaryTeamColor") val secondaryTeamColor: String = "",
    @SerializedName("tertiaryTeamColor") val tertiaryTeamColor: String = ""

)

data class UserRoles(
    @SerializedName("value") val value: String = "",
    @SerializedName("key") val key: String = "",
)

data class UserDocType(
    @SerializedName("name") val name: String = "",
    @SerializedName("key") val key: String = "",
    @SerializedName("url") val url: String = "",
)

data class ParentDetails(

    @SerializedName("_id") val Id: String = "",
    @SerializedName("parent") val parent: Parent? = null,
    @SerializedName("parentType") val parentType: String = ""

)

data class Parent(

    @SerializedName("_id") val _id: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("parentType") val parentType: String = ""

)

data class AllUser(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("role") val role: String = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("jersey") val jersey: String = ""
)


data class AddProfileRequest(
    val firstName: String = "",
    val lastName: String = "",
    val profileImage: String? = "",
    val email: String? = ""
//    val address: String = "",
//    val city: String = "",
//    val state: String = "",
//    val zip: String = "",
//    val gender: String = "",
//    val birthdate: String = "",
//    val role: String? = "",
//    val profileImageUri: String? = null,
)

data class StaffScheduleRequest(
    val userId: String = "",
    val schedule: ArrayList<ScheduleObject> = ArrayList()
)

data class ScheduleObject(
    val userId: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: String = "",
    val description: String = "",
)

data class ScheduleResponseObject(
    @SerializedName("_id") val _id: String = "",
    @SerializedName("__v") val __v: String = "",
    @SerializedName("userId") val userId: String = "",
    @SerializedName("startTime") val startTime: String = "",
    @SerializedName("endTime") val endTime: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("updatedAt") val updatedAt: String = "",
)