package site.iplease.iadserver.infra.alarm.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.infra.alarm.service.data.type.AlarmType

interface PushAlarmService {
    fun publish( receiverId: Long, title: String, description: String, type: AlarmType = AlarmType.FCM): Mono<Unit>
}