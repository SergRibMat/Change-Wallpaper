package sergio.ribera.testingenvironment

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

    @Test
    fun repetitionIntervalLongTest(){
        val milis: Long = repetitionIntervalLong()
        assertEquals(86400000L, milis)
    }

    fun timeChoice(): Long = 1L

    fun repetitionIntervalLong(): Long = (oneMinuteLong() * timeChoice()) * getTimeUnitToMultiply(1)


    fun getTimeUnitToMultiply(timeUnitInt: Int): Long = when(timeUnitInt) {
        0   -> 1L //minutes
        1   -> 60L * 24L //days
        2   -> 60L //hours
        else -> 60L //hours
    }

    fun oneMinuteLong(): Long = 60000L

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