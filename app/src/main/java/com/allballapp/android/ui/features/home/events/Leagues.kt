package com.allballapp.android.ui.features.home.events

import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName

data class Leagues(
    @SerializedName("_id") val id: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("desc") val desc: String = "",
    @SerializedName("logo") val logo: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("color") val color: Color? = null,
)

enum class LeaguesTypes(val status: String) {
    LEAGUE("League"),
    TOURNAMENT("Tournament"),
}