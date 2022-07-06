package site.iplease.iadserver.domain.reject.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "iplease.rejected-demand")
class RejectedDemandProperties(
    val redisKeyPrefix: String
) {
}
