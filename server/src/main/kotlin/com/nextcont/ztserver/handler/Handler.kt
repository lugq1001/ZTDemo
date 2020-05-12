package com.nextcont.ztserver.handler

import com.google.gson.Gson
import io.javalin.http.Context
import java.lang.Exception

abstract class Handler {



    protected val json = Gson()

    fun process(ctx: Context) {
        val response = try {
            responseData(ctx)
        } catch (e: Exception) {
            Response(-1, e.localizedMessage?: "Unknown Error", "")
        }
        val respJson = json.toJson(response)
        ctx.result(respJson).contentType("application/json")
    }

    protected fun checkToken(ctx: Context) {
//        val auth = ctx.header("auth")
//        if (auth == null) {
//            ctx.result(json.toJson(Response(-1, "登录已失效"))).contentType("application/json")
//        }
    }

    abstract fun responseData(ctx: Context): Response

    data class Response(val code: Int, val message: String, val data: String)

}

