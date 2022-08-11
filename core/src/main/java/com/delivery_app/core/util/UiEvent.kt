package com.delivery_app.core.util

sealed class UiEvent {
    object Success : UiEvent()
    object GenerateOtp : UiEvent()
    object NavigateUp : UiEvent()
    object NeedVerify : UiEvent()
    data class ShowToast(val message: UiText) : UiEvent()
}