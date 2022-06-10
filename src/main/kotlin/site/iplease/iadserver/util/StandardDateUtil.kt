package site.iplease.iadserver.util

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StandardDateUtil: DateUtil {
    override fun dateNow(): LocalDate = LocalDate.now()
}