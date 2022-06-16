package site.iplease.iadserver.infra.message.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandCancelErrorOnStatusMessage
import site.iplease.iadserver.global.demand.data.message.IpAssignDemandErrorOnStatusMessage
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandCancelErrorOnStatusSubscriber
import site.iplease.iadserver.global.demand.subscriber.IpAssignDemandErrorOnStatusSubscriber
import site.iplease.iadserver.infra.message.type.MessageType

@Component
class RabbitMqListener(
    private val ipAssignDemandErrorOnStatusSubscriber: IpAssignDemandErrorOnStatusSubscriber,
    private val ipAssignDemandCancelErrorOnStatusSubscriber: IpAssignDemandCancelErrorOnStatusSubscriber,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = ["iplease.ip.assign.demand"])
    fun listen(message: Message) {
        val routingKey = message.messageProperties.receivedRoutingKey
        val payload = String(message.body)
        handleMessage(MessageType.of(routingKey), payload)
            .doOnError{ throwable ->
                logger.error("메세지를 직렬화하는도중 오류가 발생하였습니다!")
                logger.error("exception: ${throwable.localizedMessage}")
                logger.error("payload(string): ${String(message.body)}")
                logger.error("payload(byte): ${message.body}")
            }.onErrorResume { Mono.empty() }
            .block()
    }

    private fun handleMessage(type: MessageType, payload: String): Mono<Unit> =
        when(type) {
            MessageType.IP_ASSIGN_DEMAND_CREATE_ERROR_ON_STATUS -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandErrorOnStatusMessage::class.java) }
                .map { message -> ipAssignDemandErrorOnStatusSubscriber.subscribe(message) }
            MessageType.IP_ASSIGN_DEMAND_CANCEL_ERROR_ON_STATUS -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandCancelErrorOnStatusMessage::class.java) }
                .map { message -> ipAssignDemandCancelErrorOnStatusSubscriber.subscribe(message) }
            else -> {
                logger.warn("처리대상이 아닌 메세지가 바인딩되어있습니다!")
                logger.warn("routingKey: ${type.routingKey}")
                logger.warn("payload(string): $payload")
                Unit.toMono()
            }
        }
}