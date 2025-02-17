package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import com.example.models.GreetingData
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Welcome to Ktor!"))
        }
    }

    @Test
    fun `test greet endpoint with JSON data`() = testApplication {
        application {
            module()
        }

        val greetingData = GreetingData(
            message = "Hello from Test",
            sender = "Test Suite"
        )

        client.post("/greet") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(greetingData))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assertTrue(responseText.contains("Hello from Test"))
            assertTrue(responseText.contains("Test Suite"))
        }
    }

    @Test
    fun `test welcome endpoint with JSON data`() = testApplication {
        application {
            module()
        }

        val greetingData = GreetingData(
            message = "Welcome Test",
            sender = "Test Suite"
        )

        client.post("/welcome") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(greetingData))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assertTrue(responseText.contains("Welcome Test"))
            assertTrue(responseText.contains("Test Suite"))
        }
    }

    @Test
    fun `test greet endpoint with invalid JSON`() = testApplication {
        application {
            module()
        }

        client.post("/greet") {
            contentType(ContentType.Application.Json)
            setBody("""{"invalid": "json"}""")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }
}