# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RealEconomy is a Minecraft server-side economy mod supporting multiple currencies for Fabric, Forge, and NeoForge. It's a fork of the MultiEconomy mod.

**Current version**: Minecraft 1.21.10 (branch `1.21.10`)

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

## GitHub Releases

Create a GitHub release with the build artifacts:

```bash
gh release create vMC_VERSION \
  --title "vMC_VERSION" \
  --notes "## Port to Minecraft MC_VERSION

### Changes
- List changes here

### Downloads
- **Fabric**: \`realeconomy-VERSION-fabric.jar\`
- **Forge**: \`realeconomy-VERSION-minecraftforge.jar\`
- **NeoForge**: \`realeconomy-VERSION-neoforge.jar\`

Also available on [Modrinth](https://modrinth.com/mod/realeconomy/versions?g=MC_VERSION)" \
  --target BRANCH_NAME \
  output/realeconomy-VERSION-fabric.jar \
  output/realeconomy-VERSION-minecraftforge.jar \
  output/realeconomy-VERSION-neoforge.jar
```

Replace `MC_VERSION` with Minecraft version (e.g., `1.21.8`), `VERSION` with full version (e.g., `0.1.1+1.21.8`), and `BRANCH_NAME` with the release branch.

## Publishing to CurseForge

The CurseForge API key and project ID are stored in `.env`. Project ID: `1263401`

Game version IDs (find new ones via `https://minecraft.curseforge.com/api/game/versions`):
- **Loaders**: Fabric=`7499`, Forge=`7498`, NeoForge=`10150`
- **Minecraft versions**: check API for current IDs (e.g., 1.21.8=`13620`)

```bash
# Load API key
source .env

# Publish Fabric version
curl -X POST "https://minecraft.curseforge.com/api/projects/$CURSEFORGE_PROJECT_ID/upload-file" \
  -H "X-Api-Token: $CURSEFORGE_API_KEY" \
  -F 'metadata={
    "changelog": "Changelog here",
    "changelogType": "markdown",
    "displayName": "Real Economy VERSION-fabric",
    "gameVersions": [MC_VERSION_ID, 7499],
    "releaseType": "release"
  }' \
  -F "file=@output/realeconomy-VERSION-fabric.jar"

# Repeat for forge (7498) and neoforge (10150)
```

Replace `VERSION` with full version, `MC_VERSION_ID` with the numeric Minecraft version ID from the API.

## Porting to New Minecraft Versions

When porting to a new Minecraft version:

1. **Create a new branch** from the latest version branch (e.g., `git checkout -b 1.21.11`)

2. **Update `gradle.properties`**:
   - `minecraft_version`
   - `yarnVersion` (check https://meta.fabricmc.net/v2/versions/yarn)
   - `fabric_loader_version` (check https://fabricmc.net/)
   - `fabric_api_version` (check https://modrinth.com/mod/fabric-api)
   - `forge_version` (check https://files.minecraftforge.net/)
   - `neoforge_version` (check https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml)

3. **Update version ranges** in mod metadata files:
   - `fabric/src/main/resources/fabric.mod.json` - update `fabricloader` and `minecraft`
   - `forge/src/main/resources/META-INF/mods.toml` - update `loaderVersion` and `versionRange` for forge/minecraft
   - `neoforge/src/main/resources/META-INF/neoforge.mods.toml` - update `versionRange` for neoforge/minecraft

4. **Check for API breaking changes**. Known changes:
   - **1.21.10+**: `GameProfile` is now a Java record, use `profile.name()` instead of `profile.getName()`

5. **Build and test**: `./gradlew build`

## Commit/PR Guidelines

- Do not mention Claude Code or AI assistance in commit messages or PR descriptions
- Always use `git push --force-with-lease` instead of `git push --force`
