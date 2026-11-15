# Rovian Client

A Minecraft Java Edition launcher and client. Tauri 2 (Rust) backend, React + TypeScript frontend.

> **Status: early foundation (roadmap phases 1–2, part of 3).** It is not yet a working launcher: sign-in and game launching are not implemented, and the PLAY button is intentionally disabled until they are. Nothing in the UI simulates a feature that doesn't exist.

## What works in this commit

- Tauri 2 desktop shell, sidebar navigation, Rovian design system (`src/styles/tokens.css`).
- Real Mojang version manifest fetched in Rust (`src-tauri/src/minecraft/manifest.rs`), shown as a version picker on Home.
- Real Minecraft patch-notes feed on Home (`minecraft/news.rs`).
- JSON config with atomic writes (`config.rs`); selected version persists. Privacy features (Discord RPC, presence sharing) default to **off**.
- Honest roadmap pages for sections not built yet.

## What is NOT built

Microsoft auth, version/asset/library downloads, Java runtime management, Fabric install, launching, instances, mod manager, Modrinth/CurseForge, the in-game Fabric mod (menu, HUD editor), cosmetics, Discord RPC, and the nametag presence service. See `docs/ROADMAP.md`.

## Prerequisites

Node 20+, Rust stable, and the [Tauri 2 system dependencies](https://tauri.app/start/prerequisites/) for your OS.

## Run

```bash
npm install
npm run tauri:dev      # desktop app
npm run tauri:build    # installers
```

Icons: generate the bundle icons from the logo with `npm run tauri icon public/rovian-mark.svg` (writes `src-tauri/icons/`).

## Layout

```
src/            React UI (components, pages, lib/api.ts = typed IPC)
src-tauri/src/  Rust: commands.rs (IPC), config.rs, paths.rs, minecraft/*
docs/           roadmap, logo reference
```

## Notes

- `public/rovian-mark.svg` is a hand-drawn vector approximation of the supplied logo (`docs/logo-reference.jpg`). Replace it with the original vector if you have one.
- Inspired by OneClient's feature set; no OneClient code, assets or branding are used.
- Cosmetics must not be described as visible to other players until a backend exists. Nametags require an opt-in presence service, which will be designed in a later phase.
