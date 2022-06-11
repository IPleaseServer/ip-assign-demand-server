package site.iplease.iadserver.infra.alarm.service

import reactor.core.publisher.Mono

interface PushAlarmService {
    fun publish(title: String, description: String): Mono<Unit>
}
