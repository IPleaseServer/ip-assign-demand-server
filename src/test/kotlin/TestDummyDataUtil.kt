import java.time.LocalDate
import kotlin.random.Random

object TestDummyDataUtil {
    fun randomDate(): LocalDate {
        val minDay = LocalDate.MIN.toEpochDay()
        val maxDay = LocalDate.MAX.toEpochDay()
        val randomDay: Long = Random.nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }
}