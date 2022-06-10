package site.iplease.iadserver.service

import reactor.core.publisher.Mono
import site.iplease.iadserver.data.type.MessageType

interface MessagePublishService {
    fun publish(type: MessageType, data: Any): Mono<Unit>
}
