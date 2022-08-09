package site.iplease.iadserver.domain.accept.scheduler

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import site.iplease.iadserver.domain.accept.strategy.RemoveAcceptedDemandStrategy

class AcceptedDemandSchedulerTest {
    private lateinit var removeAcceptDemandStrategy: RemoveAcceptedDemandStrategy
    private lateinit var target: AcceptedDemandScheduler

    @BeforeEach
    fun beforeEach() {
        removeAcceptDemandStrategy = mock()
        target = AcceptedDemandScheduler(removeAcceptDemandStrategy)
    }

    //removeAcceptedDemand 로직
    //수락된 신청 제거 트랜잭션을 수행한다. - removeAcceptedDemandStrategy

    @Test @DisplayName("수락된신청일괄제거 성공 테스트")
    fun testRemoveAcceptedDemandSuccess() {
        //수락된신청제거에 성공하려면 아래의 작업이 성공해야한다.
        //- 수락된 신청 제거 트랜잭션 수행

        //when
        target.removeAcceptedDemand()
        
        //then
        verify(removeAcceptDemandStrategy, times(1)).removeAcceptedDemand()
    }
}