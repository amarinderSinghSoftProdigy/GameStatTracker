package com.softprodigy.ballerapp.ui.features.home.events.division

import com.softprodigy.ballerapp.data.response.division.DivisionData
import com.softprodigy.ballerapp.data.response.division.DivisionTabs

data class DivisionUIState(
    val isLoading: Boolean = false,
    val divisionTab: List<DivisionTabs> = emptyList(),
    val divisionData: List<DivisionData> = emptyList()
)