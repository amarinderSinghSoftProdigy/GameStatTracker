package com.softprodigy.ballerapp.ui.features.home.home_screen

sealed class HomeScreenEvent {
    object OnPendingInvitationClick : HomeScreenEvent()
}