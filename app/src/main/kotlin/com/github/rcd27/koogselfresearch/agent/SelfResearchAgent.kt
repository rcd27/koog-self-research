package com.github.rcd27.koogselfresearch.agent

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.agent.GraphAIAgent
import ai.koog.agents.core.agent.config.AIAgentConfig
import ai.koog.agents.core.agent.entity.AIAgentGraphStrategy
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.opentelemetry.feature.OpenTelemetry
import ai.koog.agents.features.opentelemetry.integration.langfuse.addLangfuseExporter
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import com.github.rcd27.koogselfresearch.Config
import com.github.rcd27.koogselfresearch.agent.executor.openAISinglePromptExecutor

object SelfResearchAgent {
    val agentConfig = AIAgentConfig.withSystemPrompt(
        prompt = "You are deep research agent",
        maxAgentIterations = 100,
        llm = OpenAIModels.CostOptimized.GPT4oMini // TODO: check model in langchain implementation
    )

    suspend inline fun <reified INPUT, reified OUTPUT> create(
        tools: List<ToolRegistry> = emptyList(),
        strategy: AIAgentGraphStrategy<INPUT, OUTPUT>
    ): GraphAIAgent<INPUT, OUTPUT> {
        val agent: GraphAIAgent<INPUT, OUTPUT> = AIAgent(
            promptExecutor = openAISinglePromptExecutor,
            agentConfig = agentConfig,
            toolRegistry = tools.fold(ToolRegistry.EMPTY) { acc, i -> acc + i },
            strategy = strategy,
        ) {
            install(OpenTelemetry) {
                setVerbose(true)
                addLangfuseExporter(
                    langfuseUrl = Config.LANGFUSE_HOST,
                    langfusePublicKey = Config.LANGFUSE_PUBLIC_KEY,
                    langfuseSecretKey = Config.LANGFUSE_SECRET_KEY,
                )
            }
        }
        return agent
    }
}
