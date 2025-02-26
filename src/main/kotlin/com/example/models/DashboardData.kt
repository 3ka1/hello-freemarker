package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class DashboardData(
    val username: String,
    val stats: Stats
)
