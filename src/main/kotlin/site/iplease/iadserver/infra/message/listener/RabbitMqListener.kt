package site.iplease.iadserver.infra.message.listener

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.global.accept.data.message.IpAssignDemandAcceptMessage
import site.iplease.iadserver.global.accept.subscriber.IpAssignDemandAcceptSubscriber
import site.iplease.iadserver.global.confirm.data.message.IpAssignDemandConfirmMessage
import site.iplease.iadserver.global.confirm.subscriber.IpAssignDemandConfirmSubscriber
import site.iplease.iadserver.global.error.data.message.IpAssignDemandCancelErrorOnStatusMessage
import site.iplease.iadserver.global.error.data.message.IpAssignDemandCreateErrorOnStatusMessage
import site.iplease.iadserver.global.error.subscriber.IpAssignDemandCancelErrorOnStatusSubscriber
import site.iplease.iadserver.global.error.subscriber.IpAssignDemandCreateErrorOnStatusSubscriber
import site.iplease.iadserver.global.reject.data.message.IpAssignDemandRejectMessage
import site.iplease.iadserver.global.reject.subscriber.IpAssignDemandRejectSubscriber
import site.iplease.iadserver.infra.message.type.MessageType

@Component
class RabbitMqListener(
    private val ipAssignDemandCreateErrorOnStatusSubscriber: IpAssignDemandCreateErrorOnStatusSubscriber,
    private val ipAssignDemandCancelErrorOnStatusSubscriber: IpAssignDemandCancelErrorOnStatusSubscriber,
    private val ipAssignDemandConfirmSubscriber: IpAssignDemandConfirmSubscriber,
    private val ipAssignDemandRejectSubscriber: IpAssignDemandRejectSubscriber,
    private val ipAssignDemandAcceptSubscriber: IpAssignDemandAcceptSubscriber,
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
            MessageType.IP_ASSIGN_DEMAND_CONFIRM -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandConfirmMessage::class.java) }
                .map { message -> ipAssignDemandConfirmSubscriber.subscribe(message) }
            MessageType.IP_ASSIGN_DEMAND_REJECT -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandRejectMessage::class.java) }
                .map { message -> ipAssignDemandRejectSubscriber.subscribe(message) }
            MessageType.IP_ASSIGN_DEMAND_ACCEPT -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandAcceptMessage::class.java) }
                .map { message -> ipAssignDemandAcceptSubscriber.subscribe(message) }
            MessageType.IP_ASSIGN_DEMAND_CREATE_ERROR_ON_STATUS -> objectMapper.toMono()
                .map { it.readValue(payload, IpAssignDemandCreateErrorOnStatusMessage::class.java) }
                .map { message -> ipAssignDemandCreateErrorOnStatusSubscriber.subscribe(message) }
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