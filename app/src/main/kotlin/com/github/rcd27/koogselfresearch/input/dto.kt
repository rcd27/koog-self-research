package com.github.rcd27.koogselfresearch.input

import kotlinx.serialization.Serializable

@Serializable
data class McpConfig(
    val mcpServers: Map<String, McpServer>
)

@Serializable
data class McpServer(
    val command: String,
    val args: List<String>
)
