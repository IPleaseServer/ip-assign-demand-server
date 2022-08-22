package site.iplease.iadserver.global.common.repository

import org.reactivestreams.Publisher
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import site.iplease.iadserver.domain.common.data.entity.Demand

interface DemandRepository: ReactiveCrudRepository<Demand, Long> {
    fun findBy(page: Pageable): Flux<Demand>//https://stackoverflow.com/questions/58874827/spring-data-r2dbc-and-pagination
    fun findByIssuerId(page: Pageable, issuerId: Long): Flux<Demand>
    fun deleteByIdentifier(identifier: Long): Mono<Void>
    fun findByIdentifier(identifier: Long): Mono<Demand>
    fun existsByIdentifier(identifier: Long): Mono<Boolean>

    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> save(entity: S): Mono<S>
    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> saveAll(entities: MutableIterable<S>): Flux<S>
    @Deprecated(message = "DemandSaver.saveDemand()를 써주세요!")
    override fun <S : Demand> saveAll(entityStream: Publisher<S>): Flux<S>
}