package com.github.rcd27.koogselfresearch.input

import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.mcp.McpToolRegistryProvider
import ai.koog.agents.mcp.defaultStdioTransport
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Takes a JSON representation of Tools from given File and provides List<ToolRegistry>
 */
object ToolsParser {

    suspend fun File.parseTools(): List<ToolRegistry> {
        val stringRaw = this@parseTools.readText()
        require(stringRaw.isNotEmpty()) { ".json file is empty" }

        val mcpServers = Json.decodeFromString<McpConfig>(stringRaw)
        require(mcpServers.mcpServers.isNotEmpty()) { "Provided list of MCP servers is empty" }

        val registries: List<ToolRegistry> =
            mcpServers.mcpServers.entries.asFlow().map { (name: String, server: McpServer) ->
                val process = ProcessBuilder(server.command, *server.args.toTypedArray()).start()
                val transport = McpToolRegistryProvider.defaultStdioTransport(process)
                val registry = McpToolRegistryProvider.fromTransport(
                    transport = transport,
                    name = name,
                    version = "1.0.0"
                )
                registry
            }.toList()

        return registries
    }
}
