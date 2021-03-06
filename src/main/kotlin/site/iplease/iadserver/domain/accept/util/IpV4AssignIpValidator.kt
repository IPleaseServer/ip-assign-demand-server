package site.iplease.iadserver.domain.accept.util

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import site.iplease.iadserver.domain.accept.exception.MalformedIpException
import site.iplease.iadserver.global.accept.util.AssignIpValidator

@Component
class IpV4AssignIpValidator: AssignIpValidator {
    private val regex = Regex("^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}\$")
    override fun validate(assignIp: String): Mono<Unit> =
        assignIp.toMono()
            .map { assignIp.matches(regex) }
            .flatMap { isValid ->
                if(isValid) Unit.toMono()
                else Mono.error(MalformedIpException("ip는 0.0.0.0 - 255.255.255.255 사이의 값이어야합니다! - $assignIp"))
            }
}