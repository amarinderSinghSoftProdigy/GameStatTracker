package com.softprodigy.ballerapp.ui.features.home.events.division

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.softprodigy.ballerapp.data.response.division.DivisionData
import com.softprodigy.ballerapp.data.response.division.DivisionTabs
import com.softprodigy.ballerapp.ui.features.home.teams.TeamUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DivisionViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    private val _divisionUiState = mutableStateOf(DivisionUIState())
    val divisionUiState: State<DivisionUIState> = _divisionUiState

    init {
        initDivision()
    }

    private fun initDivision() {
        val divisionTab = ArrayList<DivisionTabs>()
        divisionTab.add(DivisionTabs("All"))
        divisionTab.add(DivisionTabs("Male"))
        divisionTab.add(DivisionTabs("Female"))

        val divisionData = ArrayList<DivisionData>()
        divisionData.add(DivisionData("Boys 3rd"))
        divisionData.add(DivisionData("Boys 7th"))
        divisionData.add(DivisionData("Boys 8th"))
        divisionData.add(DivisionData("Boys 9th"))
        divisionData.add(DivisionData("Boys JV"))
        divisionData.add(DivisionData("Boys Varsity"))
        divisionData.add(DivisionData("Girls 8th"))

        _divisionUiState.value =
            _divisionUiState.value.copy(divisionData = divisionData, divisionTab = divisionTab)
    }

}