package com.softprodigy.ballerapp.data.response

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
    @SerializedName("birthdate") val birthdate: String = "",
    @SerializedName("id") val id: String = "",
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("teamDetails") val teamDetails: SnapshotStateList<TeamDetails> = mutableStateListOf(),
    @SerializedName("userDetails") val userDetails: UserDetails = UserDetails(),
    @SerializedName("parentDetails") var parentDetails: ArrayList<ParentDetails> = arrayListOf(),
    @SerializedName("age") val age: String = "",
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
    @SerializedName("perferredPartner") val perferredPartner: String = "",
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
    @SerializedName("logo") val logo: String = ""


)

data class UserDocType(
    @SerializedName("name") val name: String = "",
    @SerializedName("key") val key: String = "",
    @SerializedName("url") val url: String = "",
)

data class ParentDetails(

    @SerializedName("_id") val Id: String = "",
    @SerializedName("parent") val parent: Parent = Parent(),
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
    @SerializedName("id") val id: String = ""

)
