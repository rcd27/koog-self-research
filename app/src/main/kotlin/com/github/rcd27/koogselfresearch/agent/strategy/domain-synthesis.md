<task>
You are analyzing the agent's available tools to generate a high-level summary of capabilities for self-discovery and skill generation purposes.

Your goal is to create a CREATIVE yet REALISTIC overview that reveals non-obvious cross-domain combinations while staying strictly within actual tool capabilities.

CRITICAL: This analysis must work for ANY set of tools. Do NOT assume specific domains or tool names.
</task>

<input>
Available tools specification:
$extractedToolDescriptors
</input>

<analysis_framework>

## Phase 1: Tool Inventory & Classification

For each tool, extract with precision:
- **Exact inputs**: Required vs optional parameters, data types, format constraints
- **Exact outputs**: Data structure, fields returned, state changes (read vs write)
- **Operational constraints**: Rate limits, dependencies, sequencing requirements
- **Domain signals**: What domain does this tool operate in? (infer from description, not assume)

Then, **automatically discover domains** by clustering tools:
- Group tools that share similar data types (e.g., "customer feedback", "documentation", "analytics")
- Identify read vs write operations
- Detect data type compatibility for flows (can output of Tool A feed input of Tool B?)

## Phase 2: Cross-Domain Synthesis Discovery

**Step 1: Identify distinct domains**
Look for natural clustering:
- **Information retrieval** (search, fetch, query operations)
- **Customer interaction** (feedback, questions, messages)
- **Content management** (create, update, publish)
- **Analytics/monitoring** (count, aggregate, detect patterns)
- **Quality/taxonomy** (categories, classifications, valuations)
- **External integrations** (APIs, webhooks, third-party services)

**Step 2: Find data bridges**
For each pair of domains, ask:
- Can data from Domain A enrich operations in Domain B?
- Do tools share compatible data types? (text → text, IDs → IDs, timestamps → timestamps)
- Can aggregates from one domain trigger actions in another?
- Can classification from one domain inform retrieval in another?

**Step 3: Detect synthesis patterns**

Common high-value patterns:
1. **Enrich & Respond**: Retrieve external data → enhance internal response
2. **Classify & Route**: Categorize input → fetch domain-specific resources → deliver
3. **Monitor & Act**: Detect anomaly/spike → fetch context → broadcast solution
4. **Feedback Loop**: Collect interactions → analyze gaps → update knowledge base
5. **Proactive Mitigation**: Aggregate trends → predict issues → preemptive action

MANDATORY: You MUST find at least 2 cross-domain synthesis opportunities. If domains seem isolated, look harder:
- Text fields can contain mentions of entities from other domains (extract via NLP/regex)
- Timestamps enable correlation (e.g., spike detection → contextual lookup)
- IDs can link records across systems (e.g., product ID → documentation → support tickets)
- Categories/taxonomies can classify content for targeted retrieval

## Phase 3: Realistic Mechanism Design

For each capability, specify the EXACT mechanism using only available tools:

### Data Extraction
- What fields? (e.g., `question.text`, `feedback.productId`)
- What parsing? (e.g., `regex: /pattern/`, `frequency analysis`, `fuzzy match`)
- What normalization? (lowercase, stemming, deduplication)

### Transformation
- How does Tool A output become Tool B input?
- What intermediate steps? (filter, aggregate, enrich)
- What validation? (check existence, verify format)

### Triggering Logic
- What thresholds? (e.g., `ratio > 2.0`, `count > 100`, `rate < 50%`)
- What timing? (continuous, hourly, daily, event-driven)
- What prioritization? (SLA-based, recency, frequency)

### Constraints Handling
- How to respect rate limits? (queuing, batching, throttling)
- How to handle failures? (retry logic, fallbacks, circuit breakers)
- How to optimize? (caching, prefetching, parallelization)

FORBIDDEN phrases without specifics:
- "Analyze trends" → Specify algorithm (time-series comparison, moving average, anomaly detection)
- "Correlate data" → Specify fields and matching logic (join on ID, text similarity, timestamp window)
- "Identify gaps" → Specify detection method (keyword absence, coverage <X%, answer rate <Y%)
- "Improve quality" → Specify metric and mechanism (increase from X% to Y% by doing Z)

</analysis_framework>

<critical_constraints>

## 1. Tool Output Reality Check

Before claiming data flows:
1. Read the tool description carefully
2. Identify EXACTLY what it returns (not what you wish it returned)
3. Verify data type compatibility for flows

**Example validation:**
- Tool returns `{hasNew: boolean}` → CANNOT flow to tool needing `string libraryName`
- Tool returns `[{id, text, date}]` → CAN flow to tool needing `string` via extraction from `text`

## 2. No Capability Invention

You can ONLY claim what tools ACTUALLY provide:

❌ FORBIDDEN if not in tool description:
- Version history / changelogs (unless tool explicitly provides)
- Template generation (unless tool has template management)
- Quality scoring (unless tool returns quality metrics)
- Historical trend tracking (unless tool stores/retrieves history)
- Write access to external systems (unless tool has POST/PATCH/PUT operations)

✅ ALLOWED with proper mechanism:
- "Compare counts across time windows" (if tool accepts dateFrom/dateTo parameters)
- "Extract keywords from text" (if tool returns text fields)
- "Detect volume spikes" (via mathematical comparison of count tool outputs)
- "Group by frequency" (via local processing of list tool outputs)

## 3. Universal Cross-Domain Integration

YOU MUST create at least TWO synthetic stories that combine tools from different domains.

**How to discover cross-domain opportunities:**

### Pattern A: Entity Extraction Bridge
If Domain A has text fields AND Domain B has entity lookup:
- Extract entity mentions from Domain A text (regex, NLP)
- Query Domain B with extracted entities
- Enrich Domain A operations with Domain B data

### Pattern B: Taxonomy Classification Bridge
If Domain A has classification/categories AND Domain B has unstructured content:
- Classify Domain B content using Domain A taxonomy
- Aggregate by category to find patterns
- Target Domain B actions based on classification results

### Pattern C: Anomaly Detection Bridge
If Domain A has counting/metrics AND Domain B has detailed records:
- Detect anomalies in Domain A metrics (spikes, drops)
- Fetch Domain B details for anomaly time window
- Analyze and act on correlation

### Pattern D: Feedback Loop Bridge
If Domain A collects interactions AND Domain B manages knowledge:
- Aggregate Domain A interaction patterns
- Identify Domain B knowledge gaps (high questions, low coverage)
- Propose Domain B improvements based on Domain A data

**Critical**: If you cannot find cross-domain opportunities, you have not analyzed deeply enough. EVERY tool set has them - look at data type compatibility and business logic connections.

## 4. Measurability Requirements

Every compound/synthetic/strategic story MUST include concrete metrics:

✅ GOOD metrics (specific, measurable, time-bound):
- "Reduce metric from X to Y within Z timeframe"
- "Process N items in <T time"
- "Detect patterns using threshold T (e.g., ratio > 2.0, count > 100)"
- "Improve rate from X% to Y% per period"
- "Reduce volume by Z% over timeframe"

❌ BAD (vague, unmeasurable):
- "Improve satisfaction" (what metric? baseline? target?)
- "Enhance quality" (which quality dimension? how measured?)
- "Optimize performance" (latency? throughput? accuracy?)
- "Better experience" (not measurable)

**Metric components:**
- Baseline: current state with number
- Target: goal state with number
- Timeframe: when target is achieved
- Mechanism: how improvement is measured

## 5. Rate Limit & Constraint Strategy

For every compound/synthetic capability:
- Identify ALL rate limits from tool descriptions
- Design explicit mitigation (batching, queuing, throttling, caching)
- Show how constraints shape workflow timing
- Provide concrete parameters (queue size, batch size, cache TTL)

</critical_constraints>

<output_format>
Return a structured JSON object:

```json
{
  "domain_discovery": {
    "identified_domains": [
      {
        "domain_name": "Auto-detected domain name",
        "tools": ["tool-1", "tool-2"],
        "primary_operations": ["read", "write", "aggregate"],
        "key_data_types": ["data types this domain works with"]
      }
    ],
    "domain_isolation_score": "LOW/MEDIUM/HIGH - how isolated domains are from each other",
    "cross_domain_potential": "Assessment of synthesis opportunities"
  },
  
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
        "constraint_handling": "How rate limits and constraints are managed"
      }
    ],
    
    "synthetic": [
      {
        "capability": "Cross-domain capability",
        "domains_involved": ["domain-A", "domain-B"],
        "tools": ["tool-from-domain-A", "tool-from-domain-B"],
        "user_story": "As an agent, I can [action] by [SPECIFIC cross-domain mechanism] so that [MEASURABLE outcome]",
        "synthesis_pattern": "Which pattern from Phase 2 (Enrich & Respond / Classify & Route / etc.)",
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
        "success_metrics": [
          "metric1: baseline→target over timeframe",
          "metric2: baseline→target over timeframe"
        ]
      }
    ]
  },
  
  "data_flows": [
    {
      "from": "source-tool with parameters",
      "to": "destination-tool",
      "data_type": "Specific fields/structure flowing",
      "compatibility_check": "Verified: source output type matches destination input type",
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
      "e.g., 'Batch N items per request to achieve X throughput at Y req/sec'"
    ]
  },
  
  "value_proposition": {
    "tactical": "What agent does immediately with specific metrics (e.g., 'Responds to N items per hour')",
    "strategic": "What agent enables over time with measurable improvements (e.g., 'X% reduction in Y per quarter')",
    "transformative": "Fundamental shift in operations with concrete before/after (e.g., 'From 100% reactive to X% proactive with Y% fewer total interactions')"
  },
  
  "tool_coverage_analysis": {
    "total_tools": N,
    "utilized_tools": ["list of all tools used in capabilities"],
    "unutilized_tools": ["list of tools not used anywhere"],
    "utilization_rate": "X%",
    "justification_for_unused": "Why certain tools weren't incorporated (if any - e.g., insufficient cross-domain bridges, isolated functionality, redundant with other tools)"
  }
}
```
</output_format>

<validation_checklist>
Before generating output, verify ALL of these:

□ **No Tool Hallucination**: Every tool name exists in input tools_json
□ **No Output Hallucination**: Every claimed data field exists in tool's actual output
□ **No Domain Assumptions**: Domains discovered from tools, not assumed
□ **Cross-Domain Mandatory**: At least 2 synthetic stories combine different domains
□ **High Utilization**: ≥80% of provided tools incorporated (aim for 100%)
□ **Mechanism Specificity**: Every "by [mechanism]" includes:
- Tool parameters (e.g., `param=value`)
- Transformation logic (e.g., `regex: /pattern/`, `aggregation: sum/count`)
- Thresholds (e.g., `if X > Y`, `when rate < Z%`)
  □ **Measurability**: Every compound+ story has metrics with baseline→target→timeframe
  □ **Data Flow Validity**: Verified compatibility between source output and destination input
  □ **Constraint Awareness**: Rate limits and constraints explicitly handled
  □ **No Vague Verbs**: No "enhance", "improve", "optimize" without specifics
  □ **Realistic Only**: No capabilities requiring functionality not present in tools
  □ **Synthesis Patterns**: Each synthetic story maps to a recognized pattern

If ANY checkbox fails, revise until all pass.
</validation_checklist>

<examples>

## Example: GOOD Atomic Story (universal pattern)
✅ "As an agent, I can retrieve items matching criteria by calling [tool-name](param1=value1, param2=value2, limit=N) and filtering by [specific-field] so that I obtain M relevant results in <T seconds with X% accuracy based on [quality-signal]."

## Example: GOOD Synthetic Story (universal pattern)
✅ "As an agent, I can detect high-impact issues by:
1. Calling [metric-tool](timeWindow='24h') to get current volumes
2. Comparing against [metric-tool](timeWindow='7d') baseline using ratio formula
3. When current/baseline > 2.0, fetching details via [detail-tool](filter='recent', limit=100)
4. Extracting entity references from [text-field] using regex [pattern]
5. Looking up entities via [lookup-tool](entityName=extracted)
6. Retrieving relevant context via [context-tool](entityId=resolved, scope='troubleshooting')
7. Broadcasting guidance via [action-tool](targetId=affectedItems, content=composed)

So that I reduce time-to-resolution from X hours to Y hours and prevent Z% of follow-up inquiries."

**This shows:**
- No hardcoded domains ✓
- Exact tool parameters ✓
- Clear algorithms (ratio formula, regex) ✓
- Data flow between domains ✓
- Measurable outcomes ✓

</examples>

<critical_reminders>
1. **Discover, don't assume**: Domains come from analyzing tools, not prior knowledge
2. **Reality over creativity**: If tools don't support it, don't include it
3. **Specificity over abstraction**: Show the actual mechanism with parameters
4. **Universal patterns**: Use synthesis patterns that work for ANY domain combination
5. **Every claim traces to tools**: If you can't show the tool chain, it's invalid
6. **Cross-domain is mandatory**: Every tool set has them - find the data bridges
7. **Utilization goal**: Aim to incorporate 100% of tools; justify any unused

Think step-by-step:
1. Classify tools into domains (infer from descriptions)
2. Find data type overlaps between domains
3. Design realistic cross-domain mechanisms
4. Validate every capability against actual tool specifications
5. Ensure all metrics are measurable with baselines and targets
   </critical_reminders>
