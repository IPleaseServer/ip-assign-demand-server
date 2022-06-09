package site.iplease.iadserver.data.type

enum class MessageType(
    val routingKey: String
) {
    DEMAND_ASSIGN_IP("ipAssignDemandCreate")
}
