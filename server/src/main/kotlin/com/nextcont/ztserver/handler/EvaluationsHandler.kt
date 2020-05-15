package com.nextcont.ztserver.handler

import com.nextcont.ztserver.model.Evaluation
import com.nextcont.ztserver.model.User
import io.javalin.http.Context

class EvaluationsHandler: Handler() {

    override fun responseData(ctx: Context): Response {
        return Response(0, "", json.toJson(ResponseData()))
    }

    data class ResponseData(
        val evaluations: List<Evaluation> = Evaluation.makeExamines().toList()
    )
}