package site.iplease.iadserver.infra.alarm.service

import reactor.core.publisher.Mono

interface PushAlarmService {
    fun publish(receiverId: Long, title: String, description: String): Mono<Unit>
}