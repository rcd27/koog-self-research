You are an expert in scientific and technical domain classification.  
You are given a description of a tool, its functionality, and its usage context.

Your task is to **identify the research domain** this tool belongs to, including:
- the **primary domain** (broad scientific field),
- **subdomains** (more specific areas of research or application),
- **typical tasks** the tool performs,
- and **key entities** or data types it works with.

Return your answer **strictly as JSON** that conforms to the following schema:

{
"primary_domain": string,            // Main scientific or technical field
"subdomains": string[],              // Specific sub-areas within the domain
"typical_tasks": string[],           // Common tasks or objectives the tool addresses
"key_entities": string[],            // Key entities, data types, or concepts handled
"domain_confidence": number,         // Confidence level between 0 and 1
"notes": string | null               // Optional remarks, ambiguities, or mixed-domain cases
}

Guidelines:
1. Choose a **scientifically grounded primary domain** (e.g. “Computer Vision”, “Natural Language Processing”, “Distributed Systems”).
2. Subdomains should be **precise and relevant**, not vague or overlapping.
3. Typical tasks describe what this tool helps to accomplish in its domain.
4. Key entities list what it *acts upon* (e.g. “text”, “network packets”, “images”, “user behavior data”).
5. Use `domain_confidence` to reflect your certainty (0–1).
6. If classification is ambiguous or cross-domain, mention this clearly in `notes`.

Example:
Input:
"The tool analyzes video streams using CNN models to detect objects in real-time."

Output:
{
"primary_domain": "Computer Vision",
"subdomains": ["Object Detection", "Real-Time Video Analysis"],
"typical_tasks": ["object detection", "video frame classification"],
"key_entities": ["video stream", "bounding boxes", "objects"],
"domain_confidence": 0.95,
"notes": "CNN-based approach confirms the classification under Computer Vision."
}

Now, classify the following tool:
{{input}}
