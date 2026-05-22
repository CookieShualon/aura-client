# Agent Instructions

## Project Overview

Aura is a client-side Fabric mod for Minecraft 1.21.1. It targets Java 21 and uses Gradle with Fabric Loom. The mod ID is `aura`.

Keep the project focused on legitimate client-side quality-of-life features: HUD widgets, visual toggles, zoom, GUI improvements, and configuration. Do not add telemetry, ads, remote code loading, account handling, or gameplay automation.

## Common Commands

Build the project:

```sh
./gradlew build
```

Run a development Minecraft client:

```sh
./gradlew runClient
```

The installable mod jar is the remapped jar in `build/libs`, for example:

```text
build/libs/aura-0.1.0.jar
```

Do not use `*-sources.jar` or `*-dev.jar` as the release/download artifact.

## Repository Layout

```text
src/main/java/com/aura/client
  AuraClient.java              Client entrypoint and keybind setup
  AuraCommands.java            Client-side /aura commands
  config/                      JSON config load/save
  gui/                         ClickGUI and HUD editor screens
  hud/                         HUD module base and render callback
  input/                       CPS/input tracking
  mixin/                       Client rendering mixins
  module/                      Module registry, categories, settings
  module/impl/                 Built-in modules
src/main/resources
  fabric.mod.json              Fabric metadata
  aura.mixins.json             Mixin config
  assets/aura/lang/en_us.json  Keybind localization
```

## Coding Guidelines

- Follow the existing package structure under `com.aura.client`.
- Prefer small, focused modules over broad shared abstractions.
- Register new modules in `AuraClient#onInitializeClient`.
- HUD features should extend `HudModule`.
- Non-HUD features should extend `Module`.
- Settings should use the existing setting classes in `module/setting`.
- Persisted fields should flow through the existing config system rather than a second storage path.
- Keep mixins narrow and client-only.
- Avoid adding dependencies unless the feature clearly needs them.

## Validation

Before committing code changes, run:

```sh
./gradlew build
```

If a change affects GUI rendering or runtime behavior, also run:

```sh
./gradlew runClient
```

Then verify the relevant screen/module in a Minecraft client.

## GitHub Actions And Releases

CI is defined in `.github/workflows/build.yml`.

- Pushes to `main` and pull requests run the build job.
- The build job uploads the installable mod jar as the `aura-jars` artifact.
- Pushing a tag that starts with `v`, such as `v0.1.0`, also runs the release job.
- The release job downloads the `aura-jars` artifact from the same workflow run and attaches it to the GitHub Release.

Create a release with:

```sh
git tag v0.1.0
git push origin v0.1.0
```

## Files To Leave Alone Unless Needed

- Do not edit Gradle wrapper files unless updating the wrapper intentionally.
- Do not commit generated directories such as `build/`, `.gradle/`, or `run/`.
- Do not remove bundled font license files.
