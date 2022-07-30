import java.time.LocalDate
import kotlin.random.Random

object TestUtil {
    fun randomPastDate(): LocalDate {
        val minDay = LocalDate.of(1970, 1, 1).toEpochDay()
        val maxDay = now().toEpochDay()
        val randomDay: Long = Random.nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }

    fun now(): LocalDate = LocalDate.now()
}