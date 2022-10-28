package com.allballapp.android.data.request

import android.net.Uri

sealed class GlobalRequest {

    data class Users(val role: String = "") : GlobalRequest()

    data class SetupProfile(
        val image: Uri ?= null,
        val fName: String = "",
        val lName: String = "",
        val email: String = "",
        val phoneNumber: String = ""
    ) : GlobalRequest()

    data class SetUpTeam(
        val teamName: String = "",
        val teamLogo: Uri ?= null,
        val teamColor: String = ""
    ) :
        GlobalRequest()

    data class AddPlayers(val search: String = "") : GlobalRequest()

}