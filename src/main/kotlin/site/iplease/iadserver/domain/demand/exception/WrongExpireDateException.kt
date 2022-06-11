package site.iplease.iadserver.domain.demand.exception

import java.time.LocalDate

class WrongExpireDateException(errorDetail: String, expireAt: LocalDate): RuntimeException("잘못된 만료일입니다! - $errorDetail - $expireAt")