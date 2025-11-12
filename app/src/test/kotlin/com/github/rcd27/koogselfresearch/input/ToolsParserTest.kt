package com.github.rcd27.koogselfresearch.input

import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.Test

class ToolsParserTest {

    @Test
    fun `Parse valid JSON`() {
        val input = File("./src/test/resources/mcp.json")
        assert(input.exists())

        val stringRaw = input.readText()
        assert(stringRaw.isNotEmpty())

        val mcpServers = Json.decodeFromString<McpConfig>(stringRaw)
        assert(mcpServers.mcpServers.isNotEmpty())

        // TODO: MCPServer ->
    }
}
