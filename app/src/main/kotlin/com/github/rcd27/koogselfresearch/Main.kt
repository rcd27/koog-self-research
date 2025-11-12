package com.github.rcd27.koogselfresearch

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("mcp-servers")

    val configPath by parser.argument(
        ArgType.String,
        description = "Path to MCP servers configuration JSON file"
    )

    val verbose by parser.option(
        ArgType.Boolean,
        shortName = "v",
        description = "Enable verbose output"
    ).default(false)

    parser.parse(args)

    println("Loading configuration from: $configPath")
    if (verbose) {
        println("Verbose mode enabled")
    }

    val configFile = File(configPath)
    if (!configFile.exists()) {
        println("Error: Configuration file not found: $configPath")
        return
    }
}
