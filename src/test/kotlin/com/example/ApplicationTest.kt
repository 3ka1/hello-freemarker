package com.example

import com.example.models.DashboardData
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import com.example.models.GreetingData
import com.example.models.Stats
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class ApplicationTest {

    @Test
    fun `test root endpoint returns index page`() = testApplication {
        application {
            module()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assertTrue(responseText.contains("Template Tester"))
            assertTrue(responseText.contains("JSON Input"))
        }
    }

    @Test
    fun `test greet endpoint with valid JSON data`() = testApplication {
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
            assertTrue(responseText.contains("Time:"))
        }
    }

    @Test
    fun `test welcome endpoint with valid JSON data`() = testApplication {
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
    fun `test dashboard endpoint with valid JSON data`() = testApplication {
        application {
            module()
        }

        val stats = Stats(
            visits = 100,
            actions = 50
        )

        val dashboardData = DashboardData(
            username = "testUser",
            stats = stats
        )

        client.post("/dashboard") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(dashboardData))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            val responseText = bodyAsText()
            assertTrue(responseText.contains("testUser"))
            assertTrue(responseText.contains("100"))
            assertTrue(responseText.contains("50"))
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
            assertEquals("Invalid JSON format", bodyAsText())
        }
    }

    @Test
    fun `test welcome endpoint with invalid JSON`() = testApplication {
        application {
            module()
        }

        client.post("/welcome") {
            contentType(ContentType.Application.Json)
            setBody("""{"invalid": "json"}""")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Invalid JSON format", bodyAsText())
        }
    }

    @Test
    fun `test dashboard endpoint with invalid JSON`() = testApplication {
        application {
            module()
        }

        client.post("/dashboard") {
            contentType(ContentType.Application.Json)
            setBody("""{"invalid": "json"}""")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals("Invalid JSON format", bodyAsText())
        }
    }

    @Test
    fun `test greet endpoint with missing required fields`() = testApplication {
        application {
            module()
        }

        client.post("/greet") {
            contentType(ContentType.Application.Json)
            setBody("""{"sender": "Test"}""")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun `test dashboard endpoint with missing nested fields`() = testApplication {
        application {
            module()
        }

        client.post("/dashboard") {
            contentType(ContentType.Application.Json)
            setBody("""{"username": "test"}""")
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }
}
