package site.iplease.iadserver.domain.demand.legacy.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "iplease.accepted-demand")
class AcceptedDemandProperties(
    val redisKeyPrefix: String
) {
}
