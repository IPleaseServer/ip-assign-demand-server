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
import site.iplease.iadserver.domain.accept.data.entity.AcceptedDemand
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.data.type.DemandPolicyType
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter
import site.iplease.iadserver.global.common.util.DemandPolicyValidator
import kotlin.random.Random

class LazyRemoveAcceptlDemandStrategyTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var demandConverter: DemandConverter
    private lateinit var acceptedDemandRepository: AcceptedDemandRepository
    private lateinit var demandPolicyValidator: DemandPolicyValidator
    private lateinit var target: LazyRemoveAcceptDemandStrategy

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        demandConverter = mock()
        acceptedDemandRepository = mock()
        demandPolicyValidator = mock()
        target = LazyRemoveAcceptDemandStrategy(demandConverter, demandRepository, acceptedDemandRepository, demandPolicyValidator)
    }

    //acceptDemand 로직
    //예약정보를 추출한다. (byId)
    //예약정보를 검증한다.
    //예약 엔티티를 조회한다.
    //예약정보를 추출한다. (byEntity)
    //수락된 예약 엔티티를 추출한다.
    //수락된 예약 엔티티를 추가한다.
    //예약정보를 반환한다.

    @Test @DisplayName("IP할당예약수락 성공 테스트")
    fun testAcceptDemandSuccess() {
        //예약수락이 성공하였다면,수락한 예약정보를 반환한다.
        //예약수락이 성공하려면, 아래의 작업이 성공해야한다.
        //- 예약수락 트랜잭션 수행 (수락된 예약 추가)

        //given
        val demandId = Random.nextLong()
        val dto = mock<DemandDto>()
        val entity = mock<Demand>()
        val assignIp = listOf("127.0.0.1", "10.120.74.32", "192.168.8.175").random()
        val acceptedDemand = AcceptedDemand(demandId, assignIp)
        val expectedResult = mock<DemandDto>()

        //when
        whenever(demandConverter.toDto(demandId)).thenReturn(dto.toMono())
        whenever(demandPolicyValidator.validate(dto, DemandPolicyType.DEMAND_ACCEPT)).thenReturn(dto.toMono())
        whenever(demandRepository.findByIdentifier(demandId)).thenReturn(entity.toMono())

        whenever(demandConverter.toDto(entity)).thenReturn(expectedResult.toMono())
        whenever(expectedResult.id).thenReturn(demandId)
        whenever(acceptedDemandRepository.insert(acceptedDemand)).thenReturn(Unit.toMono().then())

        val result = target.acceptDemand(demandId, assignIp).block()!!

        //then
        assertEquals(result, expectedResult)
        verify(acceptedDemandRepository, times(1)).insert(acceptedDemand)
    }
}