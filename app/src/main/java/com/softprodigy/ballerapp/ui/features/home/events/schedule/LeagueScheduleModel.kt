package com.softprodigy.ballerapp.ui.features.home.events.schedule

import com.softprodigy.ballerapp.data.response.team.Team
import com.softprodigy.ballerapp.ui.features.home.events.Matches

data class LeagueScheduleModel(
    val date: String = "",
    val gameCount: String = "",
    val matches: List<Matches> = listOf()
)

data class Match(
    val teamA: Team = Team(),
    val teamB: Team = Team(),
    val location: String = "",
    val divisions: String = "",
    var time: String = "",
)