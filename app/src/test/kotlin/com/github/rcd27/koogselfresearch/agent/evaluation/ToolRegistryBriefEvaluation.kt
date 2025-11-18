package com.github.rcd27.koogselfresearch.agent.evaluation

import com.github.rcd27.koogselfresearch.agent.SelfResearchAgent
import com.github.rcd27.koogselfresearch.agent.strategy.standaloneLLMToolsBrief
import com.github.rcd27.koogselfresearch.input.ToolsParser.parseTools
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.io.File

class ToolRegistryBriefEvaluation {

    @Test
    fun `Dummy output checkout`() = runBlocking {
        val tools = File("./src/test/resources/mcp-servers.json").parseTools()
        val agent = SelfResearchAgent.create(
            tools = tools,
            strategy = standaloneLLMToolsBrief()
        )
        val output = agent.run("Output in RUSSIAN language")
        println(output)
    }
}
