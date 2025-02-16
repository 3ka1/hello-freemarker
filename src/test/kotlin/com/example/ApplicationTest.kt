package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun `test root endpoint returns correct message`() = testApplication {
        application {
            module()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Welcome to Ktor!"))
        }
    }

    @Test
    fun `test greet endpoint with query parameter`() = testApplication {
        application {
            module()
        }

        client.get("/greet?message=Hello+Test").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Hello Test"))
        }
    }

    @Test
    fun `test greet endpoint with path parameter`() = testApplication {
        application {
            module()
        }

        client.get("/greet?message=Hello+Path+Test").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Hello Path Test"))
        }
    }

    @Test
    fun `test greet endpoint without message parameter but it contains path`() = testApplication {
        application {
            module()
        }

        client.get("/greet/Hello+World").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Hello+World"))
        }
    }

    @Test
    fun `test greet endpoint without message parameter`() = testApplication {
        application {
            module()
        }

        client.get("/greet").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Default Message"))
        }
    }

    @Test
    fun `test welcome endpoint contains message and timestamp`() = testApplication {
        application {
            module()
        }

        client.get("/welcome").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = bodyAsText()
            assertTrue(response.contains("Welcome to our site!"))
            assertTrue(response.contains("Time:"))
        }
    }
}