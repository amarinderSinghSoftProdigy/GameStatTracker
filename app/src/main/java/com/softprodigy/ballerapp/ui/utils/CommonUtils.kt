package com.softprodigy.ballerapp.ui.utils

import com.softprodigy.ballerapp.data.response.team.Player

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
    }
}