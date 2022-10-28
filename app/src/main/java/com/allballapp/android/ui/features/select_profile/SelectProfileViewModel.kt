package com.allballapp.android.ui.features.select_profile

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectProfileViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _selectProfileUiState = mutableStateOf(SelectProfileUIState())
    val selectProfileUiState: State<SelectProfileUIState> = _selectProfileUiState

    fun onEvent(event: SelectProfileUIEvent) {
        when (event) {
            is SelectProfileUIEvent.IsSelectedRole -> {

                _selectProfileUiState.value =
                    _selectProfileUiState.value.copy(isSelectedRole = event.role)
            }
        }
    }

    init {
        getProfileData()
    }

    private fun getProfileData() {

    }
}