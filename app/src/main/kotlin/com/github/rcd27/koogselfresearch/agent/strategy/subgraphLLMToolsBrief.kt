package com.github.rcd27.koogselfresearch.agent.strategy

import ai.koog.agents.core.agent.context.DetachedPromptExecutorAPI
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphBuilderBase
import ai.koog.agents.core.dsl.builder.AIAgentSubgraphDelegate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.serialization.serializeToolDescriptorsToJsonString
import ai.koog.agents.ext.agent.subgraphWithVerification
import ai.koog.prompt.markdown.markdown
import ai.koog.prompt.xml.xml
import kotlinx.serialization.Serializable

@Serializable
data class ToolInventory(
    @property:LLMDescription("Human-readable reach-text analysis of tool capabilities and cross-domain connections.")
    val detailedAnalysis: String,
    @property:LLMDescription("Structured JSON output of tool capabilities and cross-domain connections.")
    val structuredSummary: ToolInventorySummary
)

@Serializable
data class ToolInventorySummary(
    @property:LLMDescription("Domains covered by tools.")
    val domains: List<Domain>,
    @property:LLMDescription("Data bridges between domains.")
    val dataBridges: List<DataBridge>,
    @property:LLMDescription("Constraints applied to tools.")
    val keyConstraints: List<Constraint>,
    @property:LLMDescription("Cross-domain opportunities identified. Just names, details in text.")
    val crossDomainOpportunities: List<String>,
    @property:LLMDescription("Operational characteristics and runtime considerations.")
    val operationalCharacteristics: OperationalCharacteristics,
    @property:LLMDescription("Implementation priority and phasing recommendations.")
    val implementationPriority: ImplementationPriority,
    @property:LLMDescription("Risk assessment and mitigation strategies.")
    val riskAssessment: RiskAssessment
)

@Serializable
data class Domain(
    @property:LLMDescription("Name of domain.")
    val name: String,
    @property:LLMDescription("Tools that operate in this domain.")
    val tools: List<String>,
    @property:LLMDescription("read/write/aggregate/etc")
    val primaryOperations: List<String>,
    @property:LLMDescription("Business criticality level: high/medium/low")
    val criticality: String,
    @property:LLMDescription("Performance pattern: read_heavy/write_heavy/mixed")
    val performanceCharacteristics: String,
    @property:LLMDescription("Whether this domain contains customer-facing operations")
    val customerFacing: Boolean
)

@Serializable
data class DataBridge(
    @property:LLMDescription("Shared field between domains, e.g., `nmId` or `customerId`.")
    val sharedField: String,
    @property:LLMDescription("List of tools that share this field. E.g., `customer-lookup` and `product-lookup`.")
    val tools: List<String>,
    @property:LLMDescription("List of domains that have data-bridge")
    val crossDomain: List<Domain>,
    @property:LLMDescription("Data freshness requirements: realtime/near_realtime/batch")
    val dataFreshness: String,
    @property:LLMDescription("Whether data transformation is required when bridging domains")
    val transformationRequired: Boolean,
    @property:LLMDescription("Recommended synchronization strategy")
    val syncStrategy: String
)

@Serializable
data class Constraint(
    @property:LLMDescription("Name of constraints applied to tools.")
    val name: String,
    @property:LLMDescription("Description of constraint.")
    val constraintDetails: String,
    @property:LLMDescription("Severity level: critical/high/medium/low")
    val severity: String,
    @property:LLMDescription("Impact area: performance/functionality/reliability")
    val impactArea: String,
    @property:LLMDescription("Recommended mitigation strategy")
    val mitigation: String
)

@Serializable
data class OperationalCharacteristics(
    @property:LLMDescription("Categorization of rate limit strictness")
    val rateLimitTiers: List<String>,
    @property:LLMDescription("Recommended architectural patterns")
    val recommendedArchPatterns: List<String>,
    @property:LLMDescription("Suggested polling intervals for monitoring tools")
    val recommendedPollingIntervals: List<PollingInterval>,
    @property:LLMDescription("Tools suitable for batch processing")
    val batchProcessingSupport: List<String>,
    @property:LLMDescription("Known failure modes and recovery strategies")
    val failureModes: List<FailureMode>
)

@Serializable
data class PollingInterval(
    @property:LLMDescription("Polling interval in seconds")
    val interval: Int,
    @property:LLMDescription("Description of polling interval")
    val description: String
)

@Serializable
data class FailureMode(
    @property:LLMDescription("Type of failure")
    val failureType: String,
    @property:LLMDescription("Tools affected")
    val affectedTools: List<String>,
    @property:LLMDescription("Recovery strategy")
    val recoveryStrategy: String
)

@Serializable
data class ImplementationPriority(
    @property:LLMDescription("Tools and capabilities for immediate implementation")
    val immediateImplementation: List<String>,
    @property:LLMDescription("Tools for medium-term implementation")
    val mediumImplementation: List<String>,
    @property:LLMDescription("Advanced capabilities for long-term roadmap")
    val longTermImplementation: List<String>,
    @property:LLMDescription("Quick wins that deliver immediate value")
    val quickWins: List<String>
)

@Serializable
data class RiskAssessment(
    @property:LLMDescription("High-risk operations requiring special handling")
    val highRiskOperations: List<RiskOperation>,
    @property:LLMDescription("Potential performance bottlenecks")
    val performanceBottlenecks: List<String>,
    @property:LLMDescription("Compliance and data handling considerations")
    val complianceConsiderations: List<String>,
    @property:LLMDescription("Data consistency requirements across domains")
    val dataConsistencyRequirements: List<String>
)

@Serializable
data class RiskOperation(
    @property:LLMDescription("Name of risky operation")
    val operation: String,
    @property:LLMDescription("Type of risk")
    val riskType: String,
    @property:LLMDescription("Recommended safety measures")
    val safetyMeasures: List<String>
)

@Serializable
data class AgentSkillsSynthesis(
    @property:LLMDescription("List of skills that use ONLY the provided API tools without external systems or storage")
    val discoveredSkills: List<AgentSkill>,
    @property:LLMDescription("Categories organizing skills by their operational scope")
    val skillCategories: List<SkillCategory>,
    @property:LLMDescription("Dependencies ensuring skills build upon available tools only")
    val skillDependencies: List<SkillDependency>
)

@Serializable
data class AgentSkill(
    @property:LLMDescription("Unique ID for the skill using only available tools")
    val id: String,
    @property:LLMDescription("Name describing the skill's function with available tools")
    val name: String,
    @property:LLMDescription("Category ID from predefined set")
    val category: String,
    @property:LLMDescription("What this skill accomplishes using ONLY provided API endpoints")
    val description: String,
    @property:LLMDescription("User story that can be implemented with available tools only")
    val userStory: UserStory,
    @property:LLMDescription("EXACT tool names from the provided list - no external tools")
    val toolsRequired: List<String>,
    @property:LLMDescription("Domains covered by the available tools used")
    val domains: List<String>,
    @property:LLMDescription("Skills that must be implemented first using same tool constraints")
    val prerequisites: List<String>,
    @property:LLMDescription("Complexity based on available tool combinations")
    val complexity: String,
    @property:LLMDescription("Concrete examples implementable with current tools only")
    val usageExamples: List<String>
)

@Serializable
data class UserStory(
    @property:LLMDescription("The role/persona who benefits from this skill (e.g., 'as a seller')")
    val asA: String,
    @property:LLMDescription("The desired capability or action the skill enables")
    val iWant: String,
    @property:LLMDescription("The business value or outcome achieved by this skill")
    val soThat: String,
    @property:LLMDescription("Specific conditions that must be met for the skill to work correctly")
    val acceptanceCriteria: List<String>
)

@Serializable
data class SkillCategory(
    @property:LLMDescription("Unique category identifier (e.g., 'customer-support')")
    val id: String,
    @property:LLMDescription("Human-readable category name")
    val name: String,
    @property:LLMDescription("Brief explanation of what this category encompasses")
    val description: String,
    @property:LLMDescription("List of skill IDs belonging to this category")
    val skills: List<String>
)

@Serializable
data class SkillDependency(
    @property:LLMDescription("The skill that has dependencies")
    val skillId: String,
    @property:LLMDescription("List of skill IDs that must be implemented first")
    val dependsOn: List<String>,
    @property:LLMDescription("Type of dependency: 'hard' (blocks execution), 'soft' (recommended), or 'data' (shares data)")
    val dependencyType: String
)

fun standaloneLLMToolsBrief() = strategy<String, String>("llm_tools_brief") {
    val toolRegistryBrief by subgraphLLMToolsBrief()
    nodeStart then toolRegistryBrief then nodeFinish
}

// FIXME: in current implementation we look at WHOLE llm.tools. What if there are hundreds of tools?
// FIXME: shift to structured output
// FIXME: 2 MCP servers gave 2m waiting time with very deep analysis regarding their DOMAINS
// TODO: provide response schemas for Tools outputs
@OptIn(DetachedPromptExecutorAPI::class)
fun AIAgentSubgraphBuilderBase<*, *>.subgraphLLMToolsBrief(): AIAgentSubgraphDelegate<String, String> =
    subgraph("tool_registry_brief") {
        val addSystemPrompt by node<String, Unit>("task") { input ->
            llm.writeSession {
                appendPrompt {
                    system(
                        xml {
                            tag("task") {
                                +"""
                                    You are analyzing the agent's available tools to generate a high-level summary of capabilities for self-discovery and skill generation purposes.
                                    Your goal is to create a CREATIVE yet REALISTIC overview that reveals non-obvious cross-domain combinations while staying strictly within actual tool capabilities.
                                    CRITICAL: This analysis must work for ANY set of tools. Do NOT assume specific domains or tool names.
                                """.trimIndent()
                            }
                            tag("additional_instructions") {
                                +input
                            }
                        }
                    )
                }
            }
        }

        val extractToolDescriptors by node<Unit, Unit>("extract_tool_descriptors") { input ->
            val tools = serializeToolDescriptorsToJsonString(this.llm.tools)
            println(tools)
            llm.writeSession {
                appendPrompt {
                    user(
                        xml {
                            tag("tools") {
                                +tools
                            }
                        }
                    )
                }
            }
        }

        val toolClassification by node<Unit, ToolInventory>("tool_inventory") {
            llm.writeSession {
                appendPrompt {
                    user(
                        markdown {
                            h2("Phase 1: Tool Inventory & Classification")
                            br()
                            bulleted {
                                item("**Exact inputs**: Required vs optional parameters, data types, format constraints")
                                item("**Exact outputs**: Data structure, fields returned, state changes (read vs write)")
                                item("**Operational constraints**: Rate limits, dependencies, sequencing requirements")
                                item("**Domain signals**: What domain does this tool operate in? (infer from description, not assume)")
                            }
                            br()
                            text(
                                """
                            Then, **automatically discover domains** by clustering tools:
                            - Group tools that share similar data types (e.g., "customer feedback", "documentation", "analytics")
                            - Identify read vs write operations
                            - Detect data type compatibility for flows (can output of Tool A feed input of Tool B?)    
                                """.trimIndent()
                            )
                        }
                    )
                }
                requestLLMStructured<ToolInventory>().getOrNull()!!.structure
            }
        }

        val agentSkillsSynthesys by node<ToolInventory, Pair<ToolInventory, AgentSkillsSynthesis>>("agent_skills_synthesis") { input ->
            llm.writeSession {
                appendPrompt {
                    user(
                        xml {
                            markdown {
                                h2("Phase 2: Cross-Domain Synthesis Discovery")
                                br()
                                text("**Step 1: Identify distinct domains**")
                                text("Look for natural clustering:")
                                bulleted {
                                    item("Information retrieval (search, fetch, query operations)")
                                    item("Customer interaction (feedback, questions, messages)")
                                    item("Content management (create, update, publish)")
                                }
                                br()
                                text("**Step 2: Find data bridges**")
                                text("For each pair of domains, ask:")
                                bulleted {
                                    item("Can data from Domain A enrich operations in Domain B?")
                                    item("Do tools share compatible data types?")
                                    item("Can aggregates from one domain trigger actions in another?")
                                    item("Can classification from one domain inform retrieval in another?")
                                }
                                br()
                                text("**Step 3: Detect synthesis patterns**")
                                text("Common high-value patterns:")
                                bulleted {
                                    item("**Enrich & Respond**: Retrieve external data → enhance internal response")
                                    item("**Classify & Route**: Categorize input → fetch domain-specific resources → deliver")
                                    item("**Monitor & Act**: Detect anomaly/spike → fetch context → broadcast solution")
                                    item("**Feedback Loop**: Collect interactions → analyze gaps → update knowledge base")
                                    item("**Proactive Mitigation**: Aggregate trends → predict issues → preemptive action")
                                }
                            }
                            tag("restrictions") {
                                +"""
                                    Use ONLY the tools you have to provide comprehensive AgentSkillsSynthesis
                                """.trimIndent()
                            }
                        }
                    )
                }
                val structure = requestLLMStructured<AgentSkillsSynthesis>().getOrThrow().structure
                return@writeSession input to structure
            }
        }

        val verifyAgentSkills by subgraphWithVerification<AgentSkillsSynthesis>(
            tools = emptyList(),
        ) { agentSkills: AgentSkillsSynthesis ->
            """
        <validation_checklist>
        **CORE VALIDATION**
        □ **Tool Existence**: Every tool in skills exists in provided tools list
        □ **Parameter Compliance**: Tool usage matches documented parameters and types  
        □ **Domain Grounding**: All domains derived from actual tool capabilities
        □ **Data Flow Validity**: Source outputs compatible with destination inputs
        
        **SYNTHESIS QUALITY**
        □ **Cross-Domain Mandatory**: All skills combine ≥2 different domains
        □ **Tool Coverage**: ≥70% READ and ≥50% WRITE tools utilized
        □ **Pattern Implementation**: Each skill implements a recognized synthesis pattern
        □ **Constraint Handling**: Rate limits and operational constraints explicitly addressed
        
        **PRACTICALITY CHECKS**
        □ **No Vagueness**: All mechanisms include specific parameters and logic
        □ **Measurability**: Success criteria include quantifiable metrics
        □ **Realistic Scope**: No capabilities beyond provided tool functionality
        □ **Skill Coherence**: Prerequisites form logical dependency chain
        
        **SCORING**: Each check = 5 points, total ≥70% required for validation
        </validation_checklist>
        
        <agent_skills_synthesys>
                $agentSkillsSynthesys
        </agent_skills_synthesys>
            """.trimIndent()
        }

        val generateMarkdown by node<Pair<ToolInventory,AgentSkillsSynthesis>, String>("generate_markdown") { input ->
            llm.writeSession {
                appendPrompt {
                    user(
                        """
                        You are a documentation generator.  
                        Your task is to produce a beautifully structured, highly readable and well-formatted Markdown document that describes the agent’s skills.

                        Your output MUST be a single valid Markdown document.  
                        Do not include any explanations or preambles.  
                        Only output Markdown content.

                        ## Requirements for Markdown formatting:

                        ### Top-level structure:
                        1. `# Agent Skills Overview` — main title  
                        2. Table of contents  
                        3. Section: `Skill Categories`  
                        4. Section: `Discovered Skills`  
                        5. Section: `Skill Dependencies`

                        ### Formatting rules:
                        - Use headings up to `###` for clean structure.
                        - Use tables where appropriate.
                        - Use lists for acceptance criteria, usage examples, tools, domains.
                        - Add emojis to make navigation easier, but don’t overload.
                        - Ensure all category and skill IDs appear in the document.
                        - Add small horizontal rules (`---`) between major sections.
                        - Wrap long descriptions cleanly.
                        - Keep the entire Markdown strictly deterministic and well-structured.

                        ### Layout details:

                        ## Skill Categories
                        For each category include:
                        - `### {Category Name} ({id})`
                        - Description paragraph
                        - Table of skills inside this category

                        ## Discovered Skills
                        For each skill:
                        - `### {Skill Name} ({id})`
                        - Badge line: category, tools, complexity, domains
                        - Description
                        - Subsection **User Story**, with formatted fields:
                          - As a…
                          - I want…
                          - So that…
                        - Subsection **Acceptance Criteria** — bullet list
                        - Subsection **Usage Examples** — bullet list
                        - Horizontal rule

                        ## Skill Dependencies
                        Generate a table:

                        | Skill | Depends On | Type |
                        |-------|------------|------|
                        | skillId | comma-separated | dependencyType |

                        ### Final note:
                        At the bottom add a closing line:
                        “_This document was automatically generated from AgentSkillsSynthesis._”

                        ### VERY IMPORTANT
                        - Do NOT invent new skills or categories.
                        - Do NOT omit any skills or fields.
                        - Preserve all names exactly as in input.
                        - The output must be deterministic and reproducible.

                        Now generate the Markdown using the provided AgentSkillsSynthesis as the single source of truth.
                    """.trimIndent()
                    )
                    xml {
                        tag("tool_inventory") {
                            +input.first.toString()
                        }
                        tag("agent_skills_synthesis") {
                            +input.second.toString()
                        }
                    }
                }
                requestLLM().content
            }
        }

        nodeStart then
            addSystemPrompt then
            extractToolDescriptors then
            toolClassification then
            agentSkillsSynthesys then
            generateMarkdown

        edge(
            generateMarkdown forwardTo nodeFinish
                transformed {
                it.toString()
            }
        )
    }
