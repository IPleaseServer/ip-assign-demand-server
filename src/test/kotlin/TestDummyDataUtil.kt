import site.iplease.iadserver.global.common.data.type.AssignIpUsageType
import site.iplease.iadserver.global.common.data.type.DemandStatusType
import java.time.LocalDate
import kotlin.random.Random

object TestDummyDataUtil {
    fun reason() = listOf("날이 좋아서", "날이 좋지 않아서", "날이 좋아서", "그냥").random()
    fun title() = listOf("제목", "제목일거야", "제목일지도", "제목인걸까", "제목인거야").random()
    fun description() = listOf("내용", "내용일거야", "내용일지도", "내용인걸까", "내용인거야").random()
    fun errorMessage() = listOf("에러메시지", "에러메시지일거야", "에러메시지일지도", "에러메시지인걸까", "에러메시지인거야").random()
    fun assignIp() = listOf("127.0.0.1", "192.168.12.4", "10.28.87.61", "142.250.206.196").random()
    fun id() = Random.nextLong(1, Long.MAX_VALUE)
    fun demandStatus() = DemandStatusType.values().random()
    fun usage() = AssignIpUsageType.values().random()
    fun randomDate(): LocalDate {
        val minDay = LocalDate.MIN.toEpochDay()
        val maxDay = LocalDate.MAX.toEpochDay()
        val randomDay: Long = Random.nextLong(minDay, maxDay)
        return LocalDate.ofEpochDay(randomDay)
    }
}