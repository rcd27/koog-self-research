package com.github.rcd27.koogselfresearch.agent.strategy

import ai.koog.agents.core.agent.context.DetachedPromptExecutorAPI
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeLLMRequest
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.tools.serialization.serializeToolDescriptorsToJsonString

fun standaloneLLMToolsBrief() = strategy<String, String>("llm_tools_brief") {
    val toolRegistryBrief by subgraphLLMToolsBrief()
    nodeStart then toolRegistryBrief then nodeFinish
}

// FIXME: in current implementation we look at WHOLE llm.tools. What if there are hundreds of tools?
// FIXME: shift to structured output
@OptIn(DetachedPromptExecutorAPI::class)
fun AIAgentSubgraphBuilderBase<*, *>.subgraphLLMToolsBrief(): AIAgentSubgraphDelegate<String, String> = subgraph("tool_registry_brief") {
    val extractToolDescriptors by node<String, String>("extract_tool_descriptors") { input ->
        return@node serializeToolDescriptorsToJsonString(this.llm.tools)
    }

    val requestLLM by nodeLLMRequest("ask_for_tool_descriptors_brief", allowToolCalls = false)

    nodeStart then extractToolDescriptors

    edge(
        extractToolDescriptors forwardTo requestLLM
            transformed { extractedToolDescriptors ->
                """
            <task>
You are analyzing the agent's available tools to generate a high-level summary of capabilities for self-discovery purposes.

Your goal is to create a concise, strategic overview that will help the agent understand WHAT it can do, not HOW to use specific tools.
</task>

<input>
Available tools specification:
$extractedToolDescriptors
</input>

<instructions>
1. **Group by Domain**: Identify logical capability domains (e.g., "Documentation Access", "Data Processing", "External Integrations")

2. **Abstract to User Stories**: For each domain, formulate capabilities as user stories:
   - Format: "As an agent, I can [capability] so that [benefit]"
   - Focus on outcomes, not implementation details
   - Example: "As an agent, I can retrieve up-to-date library documentation so that I provide accurate technical guidance"

3. **Identify Workflows**: Detect tool chains or sequences (e.g., Tool A must be called before Tool B)
   - Highlight prerequisites and dependencies
   - Note any required sequencing

4. **Capability Limitations**: Extract constraints from descriptions:
   - Required vs optional parameters
   - Format requirements
   - Pre-conditions for tool usage

5. **Strategic Summary**: Conclude with 2-3 sentences describing the agent's core value proposition based on these tools
</instructions>

<output_format>
Return a structured JSON object:

{
  "capability_domains": [
    {
      "domain": "Domain Name",
      "user_stories": [
        "As an agent, I can... so that..."
      ],
      "key_workflows": [
        {
          "description": "Brief workflow description",
          "sequence": ["tool-name-1", "tool-name-2"],
          "prerequisites": "What must be known/done first"
        }
      ]
    }
  ],
  "constraints": [
    "Global limitation or requirement that affects multiple tools"
  ],
  "core_value_proposition": "2-3 sentence summary of what makes this agent valuable"
}
</output_format>

<critical_rules>
- Do NOT include technical parameter details or API specifics
- Focus on CAPABILITIES and OUTCOMES, not mechanics
- User stories must be from the agent's perspective (first person)
- Identify tool dependencies explicitly
- Keep language abstract enough for strategic planning, concrete enough for skill generation
</critical_rules>

Analyze the provided tools and generate the capability brief.       
                """.trimIndent()
            }
    )

    edge(
        requestLLM forwardTo nodeFinish
            onAssistantMessage { true }
    )
}
