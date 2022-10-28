package com.allballapp.android.data.response.team

import com.google.gson.annotations.SerializedName

data class Documents(
    @SerializedName("auuCard")
    val auuCard: String? = "",
    @SerializedName("birthCertificate")
    val birthCertificate: String? = "",
    @SerializedName("gradeVerfication")
    val gradeVerification: String? = "",
    @SerializedName("permissionSlip")
    val permissionSlip: String? = "",
    @SerializedName("vaccineCard")
    val vaccineCard: String? = "",
    @SerializedName("waiver")
    val waiver: String? = ""
)