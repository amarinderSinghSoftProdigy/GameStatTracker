package com.allballapp.android.ui.features.select_profile

sealed class SelectProfileUIEvent {

    data class IsSelectedRole(val role: String) : SelectProfileUIEvent()

}