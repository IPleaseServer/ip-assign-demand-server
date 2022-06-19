package site.iplease.iadserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import site.iplease.iadserver.domain.demand.config.RejectedDemandProperties

@SpringBootApplication
@EnableConfigurationProperties(RejectedDemandProperties::class)
class IpAssignDemandServerApplication

fun main(args: Array<String>) {
    runApplication<IpAssignDemandServerApplication>(*args)
}
