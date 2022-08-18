package com.baller_app.core.util

import java.math.BigInteger
import java.security.MessageDigest

object HashUtils {

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun md5AsPart(input: String, size: Int): String{
        val allStr = md5(input)
        return if(allStr.length>size){
            allStr.substring(0, size)
        }else{
            allStr
        }
    }
}