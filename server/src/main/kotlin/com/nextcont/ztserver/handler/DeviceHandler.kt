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

        val respData = ResponseData(deviceId, null)

        // todo 登录已过期

        // 保存设备
        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        var device = deviceRepo.find(ObjectFilters.eq("id", deviceId)).singleOrNull()
        if (device == null) {
            device = Device(deviceId, null)
            deviceRepo.insert(device)
        }

        val userRepo: ObjectRepository<User> = Program.db.getRepository(User::class.java)
        device.userId?.let { id ->
            val user = userRepo.find(ObjectFilters.eq("id", id)).singleOrNull()
            respData.user = user
        }

        return Response(0, "", json.toJson(respData))
    }

    data class Request(
        val deviceName: String = "",
        val fingerprint: String = ""
    )

    data class ResponseData(
        val deviceId: String,
        var user: User?
    )
}