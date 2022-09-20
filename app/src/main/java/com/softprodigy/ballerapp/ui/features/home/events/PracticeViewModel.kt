package com.softprodigy.ballerapp.ui.features.home.events

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.ui.features.home.events.division.DivisionUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _practiceUiState = mutableStateOf(PracticeUIState())
    val practiceUiState: State<PracticeUIState> = _practiceUiState

    fun onEvent(event: PracticeUIEvent) {
        when (event) {

            is PracticeUIEvent.OnDateChanged -> {
                _practiceUiState.value = _practiceUiState.value.copy(selectedDate = event.date)
            }

            is PracticeUIEvent.OnArrivalTimeChanged -> {
                _practiceUiState.value =
                    _practiceUiState.value.copy(selectedArrivalTime = event.time)

            }

            is PracticeUIEvent.OnStartTimeChanged -> {
                _practiceUiState.value = _practiceUiState.value.copy(selectedStartTime = event.time)

            }

            is PracticeUIEvent.OnEndTimeChanged -> {
                _practiceUiState.value = _practiceUiState.value.copy(selectedEndTime = event.time)

            }

            is PracticeUIEvent.OnLocationChanged -> {
                _practiceUiState.value =
                    _practiceUiState.value.copy(selectedLocation = event.location)

            }

            is PracticeUIEvent.OnAddressChanged -> {
                _practiceUiState.value =
                    _practiceUiState.value.copy(selectedAddress = event.address)
            }
        }
    }
}