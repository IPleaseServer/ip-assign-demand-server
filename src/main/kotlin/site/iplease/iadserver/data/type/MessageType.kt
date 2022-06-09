package site.iplease.iadserver.data.type

enum class MessageType(
    val routingKey: String
) {
    IP_ASSIGN_DEMAND_CREATE("ipAssignDemandCreate")
}
