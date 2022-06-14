package site.iplease.iadserver.infra.message.type

enum class MessageType(
    val routingKey: String
) {
    IP_ASSIGN_DEMAND_CREATE("ipAssignDemandCreate"),
    IP_ASSIGN_DEMAND_CANCEL("ipAssignDemandCancel"),
    IP_ASSIGN_DEMAND_ERROR_ON_STATUS("ipAssignDemandErrorOnStatus"), //TODO routingKey를 ipAssignDemandCreateErrorOnStatus로 변경
    SEND_ALARM("sendAlarm"),
    UNKNOWN("");

    companion object {
        fun of(routingKey: String) =
            kotlin.runCatching { values().first { it.routingKey == routingKey } }
                .getOrElse { UNKNOWN }
    }
}