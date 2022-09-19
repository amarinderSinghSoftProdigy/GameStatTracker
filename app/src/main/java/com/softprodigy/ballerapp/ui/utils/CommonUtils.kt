package com.softprodigy.ballerapp.ui.utils

import com.softprodigy.ballerapp.data.response.team.Player
import com.softprodigy.ballerapp.data.response.team.TeamLeaderBoard

class CommonUtils {
    companion object {
        fun getPlayerTabs(list: ArrayList<Player>): ArrayList<Player> {
            val result = ArrayList<String>()
            val relatedItems = ArrayList<Player>()
            for (item in list) {
                if (!result.contains(item.position)) {
                    result.add(item.position)
                }
            }
            for (item in result) {
                relatedItems.add(Player(_id = item, locked = true))
                for (items in list) {
                    if (item == items.position && !relatedItems.contains(items)) {
                        relatedItems.add(items)
                    }
                }
            }
            return relatedItems
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