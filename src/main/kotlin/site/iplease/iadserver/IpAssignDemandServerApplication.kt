package site.iplease.iadserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IpAssignDemandServerApplication

fun main(args: Array<String>) {
    runApplication<IpAssignDemandServerApplication>(*args)
}
