package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Logger
import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.Device
import com.nextcont.ztserver.model.Disable
import com.nextcont.ztserver.model.Token
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import java.util.*

/**
 * 停用帐号
 */
class DisableHandler: Handler() {

    override fun responseData(ctx: Context): Response {

        val body = ctx.body()
        val params = json.fromJson(body, Request::class.java)

        Logger.info(params.toString())

        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        deviceRepo.find(eq("id", params.deviceId)).singleOrNull()?.let {
            it.disableTime = System.currentTimeMillis()
            it.loginTime = 0
            it.userId = null
            deviceRepo.update(it)
        }

        return Response(0, "", json.toJson(ResponseData()))

    }

    data class Request(
        val deviceId: String = ""
    )

    class ResponseData

}