package site.iplease.iadserver.domain.common.strategy

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import kotlin.random.Random

class CancelDemandStrategyImplTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var demandConverter: DemandConverter
    private lateinit var target: CancelDemandStrategyImpl

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        demandConverter = mock()
        target = CancelDemandStrategyImpl(demandRepository, demandConverter)
    }

    //cancelDemand 로직
    //예약 엔티티를 추출한다.
    //예약 정보를 추출한다.
    //예약 엔티티를 삭제한다.
    //예약 정보를 반환한다.

    @Test @DisplayName("IP할당예약취소 성공 테스트")
    fun testCancelDemandSuccess() {
        //예약취소가 성공하였다면,취소한 예약정보를 반환한다.
        //예약취소가 성공하려면, 아래의 작업이 성공해야한다.
        //- 예약취소 트랜잭션 수행 (예약정보 제거)

        //given
        val demandId = Random.nextLong()
        val entity = mock<Demand>()
        val dto = mock<DemandDto>()

        //when
        whenever(demandRepository.findByIdentifier(demandId)).thenReturn(entity.toMono())
        whenever(demandConverter.toDto(entity)).thenReturn(dto.toMono())
        whenever(demandRepository.deleteByIdentifier(demandId)).thenReturn(Unit.toMono().then())

        val result = target.cancelDemand(demandId).block()!!

        //then
        assertEquals(result, dto)
        verify(demandRepository, times(1)).deleteByIdentifier(demandId)
    }
}