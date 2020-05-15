package com.nextcont.mob.network.response

import com.nextcont.mob.model.User

data class DeviceRegisterResponse(
    val deviceId: String,
    val user: User?,
    val expired: Boolean,
    val disabled: Boolean
)