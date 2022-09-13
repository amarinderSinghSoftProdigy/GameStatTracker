package com.softprodigy.ballerapp.ui.features.home.invitation


data class InvitationState(
    val invitations: ArrayList<InvitationDemoModel> = arrayListOf(),
    val roles: ArrayList<String> = arrayListOf("Project Manager", "Coach", "Assistance Coach", "Player", "Guardian"),
    val showDialog: Boolean = false,
    val showLoading: Boolean = false,
    val selectedRole:String="",

)