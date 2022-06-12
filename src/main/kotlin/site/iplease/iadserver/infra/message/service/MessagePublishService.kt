package site.iplease.iadserver.infra.message.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.infra.message.type.MessageType

interface MessagePublishService {
    fun publish(type: MessageType, data: Any): Mono<Unit>
}
