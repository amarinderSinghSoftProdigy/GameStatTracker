package com.cometchat.pro.uikit.ui_resources.utils

import android.content.Context
import android.graphics.Typeface

public class FontUtils {


    private var context: Context? = null
    companion object {
        private var _instance: FontUtils? = null

        val robotoMedium = "rubik_medium_500.ttf"

        val robotoBlack = "rubik_black_900.ttf"

        val robotoRegular = "rubik_regular_400.ttf"

        val robotoBold = "rubik_bold_700.ttf"

        val robotoLight = "rubik_light_300.ttf"

        val robotoThin = "rubik_light_300.ttf"

//         var context: Context? = null

        fun getInstance(context: Context?): FontUtils {
            if (_instance == null) {
                _instance = FontUtils(context!!)
            }
            return _instance!!
        }



    }
    fun getTypeFace(fontName: String?): Typeface? {
        var typeface: Typeface? = null
        if (context != null) typeface = Typeface.createFromAsset(context!!.assets, fontName)
        return typeface
    }
    constructor(context: Context) {
        this.context = context
    }
}