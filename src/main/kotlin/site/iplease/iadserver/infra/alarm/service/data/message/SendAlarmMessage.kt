package site.iplease.iadserver.infra.alarm.service.data.message

import site.iplease.iadserver.infra.alarm.service.data.type.AlarmType

data class SendAlarmMessage (
    val type: AlarmType,
    val receiverId: Long,
    val title: String,
    val description: String
)