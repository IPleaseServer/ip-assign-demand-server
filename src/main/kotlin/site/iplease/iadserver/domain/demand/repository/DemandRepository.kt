package site.iplease.iadserver.domain.demand.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import site.iplease.iadserver.domain.demand.data.entity.Demand

interface DemandRepository: ReactiveCrudRepository<Demand, Long>