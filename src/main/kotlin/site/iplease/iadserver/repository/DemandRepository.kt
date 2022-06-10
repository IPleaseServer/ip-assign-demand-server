package site.iplease.iadserver.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import site.iplease.iadserver.data.entity.Demand

interface DemandRepository: ReactiveCrudRepository<Demand, Long>