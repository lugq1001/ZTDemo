package com.nextcont.ztserver.handler

import com.nextcont.ztserver.model.Examine
import io.javalin.http.Context

class ExaminesHandler: Handler() {

    override fun responseData(ctx: Context): Response {
        return Response(0, "", json.toJson(Examine.makeExamines()))
    }

}