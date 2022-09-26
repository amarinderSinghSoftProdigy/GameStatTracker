package com.softprodigy.ballerapp.ui.features.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.softprodigy.ballerapp.data.response.JerseyPerferences
import com.softprodigy.ballerapp.data.response.User

data class ProfileState(
    val isLoading: Boolean = false,
    val showParentDialog: Boolean = false,
    val user: User = User(),
    val jerseyNumerPerferences:String = "",
    val shirtSize:String = "",
    val waistSize:String = "",

    val favCollegeTeam:String = "",
    val favProfessionalTeam:String = "",
    val favActivePlayer:String = "",
    val favAllTimePlayer:String = "",


)
