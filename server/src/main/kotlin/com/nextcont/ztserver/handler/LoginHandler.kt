package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Logger
import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.Device
import com.nextcont.ztserver.model.Disable
import com.nextcont.ztserver.model.User
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq

class LoginHandler: Handler() {

    override fun responseData(ctx: Context): Response {

        val body = ctx.body()
        val params = json.fromJson(body, Request::class.java)

        Logger.info(params.toString())

        // 登录
        val userRepo: ObjectRepository<User> = Program.db.getRepository(User::class.java)
        val user = userRepo.find(eq("username", params.username)).singleOrNull() ?: throw Exception("用户不存在")

        if (user.password != params.password) {
            throw Exception("密码错误")
        }

        // TODO 应用失效
        val disableRepo: ObjectRepository<Disable> = Program.db.getRepository(Disable::class.java)
        val disable = disableRepo.find(eq("userId", user.id)).singleOrNull()
        if (disable != null && System.currentTimeMillis() - disable.createAt < 1000 * 60 * 3) {
            throw Exception("该应用已失效")
        }

        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        val device = deviceRepo.find(eq("id", params.deviceId)).singleOrNull() ?: throw Exception("无效的设备")
        device.userId = user.id
        deviceRepo.update(device)

        return Response(0, "", json.toJson(ResponseData(user)))
    }

    data class Request(
        val username: String = "",
        val password: String = "",
        val deviceId: String = ""
    )

    data class ResponseData(
        val user: User
    )
}