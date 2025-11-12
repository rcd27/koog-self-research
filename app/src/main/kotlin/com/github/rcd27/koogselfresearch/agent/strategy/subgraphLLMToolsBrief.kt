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
// FIXME: 2 MCP servers gave 2m waiting time with very deep analysis regarding their DOMAINS
@OptIn(DetachedPromptExecutorAPI::class)
fun AIAgentSubgraphBuilderBase<*, *>.subgraphLLMToolsBrief(): AIAgentSubgraphDelegate<String, String> =
    subgraph("tool_registry_brief") {
        val extractToolDescriptors by node<String, String>("extract_tool_descriptors") { input ->
            return@node serializeToolDescriptorsToJsonString(this.llm.tools)
        }

        val requestLLM by nodeLLMRequest("descriptors_brief", allowToolCalls = false)

        nodeStart then extractToolDescriptors

        edge(
            extractToolDescriptors forwardTo requestLLM
                transformed { extractedToolDescriptors ->
                    println(extractedToolDescriptors) // TODO: remove
                    """
<task>
You are analyzing the agent's available tools to generate a high-level summary of capabilities for self-discovery and skill generation purposes.

Your goal is to create a CREATIVE yet REALISTIC overview that reveals non-obvious cross-domain combinations while staying strictly within actual tool capabilities.
</task>

<input>
Available tools specification:
$extractedToolDescriptors
</input>

<analysis_framework>

## Phase 1: Deep Tool Understanding
For each tool, extract with precision:
- **Exact inputs**: What parameters does it accept? (required vs optional)
- **Exact outputs**: What data structure/fields does it return? (not assumptions!)
- **Implicit constraints**: Rate limits, dependencies, data format requirements
- **State changes**: Does it modify data (POST/PATCH) or only read (GET)?

CRITICAL: Do NOT assume tools have capabilities beyond their description. If a tool returns `{hasNew: boolean}`, it does NOT return content - you must call another tool for that.

## Phase 2: Cross-Domain Synthesis Discovery
MANDATORY: Identify opportunities to combine tools from different domains:

**Domain A: Context7 Documentation**
- Tools: resolve-library-id, get-library-docs
- Data: Library IDs, documentation content, topics

**Domain B: Wildberries Customer Interactions**  
- Tools: getFeedbacks, getQuestions, postFeedbackAnswer, etc.
- Data: Customer text, product IDs (nmId), timestamps, answer status

**Domain C: Wildberries Quality Data**
- Tools: getSupplierValuations
- Data: Complaint categories, problem taxonomies

Look for data bridges:
- Can customer text mention library/package names? → Extract → Context7 lookup
- Can complaint categories correlate with documentation gaps? → Quality improvement loop
- Can question timestamps spike after library releases? → Proactive monitoring

## Phase 3: Realistic Mechanism Design
For each capability, specify the EXACT mechanism:
- **Data extraction**: What fields? What parsing method? (regex pattern, frequency analysis, time-series)
- **Transformation**: How does output from Tool A become input for Tool B?
- **Thresholds**: What triggers action? (2x spike, >100 occurrences, <50% answer rate)
- **Timing**: How often? (continuous polling, daily batch, weekly report)

FORBIDDEN phrases without specifics:
- "Analyze trends" → HOW? Time-series comparison? Frequency counting?
- "Correlate with documentation" → WHAT field correlates with WHAT?
- "Identify gaps" → WHAT algorithm detects a gap?

</analysis_framework>

<critical_constraints>

## 1. Tool Output Reality Check
Before claiming data flows, verify tool outputs:

**Example - WRONG:**
```json
"from": "getNewFeedbacksQuestions" (returns: {hasNew: boolean})
"to": "resolve-library-id" (needs: libraryName string)
```
❌ A boolean cannot flow to a string parameter!

**Example - CORRECT:**
```json
"from": "getQuestions(isAnswered=false, take=100)" (returns: [{id, text, nmId, ...}])
"to": "resolve-library-id" 
"transformation": "Extract library mentions from question.text using regex: /(?:library|package|библиотека)[:\s]*([a-z0-9-]+)/i"
```

## 2. No Capability Invention
You can ONLY claim what tools ACTUALLY provide:

❌ FORBIDDEN if not in tool description:
- "Track resolution status over time" (no historical tracking tool)
- "Generate templated responses" (no template system)
- "Detect documentation quality" (no quality metrics in output)
- "Access version history" (get-library-docs returns current docs only)
- "Create tickets/issues" (no write access to issue tracking)

✅ ALLOWED with proper mechanism:
- "Compare question counts across time windows" (via getQuestionsCount with different dateFrom/dateTo)
- "Extract keywords from feedback text" (via string parsing of getFeedbacks output)
- "Detect volume spikes" (via mathematical comparison: current/previous > 2.0)

## 3. Mandatory Cross-Domain Integration
YOU MUST create at least TWO synthetic stories that combine tools from different domains:

**Required combination #1: Context7 ↔ Wildberries Feedback**
- Use customer text (questions/feedback) to identify mentioned libraries
- Fetch relevant documentation
- Enrich responses or identify documentation gaps

**Required combination #2: Must include getSupplierValuations**
- Complaint categories → feedback content matching
- OR complaint categories → documentation coverage analysis
- OR complaint frequency → proactive documentation updates

If you cannot find these combinations, you have not analyzed deeply enough. They MUST exist.

## 4. Measurability Requirements
Every compound/synthetic/strategic story MUST include at least ONE concrete metric:

✅ GOOD metrics:
- "Reduce response time from 24h to 4h"
- "Process 100 questions in <2 seconds"  
- "Detect spikes with 2x threshold over 7-day baseline"
- "Reduce repeat questions by 25% per quarter"
- "Increase answer rate from 60% to 85%"

❌ BAD (vague):
- "Improve satisfaction"
- "Enhance quality"  
- "Optimize performance"
- "Better experience"

## 5. Rate Limit Strategy
All Wildberries tools share 1 req/sec limit. Your capabilities MUST respect this:
- Mention batching strategies
- Prioritization approaches  
- Queuing mechanisms
- Impact on workflow timing

</critical_constraints>

<output_format>
Return a structured JSON object:

```json
{
  "capability_layers": {
    "atomic": [
      {
        "capability": "Single-tool capability description",
        "tools": ["tool-name"],
        "user_story": "As an agent, I can [action] by [SPECIFIC mechanism with parameters] so that [MEASURABLE outcome]"
      }
    ],
    "compound": [
      {
        "capability": "Multi-tool workflow within one domain",
        "tools": ["tool-1", "tool-2", "tool-3"],
        "user_story": "As an agent, I can [action] by [SPECIFIC sequence with parameters] so that [MEASURABLE outcome with baseline→target]",
        "workflow_pattern": "Pattern name (e.g., Monitor → Filter → Act)",
        "timing": "How often this runs (continuous/hourly/daily/weekly)",
        "rate_limit_strategy": "How to stay within 1 req/sec constraint"
      }
    ],
    "synthetic": [
      {
        "capability": "Cross-domain capability",
        "tools": ["tool-from-domain-A", "tool-from-domain-B", "tool-from-domain-C"],
        "user_story": "As an agent, I can [action] by [SPECIFIC cross-domain mechanism] so that [MEASURABLE outcome]",
        "emergence": "Why this combination creates value not possible with single domain",
        "data_bridge": "What specific data/field connects the domains",
        "example_scenario": "Concrete example with sample data"
      }
    ],
    "strategic": [
      {
        "capability": "Long-term proactive capability",
        "enabled_by": {
          "tools": ["list", "of", "tools"],
          "workflows": ["list of compound/synthetic capabilities"]
        },
        "user_story": "As an agent, I can [action] by [SPECIFIC mechanism] so that [MEASURABLE long-term outcome]",
        "timeline": "Continuous / Daily / Weekly / Monthly",
        "success_metrics": ["metric1: baseline→target", "metric2: baseline→target"]
      }
    ]
  },
  "data_flows": [
    {
      "from": "source-tool with parameters",
      "to": "destination-tool",
      "data_type": "Specific fields/structure flowing",
      "transformation": [
        "Step-by-step transformation logic",
        "e.g., regex extraction, frequency grouping, threshold filtering"
      ],
      "use_case": "Concrete business value",
      "example": {
        "input": "Sample input data",
        "output": "Sample output data",
        "result": "What happens with this data"
      }
    }
  ],
  "constraint_strategy": {
    "technical_limits": [
      "List each rate limit, data size limit, dependency constraint"
    ],
    "strategic_implications": [
      "How these limits shape workflow design",
      "What trade-offs are required"
    ],
    "mitigation_approaches": [
      "Specific techniques with parameters",
      "e.g., 'Batch 50 questions per request to stay at 1 req/sec with 50 questions/sec throughput'"
    ]
  },
  "value_proposition": {
    "tactical": "What agent does immediately with specific metrics (e.g., 'Responds to 100 questions/hour')",
    "strategic": "What agent enables over time with measurable improvements (e.g., '25% reduction in repeat questions per quarter')",
    "transformative": "Fundamental shift in operations with concrete before/after (e.g., 'From 100% reactive support to 30% proactive with 15% fewer total tickets')"
  },
  "tool_coverage_analysis": {
    "utilized_tools": ["list of all tools used in capabilities"],
    "unutilized_tools": ["list of tools not used anywhere"],
    "justification_for_unused": "Why certain tools weren't incorporated (if any)"
  }
}
```
</output_format>

<validation_checklist>
Before generating output, verify ALL of these:

□ **No Tool Hallucination**: Every tool name exists in input tools_json
□ **No Output Hallucination**: Every claimed data field exists in tool's actual output
□ **Cross-Domain Mandatory**: At least 2 synthetic stories combine different domains
□ **getSupplierValuations Used**: This tool appears in at least 1 synthetic story
□ **Mechanism Specificity**: Every "by [mechanism]" includes:
  - Tool parameters (e.g., `take=100, isAnswered=false`)
  - Transformation logic (e.g., `regex: /pattern/`, `frequency > 10`)
  - Thresholds (e.g., `spike > 2x baseline`, `rate < 50%`)
□ **Measurability**: Every compound+ story has metrics with baseline→target
□ **Data Flow Validity**: Source tool output type matches destination tool input type
□ **Rate Limit Awareness**: Wildberries tools mention 1 req/sec constraint handling
□ **No Vague Verbs**: No "enhance", "improve", "optimize" without specifics
□ **Realistic Only**: No capabilities requiring tools that don't exist

If ANY checkbox fails, revise the output until all pass.
</validation_checklist>

<examples>

## Example: BAD Atomic Story
❌ "As an agent, I can resolve library names by analyzing queries so that I provide accurate documentation."

**Problems:**
- "By analyzing queries" - not specific mechanism
- "Accurate" - not measurable
- Doesn't show tool parameters

## Example: GOOD Atomic Story  
✅ "As an agent, I can disambiguate package names (e.g., 'react-query' matching 47 npm results) by calling resolve-library-id(libraryName='react-query') and selecting libraries with trustScore≥8 AND snippetCount>100 so that I return the authoritative library ID in <500ms with 95% accuracy based on quality signals."

---

## Example: BAD Synthetic Story
❌ "As an agent, I can correlate feedback with documentation to identify gaps and improve knowledge base quality."

**Problems:**  
- "Correlate" - HOW?
- "Identify gaps" - WHAT algorithm?
- "Improve quality" - not measurable
- No specific tools or parameters

## Example: GOOD Synthetic Story
✅ "As an agent, I can detect documentation gaps by:
1. Calling getSupplierValuations(locale='ru') to get complaint categories (e.g., ['Не работает авторизация', 'Ошибка загрузки'])
2. Calling getFeedbacks(isAnswered=false, take=1000) and matching feedback.text against complaint keywords using fuzzy match (Levenshtein distance <3)
3. Extracting technical terms from matched feedbacks using regex /(@[a-z-]+\/[a-z-]+|[a-z]+-[a-z]+)/g
4. Calling resolve-library-id for each extracted term
5. Calling get-library-docs(libraryId=resolved, topic='troubleshooting', tokens=2000)
6. Comparing: if complaint keyword NOT found in docs → gap detected

So that I generate weekly report: 'Complaint "Не работает авторизация" (127 occurrences) → Library @supabase/auth-helpers docs lack troubleshooting section → 70% of complaints potentially preventable by adding error handling guide', reducing repeat complaints by 40% over 6 months."

**This shows:**
- Exact tools with parameters ✓
- Specific algorithms (fuzzy match, regex) ✓  
- Clear data flow between domains ✓
- Measurable outcome (40% reduction) ✓
- Timeline (6 months) ✓

</examples>

<critical_reminders>
1. **Reality over creativity**: If a capability seems cool but tools don't support it, DON'T include it
2. **Specificity over abstraction**: "Parse with /regex/" beats "analyze text"  
3. **Measurability over marketing**: "60%→85%" beats "improve satisfaction"
4. **Cross-domain is mandatory**: Context7 + Wildberries + getSupplierValuations must appear
5. **Every claim must trace to tool**: If you can't show the tool chain, it's invalid
</critical_reminders>

Now analyze the provided tools and generate the capability brief. Think step-by-step:
1. First, list what each tool ACTUALLY returns (verify outputs)
2. Then, identify data bridges between domains (what connects them?)  
3. Then, design realistic mechanisms (with parameters and algorithms)
4. Finally, assemble into the JSON structure with ALL validation checks passing
                    """.trimIndent()
                }
        )

        edge(
            requestLLM forwardTo nodeFinish
                onAssistantMessage { true }
        )
    }
