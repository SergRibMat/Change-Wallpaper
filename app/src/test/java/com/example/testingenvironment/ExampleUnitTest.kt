package com.example.testingenvironment

import android.util.Log
import androidx.work.ListenableWorker
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

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

    @Test
    fun getTimeUnitTest(){
        val x = getTimeUnit("Minutes")
        //assertEquals(TimeUnit.MINUTES, x)
        if (x is TimeUnit){
            ""
        }else{
            ""
        }
    }

    fun getTimeUnit(timeUnitString: String) = when(timeUnitString) {
        "Minutes"   -> TimeUnit.MINUTES
        "Days"      -> TimeUnit.DAYS
        else -> TimeUnit.HOURS
    }

    @Test
    fun hola() {
        val albumName = null
        var x = 1
        val firstCondition = albumName != null
        val secondCondition = albumName != "Empty"
        if (firstCondition && secondCondition) {
            assertEquals(2, x)
        } else {
            assertEquals(1, x)
        }
    }
}