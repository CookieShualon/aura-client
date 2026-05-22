# Contributing to Aura

Thanks for helping make Aura better. The project goal is a clean, trustworthy, client-side quality-of-life mod: no ads, no telemetry, no unfair gameplay automation.

## Development

```sh
./gradlew build
./gradlew runClient
```

Use Java 21. Keep changes scoped and prefer the existing module/settings patterns.

## Pull requests

- Explain the user-facing behavior change.
- Include screenshots or short clips for UI changes when possible.
- Keep unrelated refactors out of feature PRs.
- Run `./gradlew build` before opening the PR.

## Feature guidelines

Acceptable features are client-side quality-of-life, visual customization, HUD improvements, configuration, and accessibility/polish.

Avoid features that automate gameplay, bypass server rules, collect user data, inject ads, or behave like malware.
