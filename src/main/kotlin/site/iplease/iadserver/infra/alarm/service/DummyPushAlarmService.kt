package site.iplease.iadserver.infra.alarm.service

import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class DummyPushAlarmService: PushAlarmService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(receiverId: Long, title: String, description: String): Mono<Unit> = Unit.toMono().map { sendAlarm(receiverId, title, description) }

    private fun sendAlarm(receiverId: Long, title: String, description: String) {
        logger.info("푸쉬알람을 보냅니다.")
        logger.info("receiverId: $receiverId")
        logger.info("title: $title")
        logger.info("description: $description")
    }
}