package com.allballapp.android.data.request

import com.google.gson.annotations.SerializedName

data class UpdateUserDetailsReq(
    @SerializedName("profileImage") val profileImage: String = "",
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("lastName") val lastName: String = "",
    @SerializedName("birthdate") val birthdate: String = "",
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("grade") val grade: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("teamDetails")
    val teamDetailsReq: List<TeamDetailsReq> = arrayListOf(),
    @SerializedName("userDetails")
    val userDetailsReq: UserDetailsReq = UserDetailsReq()
)

data class TeamDetailsReq(
    @SerializedName("teamId") val teamId: String = "",
    @SerializedName("role") val role: String? = "",
    @SerializedName("position") val position: String = "",
    @SerializedName("jersey") val jersey: String = ""
)

data class UserDetailsReq(

    @SerializedName("positionPlayed") val positionPlayed: List<String> = arrayListOf(),
    @SerializedName("jerseyPerferences") val jerseyPerferences: ArrayList<JerseyPerferencesReq> = arrayListOf(),
    @SerializedName("funFacts") val funFacts: ArrayList<FunFactsReq> = arrayListOf(),
    @SerializedName("classOf") val classOf: String = "",
    @SerializedName("birthCertificate") val birthCertificate: String = "",
    @SerializedName("gradeVerfication") val gradeVerfication: String = "",
    @SerializedName("permissionSlip") val permissionSlip: String = "",
    @SerializedName("auuCard") val auuCard: String = "",
    @SerializedName("auuCardNumber") val auuCardNumber: String = "",
    @SerializedName("waiver") val waiver: String = "",
    @SerializedName("vaccineCard") val vaccineCard: String = "",
    @SerializedName("aboutExperience") val aboutExperience: String = "",
    @SerializedName("perferredPartner") val perferredPartner: String = "",
    @SerializedName("refereeWithPartner") val refereeWithPartner: Boolean = false,
    @SerializedName("refereeningExperience") val refereeningExperience: String = "",
    @SerializedName("teamAgePerference") val teamAgePerference: String = "",
)

data class JerseyPerferencesReq(
    @SerializedName("jerseyNumberPerferences") val jerseyNumberPerferences: List<String> = arrayListOf(),
    @SerializedName("shirtSize") val shirtSize: String = "",
    @SerializedName("waistSize") val waistSize: String = ""
)

data class FunFactsReq(

    @SerializedName("favCollegeTeam") val favCollegeTeam: String = "",
    @SerializedName("favProfessionalTeam") val favProfessionalTeam: String = "",
    @SerializedName("favActivePlayer") val favActivePlayer: String = "",
    @SerializedName("favAllTimePlayer") val favAllTimePlayer: String = ""

)
