package com.example

import com.example.models.DashboardData
import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import kotlin.test.*
import java.io.StringWriter
import com.example.models.GreetingData
import com.example.models.Stats

class TemplateTest {
    private lateinit var configuration: Configuration

    @BeforeTest
    fun setup() {
        configuration = Configuration(Configuration.VERSION_2_3_32).apply {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
            defaultEncoding = "UTF-8"
            localizedLookup = false
        }
    }

    @Test
    fun `test greeting template rendering`() {
        val template = configuration.getTemplate("greets/greeting.ftl")
        val writer = StringWriter()

        val model = mapOf(
            "message" to "Hello, Test!",
            "sender" to "Test User",
            "timestamp" to "2025-02-17 10:00:00"
        )

        template.process(model, writer)
        val result = writer.toString()

        // Verify content
        assertTrue(result.contains("Hello, Test!"))
        assertTrue(result.contains("Test User"))
        assertTrue(result.contains("2025-02-17 10:00:00"))

        // Verify structure
        assertTrue(result.contains("<div class=\"greeting\">"))
        assertTrue(result.contains("<div class=\"message\">"))
        assertTrue(result.contains("<div class=\"sender\">"))
        assertTrue(result.contains("<div class=\"timestamp\">"))
    }

    @Test
    fun `test welcome template rendering`() {
        val template = configuration.getTemplate("greets/welcome.ftl")
        val writer = StringWriter()

        val greetingData = GreetingData(
            message = "Welcome, Test!",
            sender = "Admin"
        )

        template.process(greetingData, writer)
        val result = writer.toString()

        // Verify content
        assertTrue(result.contains("Welcome, Test!"))
        assertTrue(result.contains("Admin"))

        // Verify structure
        assertTrue(result.contains("<div class=\"welcome\">"))
        assertTrue(result.contains("<div class=\"message\">"))
        assertTrue(result.contains("<div class=\"sender\">"))
    }

    @Test
    fun `test dashboard template rendering`() {
        val template = configuration.getTemplate("application/dashboard.ftl")
        val writer = StringWriter()

        val stats = Stats(
            visits = 100,
            actions = 50
        )

        val dashboardData = DashboardData(
            username = "testUser",
            stats = stats
        )

        template.process(dashboardData, writer)
        val result = writer.toString()

        // Verify content
        assertTrue(result.contains("testUser"))
        assertTrue(result.contains("100"))
        assertTrue(result.contains("50"))

        // Verify structure
        assertTrue(result.contains("<div class=\"dashboard\">"))
        assertTrue(result.contains("<div class=\"user-info\">"))
        assertTrue(result.contains("<div class=\"stats\">"))
        assertTrue(result.contains("<div class=\"stat-card\">"))
    }

    @Test
    fun `test greeting template with missing optional fields`() {
        val template = configuration.getTemplate("greets/greeting.ftl")
        val writer = StringWriter()

        val model = mapOf(
            "message" to "Hello, Test!",
            "sender" to "Test User"
            // timestamp is missing
        )

        template.process(model, writer)
        val result = writer.toString()

        // Verify content exists without a timestamp
        assertTrue(result.contains("Hello, Test!"))
        assertTrue(result.contains("Test User"))
        assertTrue(result.contains("Time: ")) // Should show empty value
    }

    @Test
    fun `test index template rendering`() {
        val template = configuration.getTemplate("application/index.ftl")
        val writer = StringWriter()

        template.process(emptyMap<String, Any>(), writer)
        val result = writer.toString()

        // Verify basic structure
        assertTrue(result.contains("<div class=\"nav-sidebar\">"))
        assertTrue(result.contains("<div class=\"main-content\">"))

        // Verify form elements
        assertTrue(result.contains("<form id=\"jsonForm\">"))
        assertTrue(result.contains("<textarea id=\"jsonInput\""))

        // Verify a template list
        assertTrue(result.contains("Greeting Template"))
        assertTrue(result.contains("Welcome Template"))
        assertTrue(result.contains("Dashboard Template"))

        // Verify JavaScript includes
        assertTrue(result.contains("const templates ="))
        assertTrue(result.contains("function updateTemplateInfo"))
    }

    @Test
    fun `test template error handling`() {
        assertFailsWith<freemarker.core.InvalidReferenceException> {
            val template = configuration.getTemplate("greets/greeting.ftl")
            val writer = StringWriter()
            template.process(emptyMap<String, Any>(), writer)
        }
    }

    @Test
    fun `test all templates exist`() {
        val expectedTemplates = listOf(
            "application/index.ftl",
            "application/dashboard.ftl",
            "greets/greeting.ftl",
            "greets/welcome.ftl"
        )

        expectedTemplates.forEach { templatePath ->
            assertNotNull(
                configuration.getTemplate(templatePath),
                "Template $templatePath should exist"
            )
        }
    }
}
