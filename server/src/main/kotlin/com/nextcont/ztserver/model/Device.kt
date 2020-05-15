package com.nextcont.ztserver.model

import org.dizitart.no2.objects.Id

data class Device(
    @Id val id: String,
    var userId: String?,
    var loginTime: Long,
    var disableTime: Long
)