package com.nextcont.ztserver.handler

import com.nextcont.ztserver.Program
import com.nextcont.ztserver.model.User
import io.javalin.http.Context
import org.dizitart.no2.objects.ObjectRepository

class UsersHandler: Handler() {

    override fun responseData(ctx: Context): Response {
        val userRepo: ObjectRepository<User> = Program.db.getRepository(User::class.java)
        val users = userRepo.find().toList()
        return Response(0, "", json.toJson(ResponseData(users)))
    }

    data class ResponseData(
        val users: List<User>
    )
}