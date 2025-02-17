package com.example

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import java.io.StringWriter
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class TemplateTest {
    private lateinit var configuration: Configuration

    @BeforeTest
    fun setup() {
        configuration = Configuration(Configuration.VERSION_2_3_32)
        configuration.templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    @Test
    fun `test index template rendering`() {
        val template = configuration.getTemplate("index.ftl")
        val writer = StringWriter()
        val model = mapOf("message" to "Test Message")

        template.process(model, writer)
        val result = writer.toString()

        assertTrue(result.contains("Test Message"))
        assertTrue(result.contains("<h1>"))
        assertTrue(result.contains("</h1>"))
    }
}