package site.iplease.iadserver.domain.accept.scheduler

import org.springframework.scheduling.annotation.Scheduled
import site.iplease.iadserver.domain.accept.strategy.RemoveAcceptedDemandStrategy

class AcceptedDemandScheduler(private val removeAcceptedDemandStrategy: RemoveAcceptedDemandStrategy) {
    @Scheduled(cron = "0 0 3 ? * SAT")
    fun removeRejectedDemand() = removeAcceptedDemandStrategy.removeAcceptedDemand()
}