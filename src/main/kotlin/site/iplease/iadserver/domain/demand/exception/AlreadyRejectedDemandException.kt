package site.iplease.iadserver.domain.demand.exception

class AlreadyRejectedDemandException(message: String): RuntimeException("이미 거절된 예약입니다! $message")