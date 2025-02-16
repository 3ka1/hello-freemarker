package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.freemarker.*
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        // root
        get("/") {
            call.respond(FreeMarkerContent("index.ftl", mapOf(
                "message" to "Welcome to Ktor!"
            )))
        }

        // Using query parameters: /greet?message=Hello+World
        get("/greet") {
            val message = call.parameters["message"] ?: "Default Message"
            call.respond(FreeMarkerContent("index.ftl", mapOf(
                "message" to message
            )))
        }

        // Using path parameters: /greet/Hello+World
        get("/greet/{message}") {
            val message = call.parameters["message"] ?: "Default Message"
            call.respond(FreeMarkerContent("index.ftl", mapOf(
                "message" to message
            )))
        }

        // Using a data class
        get("/welcome") {
            val pageData = PageData(
                message = "Welcome to our site!",
                timestamp = LocalDateTime.now().toString()
            )
            call.respond(FreeMarkerContent("welcome.ftl", mapOf(
                "data" to pageData
            )))
        }
    }
}

data class PageData(
    val message: String,
    val timestamp: String
)