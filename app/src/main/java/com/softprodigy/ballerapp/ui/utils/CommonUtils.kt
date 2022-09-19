package com.softprodigy.ballerapp.ui.utils

import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard

class CommonUtils {
    companion object {
        fun getPlayerTabs(list: ArrayList<Player>): ArrayList<Player> {
            var count = 0
            for (item in list) {
                item.uniqueId = count
                count += 1
            }
            return list
        }

        fun getSelectedList(teamLeaderBoard: List<TeamLeaderBoard>): Boolean {
            var count = 0
            for (item in teamLeaderBoard) {
                if (item.status) {
                    count += 1
                }
            }
            return count == teamLeaderBoard.size
        }
    }
}