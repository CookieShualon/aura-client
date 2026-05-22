# Aura

Aura is a client-side Fabric mod for Minecraft `1.21.1`. It is built as a clean, expandable base for legitimate quality-of-life features: HUD widgets, visual toggles, zoom, and a custom ClickGUI.

Aura is designed to be open-source and trustworthy: no ads, no telemetry, no remote code loading, and no gameplay automation.

## What is included now

- Fabric client-only mod scaffold with mod ID `aura`
- Java 21 Gradle/Loom build
- Module registry with categories and persistent module state
- JSON config at `.minecraft/config/aura.json`
- Custom ClickGUI opened with `Right Shift`
- Centered ClickGUI window
- Toggleable/expandable module rows
- Inline boolean and number setting support
- Scrollable module list with module descriptions
- HUD edit mode for dragging HUD modules
- Client commands:
  - `/aura gui`
  - `/aura hud`
- HUD render pipeline
- HUD modules:
  - FPS
  - Coordinates
  - Direction
  - Ping
  - CPS
  - Keystrokes
  - Potion timers
  - Armor durability
- Visual modules:
  - Fullbright
  - Custom crosshair overlay
  - Zoom, held with `C` by default

## Requirements

- Java 21 or newer
- Minecraft `1.21.1`
- Fabric Loader `0.18.2` or newer

This project includes a Gradle wrapper, so you do not need a separate Gradle install after the wrapper has been generated.

## Build

```sh
./gradlew build
```

The compiled mod jar will be written to:

```sh
build/libs/aura-0.1.0.jar
```

Use the remapped jar without `-sources`.

## Run a development client

```sh
./gradlew runClient
```

Once Minecraft opens, enter a world and press `Right Shift` to open Aura's ClickGUI. Press `Right Shift` again or `Escape` to close it.

Use the `HUD Edit` button in the ClickGUI title bar or run `/aura hud` to drag HUD modules around.

## Project layout

```text
src/main/java/com/aura/client
  AuraClient.java              Client entrypoint and keybind setup
  AuraCommands.java            Client-side /aura commands
  config/                      JSON config load/save
  gui/                         ClickGUI and HUD editor screens
  hud/                         HUD module base and render callback
  input/                       CPS/input tracking
  mixin/                       Focused client rendering mixins
  module/                      Module registry, categories, settings
  module/impl/                 Built-in starter modules
src/main/resources
  fabric.mod.json              Fabric metadata
  assets/aura/lang/en_us.json  Keybind localization
```

## Adding a module

Create a class that extends `Module` or `HudModule`, add settings with `addSetting(...)`, then register it in `AuraClient#onInitializeClient`.

HUD modules should extend `HudModule` and implement:

```java
public void render(DrawContext context, MinecraftClient client)
```

Non-HUD modules can override:

```java
public void tick(MinecraftClient client)
```

The registry automatically persists enabled state, expanded state, HUD position fields, and setting values.

## Project quality

- CI builds run through GitHub Actions.
- Contribution and security docs are included.
- Issue and PR templates are included.
- Generated Gradle, Fabric runtime, and IDE files are ignored by `.gitignore`.

## Font License

Aura bundles Inter from Google Fonts for the ClickGUI. Inter is licensed under the SIL Open Font License 1.1. The license text is included at `src/main/resources/assets/aura/font/OFL.txt`.

## Notes

Freelook and cosmetic capes are intentionally not included yet. They need mixins/camera and player-render integration, so they should be added as their own focused pass instead of being squeezed into the basic module layer.
