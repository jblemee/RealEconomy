# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RealEconomy is a Minecraft server-side economy mod supporting multiple currencies for Fabric, Forge, and NeoForge on Minecraft 1.21.4. It's a fork of the MultiEconomy mod.

## Build Commands

```bash
# Build all platforms (requires JDK 21+)
./gradlew build

# Build specific platform
./gradlew :fabric:build
./gradlew :forge:build
./gradlew :neoforge:build

# Run development client for testing
./gradlew :fabric:runClient
./gradlew :forge:runClient
./gradlew :neoforge:runClient
```

Build outputs go to `output/` directory with classifiers: `-fabric`, `-minecraftforge`, `-neoforge`.

## Architecture

This is a multi-platform Minecraft mod using the **Architectury** framework with Architectury Loom for cross-platform development.

### Module Structure

- **common/** - Platform-agnostic code (all business logic lives here)
- **fabric/** - Fabric-specific entry point and event registration
- **forge/** - Forge-specific entry point and event registration
- **neoforge/** - NeoForge-specific entry point and event registration

### Key Packages (in common/)

- `co.lemee.realeconomy` - Entry point (`RealEconomy.init()`) and error management
- `co.lemee.realeconomy.account` - Player account storage (`AccountManager`, `Account`, `AccountFile`)
- `co.lemee.realeconomy.command` - Brigadier command system (`BaseCommand` + subcommands)
- `co.lemee.realeconomy.config` - Configuration loading (`ConfigManager`, `Config`)
- `co.lemee.realeconomy.currency` - Currency definitions
- `co.lemee.realeconomy.permission` - LuckPerms integration

### Platform Entry Points

Each platform module has its own entry point class that:
1. Calls `RealEconomy.init()` to load config and initialize accounts
2. Registers platform-specific event handlers (player join)
3. Registers commands using `BaseCommand.register()`

### Data Storage

- Config stored in `config.json` via `ConfigManager`
- Player accounts stored as JSON files in `accounts/` directory (one file per UUID)
- Uses Gson for serialization with async file I/O via `Utils`

### Command System

Commands use Brigadier with base command `/realeconomy` (aliases: `/realeco`, `/reco`). Subcommands implement `SubCommandInterface` and are registered in `BaseCommand.createCommand()`.

## Dependencies

- **LuckPerms** - Required for permission management on all platforms
- **Placeholder API** - Fabric only, for placeholder support
- Uses official Mojang mappings

## Publishing to Modrinth

The Modrinth API key is stored in `.env` (not versioned). Project ID: `7Aa1dFtb`

To publish a new version, create one version per loader (fabric, forge, neoforge):

```bash
# Load API key
source .env

# Publish Fabric version
curl -X POST "https://api.modrinth.com/v2/version" \
  -H "Authorization: $MODRINTH_API_KEY" \
  -F 'data={
    "name": "Real Economy VERSION-fabric",
    "version_number": "VERSION-fabric",
    "changelog": "Changelog here",
    "dependencies": [],
    "game_versions": ["MC_VERSION"],
    "version_type": "release",
    "loaders": ["fabric"],
    "featured": true,
    "status": "listed",
    "project_id": "7Aa1dFtb",
    "file_parts": ["file"]
  }' \
  -F "file=@output/realeconomy-VERSION-fabric.jar"

# Repeat for forge (loaders: ["forge"]) and neoforge (loaders: ["neoforge"])
```

Replace `VERSION` with the version (e.g., `0.1.1+1.21.8`) and `MC_VERSION` with Minecraft version.

## Commit/PR Guidelines

- Do not mention Claude Code or AI assistance in commit messages or PR descriptions
- Always use `git push --force-with-lease` instead of `git push --force`
