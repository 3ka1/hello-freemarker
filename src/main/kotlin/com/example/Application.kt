package com.example

import com.example.models.DashboardData
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.http.*
import freemarker.cache.ClassTemplateLoader
import com.example.models.GreetingData
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import java.time.LocalDateTime

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Install content negotiation with JSON support
    install(ContentNegotiation) {
        json()
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        get("/") {
            call.respond(FreeMarkerContent("application/index.ftl", mapOf<String, Any>(), ""))
        }

        post("/greet") {
            try {
                val greetingData = call.receive<GreetingData>()
                call.respond(FreeMarkerContent(
                    "greets/greeting.ftl",
                    mapOf(
                        "message" to greetingData.message,
                        "sender" to greetingData.sender,
                        "timestamp" to LocalDateTime.now().toString()
                    )
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        post("/welcome") {
            try {
                val greetingData = call.receive<GreetingData>()
                call.respond(FreeMarkerContent(
                    "greets/welcome.ftl",
                    mapOf(
                        "message" to greetingData.message,
                        "sender" to greetingData.sender,
                    )
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }

        post("/dashboard") {
            try {
                val dashboardData = call.receive<DashboardData>()
                call.respond(FreeMarkerContent(
                    "application/dashboard.ftl",
                    mapOf(
                        "username" to dashboardData.username,
                        "stats" to mapOf(
                            "visits" to dashboardData.stats.visits,
                            "actions" to dashboardData.stats.actions
                        )
                    )
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
        }
    }
}