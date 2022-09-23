package com.softprodigy.ballerapp.ui.features.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.softprodigy.ballerapp.data.response.JerseyPerferences
import com.softprodigy.ballerapp.data.response.User

data class ProfileState(
    val isLoading: Boolean = false,
    val showParentDialog: Boolean = false,
    val user: User = User(),
    val jerseyPerferences: SnapshotStateList<JerseyPerferences> = mutableStateListOf(
        JerseyPerferences()
    ),
)
