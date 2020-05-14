package com.nextcont.ztserver


import com.nextcont.ztserver.handler.*
import com.nextcont.ztserver.model.User
import io.javalin.Javalin
import org.dizitart.kno2.nitrite
import org.dizitart.no2.Nitrite
import org.dizitart.no2.objects.ObjectRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

var Logger: Logger = LoggerFactory.getLogger(Program::class.java)

class Program {

    companion object {

        private lateinit var server: Javalin
        lateinit var db: Nitrite

        @JvmStatic
        fun main(args: Array<String>) {

            db = nitrite {
                path = "ztserver0.db"
                compress = true
                autoCompact = false
            }

            val repository: ObjectRepository<User> = db.getRepository(User::class.java)
            val users = repository.find().toList()

            if (users.isEmpty()) {
                repository.insert(User.makeUsers())
            }

            server = Javalin.create().start(7000)
            server.post("/device") { ctx -> DeviceHandler().process(ctx) }
            server.post("/login") { ctx -> LoginHandler().process(ctx) }
            server.post("/logout") { ctx -> LogoutHandler().process(ctx) }
            server.post("/disable") { ctx -> DisableHandler().process(ctx) }
            server.get("/examines") { ctx -> ExaminesHandler().process(ctx) }


        }

    }

}