package site.iplease.iadserver.domain.accept.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import site.iplease.iadserver.domain.accept.strategy.RemoveAcceptedDemandStrategy

@Component
class AcceptedDemandScheduler(private val removeAcceptedDemandStrategy: RemoveAcceptedDemandStrategy) {
    @Scheduled(cron = "0 0 3 ? * SAT")
    fun removeAcceptedDemand() = removeAcceptedDemandStrategy.removeAcceptedDemand()
}