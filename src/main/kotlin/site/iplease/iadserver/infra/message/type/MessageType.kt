package site.iplease.iadserver.infra.message.type

enum class MessageType(
    val routingKey: String
) {
    IP_ASSIGN_DEMAND_CREATE("ipAssignDemandCreate")
}
