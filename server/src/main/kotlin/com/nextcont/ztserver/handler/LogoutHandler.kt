package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.Device
import com.nextcont.ztserver.model.Token
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq

class LogoutHandler: Handler() {

    override fun responseData(ctx: Context): Response {
        val resp = Response(0, "", "")

        val authToken = ctx.header("auth") ?: return resp

        val tokenRepo: ObjectRepository<Token> = Program.db.getRepository(Token::class.java)
        val token = tokenRepo.find(eq("id", authToken)).singleOrNull() ?: return resp

        val deviceRepo: ObjectRepository<Device> = Program.db.getRepository(Device::class.java)
        val device = deviceRepo.find(eq("userId", token.userId)).singleOrNull()
        if (device != null) {
            deviceRepo.remove(device)
        }

        tokenRepo.remove(token)

        return resp

    }

}