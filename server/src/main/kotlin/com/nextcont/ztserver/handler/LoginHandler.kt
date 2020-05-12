package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Logger
import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.Device
import com.nextcont.ztserver.model.Disable
import com.nextcont.ztserver.model.Token
import com.nextcont.ztserver.model.User
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import java.lang.Exception
import java.util.*

class LoginHandler: Handler() {

    override fun responseData(ctx: Context): Response {

        val body = ctx.body()
        val params = json.fromJson(body, Request::class.java)

        Logger.info(params.toString())

        val auth = ctx.header("auth")

        // 登录
        val userRepo: ObjectRepository<User> = Program.db.getRepository(User::class.java)
        val user = userRepo.find(eq("username", params.username)).singleOrNull() ?: throw Exception("用户不存在")

        val tokenRepo: ObjectRepository<Token> = Program.db.getRepository(Token::class.java)
        if (!auth.isNullOrBlank()) {
            val token = tokenRepo.find(eq("id", auth)).singleOrNull()
            if (token != null) {
                if (System.currentTimeMillis() - token.createAt > 1000 * 10) {
                    return Response(-1001, "登录已过期", "")
                }
            }
        }



        if (user.password != params.password) {
            return Response(-1, "密码错误", "")
        }

        if (params.deviceName.isBlank()) {
            return Response(-1, "无效的设备", "")
        }

        val disableRepo: ObjectRepository<Disable> = Program.db.getRepository(Disable::class.java)
        val disable = disableRepo.find(eq("userId", user.id)).singleOrNull()
        if (disable != null && System.currentTimeMillis() - disable.createAt < 1000 * 60 * 3) {
            return Response(-2, "该应用已失效", "")
        }

        val netEnv = Device.NetworkEnv.fromString(params.networkEnv)?: return Response(-1, "网络环境异常", "")



        var token = tokenRepo.find(eq("userId", user.id)).singleOrNull()
        if (token != null) {
            tokenRepo.remove(token)
        }
        token = Token(UUID.randomUUID().toString(), user.id, System.currentTimeMillis())
        tokenRepo.insert(token)

        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        val respData = deviceRepo.find(eq("userId", user.id)).singleOrNull()?.let { device ->
            when {
                params.deviceName != device.name -> {
                    ResponseData(user, token.id, Distrust.Device)
                }
                netEnv != device.netEnv -> {
                    ResponseData(user, token.id, Distrust.Network)
                }
                else -> {
                    ResponseData(user, token.id, Distrust.None)
                }
            }
        }?: kotlin.run {
            val device = Device(UUID.randomUUID().toString(), user.id, params.deviceName, netEnv)
            deviceRepo.insert(device)
            ResponseData(user, token.id, Distrust.None)
        }


        return Response(0, "", json.toJson(respData))
    }

    data class Request(
        val username: String = "",
        val password: String = "",
        val deviceName: String = "",
        val networkEnv: String = ""
    )

    data class ResponseData(
        val user: User,
        val token: String,
        val distrust: Distrust
    )

    enum class Distrust {
        None,
        Device,
        Network
    }

}