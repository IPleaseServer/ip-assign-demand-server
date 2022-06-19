package site.iplease.iadserver.domain.demand.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import site.iplease.iadserver.domain.demand.strategy.RemoveRejectedDemandStrategy

@Component
class RejectedDemandScheduler(private val removeRejectedDemandStrategy: RemoveRejectedDemandStrategy) {
    @Scheduled(cron = "0 0 3 ? * SAT")
    fun removeRejectedDemand() = removeRejectedDemandStrategy.removeRejectedDemand()
}