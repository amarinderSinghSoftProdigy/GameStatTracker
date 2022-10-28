package com.allballapp.android.ui.features.home.event_kd.schedule

import com.allballapp.android.data.response.team.Team

data class LeagueScheduleModel(
    val date: String = "",
    val gameCount: String = "",
    val matches:List<Match> = listOf()
    )

data class Match(
    val teamA: Team = Team(),
    val teamB: Team = Team(),
    val location: String = "",
    val divisions: String = "",
    val time:String="",
)