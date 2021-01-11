package com.example.testingenvironment

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {//OK
        val albumSize = "2"
        val bool = albumSize.toInt() > 0
        assertEquals(true, bool)
    }
}