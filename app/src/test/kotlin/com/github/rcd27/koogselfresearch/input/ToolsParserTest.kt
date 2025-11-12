package com.github.rcd27.koogselfresearch.input

import ai.koog.agents.core.tools.ToolRegistry
import com.github.rcd27.koogselfresearch.input.ToolsParser.parseTools
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test

class ToolsParserTest {

    @Test
    fun `Parse valid JSON`() = runBlocking {
        val input = File("./src/test/resources/mcp.json")
        assert(input.exists())

        val toolRegistryList: List<ToolRegistry> = input.parseTools()
        assert(toolRegistryList.isNotEmpty())
    }
}
