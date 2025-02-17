package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class GreetingData(
    val message: String,
    val sender: String = "Anonymous"
)