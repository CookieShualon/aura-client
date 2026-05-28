# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```sh
./gradlew build        # compile and produce build/libs/aura-0.1.0.jar
./gradlew runClient    # launch a dev Minecraft client
```

There is no test suite. Validation is `./gradlew build`. Runtime changes require `runClient` and manual inspection in-game.

## Stack

Fabric mod for Minecraft 1.21.1, Java 21, Gradle + Fabric Loom. Mod ID: `aura`. Entry point: `AuraClient implements ClientModInitializer`.

## Architecture

### Module system

All features are `Module` subclasses. `Module` stores id, name, description, category, enabled/expanded state, and an ordered list of `Setting<?>` values. The `tick(MinecraftClient)` hook runs every client tick when the module is enabled. `onEnable()`/`onDisable()` hooks fire on state change.

Two specializations exist:
- **`HudModule extends Module`** — positioned HUD overlay. Implements `render(DrawContext, MinecraftClient)`. Defaults category to `HUD` and enabled to `true`. Exposes `x()`, `y()`, `width()`, `height()` for the drag editor. Includes a built-in `shadow` `BooleanSetting`.
- **`OverlayModule` (interface)** — for non-HUD modules that still need a per-frame render pass (e.g. `CrosshairModule`). Implement `renderOverlay(DrawContext, MinecraftClient)` alongside `extends Module`.

`HudRenderer` registers a single `HudRenderCallback` that iterates all modules, dispatching `render` for `HudModule` instances and `renderOverlay` for `OverlayModule` instances.

### Settings

Only two setting types exist: `BooleanSetting` and `NumberSetting`. Both extend `Setting<T>`. `NumberSetting` carries min, max, step, and a `setFromRatio(double)` helper used by the GUI slider.

Settings are registered in the module constructor via `addSetting(...)`. Serialization is automatic through the config system.

### Config persistence

`AuraConfig` is a GSON-serialized POJO at `.minecraft/config/aura.json`. It holds a `GuiState` (ClickGUI window position/size) and a `Map<String, ModuleState>` keyed by module id. `ModuleState` stores `enabled`, `expanded`, `hudX`, `hudY`, and a `Map<String, Object> settings` for all setting values.

`ModuleRegistry.load()` is called once at startup; `ModuleRegistry.save()` is called on every toggle, setting change, and screen close. Do not introduce a second storage path.

### Module registration

All modules are instantiated and registered in `AuraClient#onInitializeClient` via `modules.register(...)`. Newly added modules must be added there. `ModuleRegistry` groups modules by `Category` (currently `HUD` and `VISUAL`) for the ClickGUI sidebar.

### ClickGUI

`AuraClickGuiScreen` renders the full GUI using vanilla `DrawContext` primitives and four bundled Inter font variants (`inter_title`, `inter_ui`, `inter_body`, `inter_tiny`) identified by `Identifier("aura", "inter_*")`. The window position/size is kept in `AuraConfig.GuiState` and auto-fitted to screen bounds on every render frame. Scrolling is per-category and tracked in `scrollOffsets`.

### Mixins

Two narrow client-only mixins exist in `com.aura.client.mixin`:
- `InGameHudMixin` — cancels vanilla crosshair rendering when `CrosshairModule` is enabled.
- `LightmapTextureManagerMixin` — overwrites the lightmap texture after each update when `FullbrightModule` is enabled.

New mixins must be declared in `aura.mixins.json` under `"client"`. Keep them narrow and client-only.

### InputTracker

`InputTracker` polls GLFW mouse state each client tick to count clicks within the last 1 000 ms. `leftCps()` and `rightCps()` are used by `CpsModule`.

## Adding a module

1. Create a class in `src/main/java/com/aura/client/module/impl/` extending `Module` (for non-HUD) or `HudModule` (for HUD). For modules that need a render pass but are not HUD widgets, also `implements OverlayModule`.
2. Call `addSetting(...)` in the constructor for any `BooleanSetting` or `NumberSetting` values.
3. Override `tick`, `onEnable`, `onDisable`, or `render`/`renderOverlay` as needed.
4. Register the module in `AuraClient#onInitializeClient`.

No other wiring is required — persistence, GUI rows, and HUD rendering are automatic.

## Releases

Pushing a `v*` tag triggers the CI release job, which attaches the remapped jar to a GitHub Release. The installable artifact is the non-`-sources`, non-`-dev` jar in `build/libs/`.

```sh
git tag v0.1.0
git push origin v0.1.0
```

## Constraints

- Client-side only. No server-side code, no telemetry, no remote loading, no gameplay automation.
- Do not add Gradle dependencies unless clearly necessary.
- Do not edit Gradle wrapper files or commit `build/`, `.gradle/`, or `run/`.
- Do not remove `src/main/resources/assets/aura/font/OFL.txt`.
