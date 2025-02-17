package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val visits: Int,
    val actions: Int
)
