package site.iplease.iadserver.domain.common.util

import org.springframework.stereotype.Component
import site.iplease.iadserver.global.common.util.DateUtil
import java.time.LocalDate

@Component
class StandardDateUtil: DateUtil {
    override fun dateNow(): LocalDate = LocalDate.now()
}