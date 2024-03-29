package site.iplease.iadserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import site.iplease.iadserver.domain.accept.config.AcceptedDemandProperties
import site.iplease.iadserver.domain.query.config.DataQueryProperty
import site.iplease.iadserver.domain.reject.config.RejectedDemandProperties

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(RejectedDemandProperties::class, AcceptedDemandProperties::class, DataQueryProperty::class)
class IpAssignDemandServerApplication

fun main(args: Array<String>) {
    runApplication<IpAssignDemandServerApplication>(*args)
}
