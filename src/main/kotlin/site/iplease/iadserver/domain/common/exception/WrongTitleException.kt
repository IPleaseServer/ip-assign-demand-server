package site.iplease.iadserver.domain.common.exception

class WrongTitleException(detail: String): RuntimeException("잘못된 신청제목입니다! - $detail")
