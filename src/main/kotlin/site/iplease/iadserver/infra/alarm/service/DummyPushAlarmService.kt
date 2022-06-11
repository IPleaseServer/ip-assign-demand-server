package site.iplease.iadserver.infra.alarm.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service //TODO PushAlarmService 개발 후 실제 서비스 구현체 작성 및 더미서비스 비활성화
class DummyPushAlarmService: PushAlarmService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun publish(title: String, description: String): Mono<Unit> = Unit.toMono().map { sendAlarm(title, description) }

    private fun sendAlarm(title: String, description: String) {
        logger.info("푸쉬알람을 보냅니다.")
        logger.info("title: $title")
        logger.info("description: $description")
    }
}