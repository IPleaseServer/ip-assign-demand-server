package site.iplease.iadserver.infra.message.type

enum class MessageType(
    val routingKey: String
) {
    IP_ASSIGN_DEMAND_CREATE("ipAssignDemandCreate"),
    IP_ASSIGN_DEMAND_CANCEL("ipAssignDemandCancel"),
    IP_ASSIGN_DEMAND_CONFIRM("ipAssignDemandConfirm"),
    IP_ASSIGN_DEMAND_REJECT("ipAssignDemandReject"),
    IP_ASSIGN_DEMAND_ACCEPT("ipAssignDemandAccept"),
    IP_ASSIGN_DEMAND_CREATE_ERROR_ON_STATUS("ipAssignDemandCreateErrorOnStatus"),
    IP_ASSIGN_DEMAND_CANCEL_ERROR_ON_STATUS("ipAssignDemandCancelErrorOnStatus"),
    IP_ASSIGN_DEMAND_REJECT_ERROR_ON_STATUS("ipAssignDemandRejectErrorOnStatus"),//TODO ON_DEMAND 가 되어야함
    IP_ASSIGN_DEMAND_ACCEPT_ERROR_ON_DEMAND("ipAssignDemandAcceptErrorOnDemand"),
    ASSIGN_IP_CREATE("assignIpCreate"),
    SEND_ALARM("sendAlarm"),
    UNKNOWN(""),;

    companion object {
        fun of(routingKey: String) =
            kotlin.runCatching { values().first { it.routingKey == routingKey } }
                .getOrElse { UNKNOWN }
    }
}