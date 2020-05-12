package com.nextcont.ztserver.handler

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
        val resp = Response(0, "", "")

        val authToken = ctx.header("auth") ?: return resp

        val tokenRepo: ObjectRepository<Token> = Program.db.getRepository(Token::class.java)
        val token = tokenRepo.find(eq("id", authToken)).singleOrNull() ?: return resp

        val disableRepo: ObjectRepository<Disable> = Program.db.getRepository(Disable::class.java)
        val disable = disableRepo.find(eq("userId", token.userId)).singleOrNull()
        if (disable != null) {
            disableRepo.remove(disable)
        }
        disableRepo.insert(Disable(UUID.randomUUID().toString(), token.userId, System.currentTimeMillis()))

        return resp

    }

}