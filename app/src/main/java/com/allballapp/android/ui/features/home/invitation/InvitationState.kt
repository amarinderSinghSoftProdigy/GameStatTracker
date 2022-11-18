package com.allballapp.android.ui.features.home.invitation

import com.allballapp.android.data.response.PlayerDetails
import com.allballapp.android.data.response.UserRoles

data class InvitationState(
    val invitations: ArrayList<Invitation> = arrayListOf(),
    val roles: List<UserRoles> = mutableListOf(),
    val showRoleDialog: Boolean = false,
    val showDeclineDialog: Boolean = false,
    val showGuardianOnlyConfirmDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedRoleKey: String = "",
    val selectedInvitation: Invitation = Invitation(),
    val showGuardianDialog: Boolean = false,
    val showAddPlayerDialog: Boolean = false,
    val showPlayerAddedSuccessDialog: Boolean = false,
    val selectedGuardian: String = "",
    val teamId: String = "",
    val playerDetails: ArrayList<PlayerDetails> = arrayListOf(),
    val selectedPlayerId: String = "",
    val selectedPlayerIds: ArrayList<String> = arrayListOf(),
    val selectedGender: String = "",
    val selectedPlayerName: String = "",
    val selectedLogo: String = ""
)