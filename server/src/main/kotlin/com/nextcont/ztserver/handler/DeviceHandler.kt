package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Logger
import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.Device
import com.nextcont.ztserver.model.User
import com.nextcont.ztserver.util.md5
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters
import java.util.*

class DeviceHandler: Handler() {

    override fun responseData(ctx: Context): Response {
        val body = ctx.body()
        Logger.info(body)

        val params = json.fromJson(body, Request::class.java)

        var info = "${params.deviceName}${params.fingerprint}"

        if (info.isBlank()) {
            info = UUID.randomUUID().toString()
        }

        val deviceId = info.md5
        val respData = ResponseData(deviceId, null, expired = false, disabled = false)

        // 保存设备
        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        var device = deviceRepo.find(ObjectFilters.eq("id", deviceId)).singleOrNull()
        if (device == null) {
            device = Device(deviceId, null, 0, 0)
            deviceRepo.insert(device)
        }

        device.userId?.let { id ->
            val userRepo: ObjectRepository<User> = Program.db.getRepository(User::class.java)
            val user = userRepo.find(ObjectFilters.eq("id", id)).singleOrNull()
            if (user != null) {
                if (device.loginTime > 0 && (System.currentTimeMillis() - device.loginTime > 1000 * 300)) {
                    respData.expired = true
                }
            }
            respData.user = user
        }

        if (device.disableTime > 0 && (System.currentTimeMillis() - device.disableTime < 1000 * 30)) {
            respData.disabled = true
        }

        return Response(0, "", json.toJson(respData))
    }

    data class Request(
        val deviceName: String = "",
        val fingerprint: String = ""
    )

    data class ResponseData(
        val deviceId: String,
        var user: User?,
        var expired: Boolean,
        var disabled: Boolean
    )
}