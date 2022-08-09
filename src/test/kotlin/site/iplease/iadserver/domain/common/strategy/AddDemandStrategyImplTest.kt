package site.iplease.iadserver.domain.common.strategy

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandSaver
import site.iplease.iadserver.global.common.util.DemandConverter

class AddDemandStrategyImplTest {
    private lateinit var demandSaver: DemandSaver
    private lateinit var demandConverter: DemandConverter
    private lateinit var target: AddDemandStrategyImpl

    @BeforeEach
    fun beforeEach() {
        demandSaver = mock()
        demandConverter = mock()
        target = AddDemandStrategyImpl(demandSaver, demandConverter)
    }

    //addDemand 로직
    //예약 엔티티를 추출한다.
    //예약 엔티티를 변조한다. (identifier = 0)
    //예약 엔티티를 저장한다.
    //예약 정보를 추출하여 반환한다.

    @Test @DisplayName("IP할당예약추가 성공 테스트")
    fun testAddDemandSuccess() {
        //예약추가가 성공하였다면,추가한 예약정보를 반환한다.
        //예약추가가 성공하려면, 아래의 작업이 성공해야한다.
        //- 예약추가 트랜잭션 수행

        //given
        val dto = mock<DemandDto>()
        val entity = mock<Demand>()
        val tamperedEntity = mock<Demand>()
        val savedEntity = mock<Demand>()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(demandConverter.toEntity(dto)).thenReturn(entity.toMono())
        whenever(entity.copy(identifier = 0)).thenReturn(tamperedEntity)
        whenever(demandSaver.saveDemand(eq(tamperedEntity), any())).thenReturn(savedEntity.toMono())
        whenever(demandConverter.toDto(savedEntity)).thenReturn(expectedResult.toMono())

        val result = target.addDemand(dto).block()!!

        //then
        assert(result == expectedResult)
    }
}