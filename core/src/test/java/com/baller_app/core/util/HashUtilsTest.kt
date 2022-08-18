package com.baller_app.core.util

import org.junit.Assert.*

import org.junit.Test

class HashUtilsTest {

    @Test
    fun md5() {
        assertEquals("8b1a9953c4611296a827abf8c47804d7", HashUtils.md5("Hello"))
    }

    @Test
    fun md5AsPart(){
        assertEquals("8b1a99", HashUtils.md5AsPart("Hello",6))
    }
}