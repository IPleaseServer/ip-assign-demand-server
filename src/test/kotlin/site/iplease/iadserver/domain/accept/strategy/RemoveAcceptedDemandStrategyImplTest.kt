package site.iplease.iadserver.domain.accept.strategy

import TestDummyDataUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.data.entity.AcceptedDemand
import site.iplease.iadserver.domain.accept.repository.AcceptedDemandRepository
import site.iplease.iadserver.global.common.repository.DemandRepository
import kotlin.random.Random

class RemoveAcceptedDemandStrategyImplTest {
    private lateinit var acceptedDemandRepository: AcceptedDemandRepository
    private lateinit var demandRepository: DemandRepository
    private lateinit var target: RemoveAcceptedDemandStrategyImpl

    @BeforeEach
    fun beforeEach() {
        acceptedDemandRepository = mock()
        demandRepository = mock()
        target = RemoveAcceptedDemandStrategyImpl(acceptedDemandRepository, demandRepository)
    }

    //removeAcceptedDemand 로직
    //수락된 신청을 모두 가져온다. - acceptedDemandRepository
    //수락된 신청에대한 IP할당신청 정보를 삭제한다. - demandRepository
    //수락된 신청을 삭제한다. - acceptedDemandRepository

    @Test @DisplayName("수락된신청일괄제거 성공테스트")
    fun testRemoveAcceptedDemandSuccess() {
        //수락된신청일괄제거가 성공하려면 아래의 작업이 성공해야한다.
        //- 신청삭제 트랜잭션 수행
        //- 수락된 신청삭제 트랜잭션 수행

        //given
        val demandIds = (1..Random.nextInt(10)).toList().map { Random.nextLong() }.distinct()
        val entities = demandIds.map { AcceptedDemand(it, TestDummyDataUtil.assignIp()) }

        //when
        whenever(acceptedDemandRepository.selectAll()).thenReturn(entities.toFlux())
        demandIds.forEach{ id ->
            whenever(demandRepository.deleteByIdentifier(id)).thenReturn(Unit.toMono().then())
            whenever(acceptedDemandRepository.delete(id)).thenReturn(id.toMono())
        }

        target.removeAcceptedDemand()

        //then
        demandIds.forEach { id ->
            verify(demandRepository, times(1)).deleteByIdentifier(id)
            verify(acceptedDemandRepository, times(1)).delete(id)
        }
    }
}