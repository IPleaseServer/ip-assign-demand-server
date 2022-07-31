package site.iplease.iadserver.domain.common.service

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.common.data.entity.Demand
import site.iplease.iadserver.global.common.data.dto.DemandDto
import site.iplease.iadserver.global.common.repository.DemandRepository
import site.iplease.iadserver.global.common.util.DemandConverter

class DemandQueryServiceImplTest {
    private lateinit var demandRepository: DemandRepository
    private lateinit var demandConverter: DemandConverter
    private lateinit var target: DemandQueryServiceImpl

    @BeforeEach
    fun beforeEach() {
        demandRepository = mock()
        demandConverter = mock()
        target = DemandQueryServiceImpl(demandRepository, demandConverter)
    }

    //getDemandById 로직
    //예약 id를 검증한다. (isExists) -> demandRepository
    //예약 엔티티를 조회한다. -> demandRepository
    //예약정보를 추출하여 반환한다.

    @Test @DisplayName("예약정보조회(Id) 성공 테스트")
    fun getDemandByIdSuccess() {
        //예약정보조회에 성공하였다면,조회한 예약정보를 반환한다.
        //예약정보조회가 성공하려면아래의 작업이 성공해야한다.
        //- 예약존재여부 검증
        //- 예약조회 트랜잭션 수행

        //given
        val demandId = TestDummyDataUtil.id()
        val entity = mock<Demand>()
        val expectedResult = mock<DemandDto>()

        //when
        whenever(demandRepository.existsByIdentifier(demandId)).thenReturn(true.toMono())
        whenever(demandRepository.findByIdentifier(demandId)).thenReturn(entity.toMono())
        whenever(demandConverter.toDto(entity)).thenReturn(expectedResult.toMono())

        val result = target.getDemandById(demandId).block()!!

        //then
        assertTrue(result == expectedResult)
    }
}