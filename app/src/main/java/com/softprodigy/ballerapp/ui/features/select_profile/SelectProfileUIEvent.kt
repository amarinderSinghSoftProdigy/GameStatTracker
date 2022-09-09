package com.softprodigy.ballerapp.ui.features.select_profile

sealed class SelectProfileUIEvent {

    data class IsSelectedRole(val role: String) : SelectProfileUIEvent()

}