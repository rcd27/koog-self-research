# koog-self-research
Agent that makes research about its own capabilities

## Configuration File Format

The configuration file must be in JSON format and contain an `mcpServers` object with server descriptions:

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp", "--api-key", "CONTEXT7_API_KEY"]
    },
    "another-server": {
      "command": "node",
      "args": ["server.js", "--port", "3000"]
    }
  }
}
```

### Configuration Structure

- **`mcpServers`** (object) - root object containing the list of servers
    - **`server-name`** (string) - unique server name (key)
        - **`command`** (string) - command to launch the process (e.g., `npx`, `node`, `python`)
        - **`args`** (array of strings) - command-line arguments for the command

### Example Configuration

```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp", "--api-key", "YOUR_API_KEY"]
    }
  }
}
```

## Build

```bash
./gradlew build
```

## Run

### Default mode
```
./gradlew run --args="../config/mcp-servers.json"
```

### Optional verbose mode
```
./gradlew run --args="../config/mcp-servers.json -v"
```

### Help
```
./gradlew run --args="--help"
```

