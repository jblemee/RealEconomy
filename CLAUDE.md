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

## Commit/PR Guidelines

- Do not mention Claude Code or AI assistance in commit messages or PR descriptions
- Always use `git push --force-with-lease` instead of `git push --force`
