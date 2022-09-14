package com.softprodigy.ballerapp.ui.utils

import com.softprodigy.ballerapp.data.response.team.Player
import timber.log.Timber

class CommonUtils {
    companion object {
        fun getPlayerTabs(list: ArrayList<Player>): ArrayList<Player> {
            var result = ArrayList<String>()
            var relatedItems = ArrayList<Player>()
            for (item in list) {
                if (!result.contains(item.position)) {
                    result.add(item.position)
                }
            }

            for (item in result) {
                relatedItems.add(Player(_id = item))
                for (items in list) {
                    if (item == items.position && !relatedItems.contains(items)) {
                        relatedItems.add(items)
                    }
                }
            }
            Timber.e("lsit " + result)
            return relatedItems
        }
    }
}