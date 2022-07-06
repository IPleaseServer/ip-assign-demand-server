package site.iplease.iadserver.domain.demand.legacy.exception

class MalformedIpException(message: String): RuntimeException("잘못된 형식의 IP주소입니다! - $message")