# Rovian Client

A Minecraft Java Edition launcher and client. Tauri 2 (Rust) backend, React + TypeScript frontend.

> **Status: early foundation.** It is not yet a working launcher: sign-in and game launching are not implemented, and the PLAY button is intentionally disabled until they are. Nothing in the UI simulates a feature that doesn't exist.

## What works today

- Tauri 2 desktop shell with sidebar navigation and the Rovian visual theme.
- Jems wallet and shop. Balance and purchases are stored **on this device only** (browser storage); they are not synced to an account or server.
- Optional live cape catalog: set `VITE_ROVIAN_API_URL` and the Shop page also lists staff-uploaded capes from the Rovian bot API.
- Discord page that clearly states OAuth is not wired up yet.
- Roadmap placeholders for sections that are not built.

## What is not built

Microsoft sign-in, game/asset/library downloads, Java runtime management, Fabric install, launching, instances, mod manager, Modrinth/CurseForge, the in-game Fabric mod, cosmetics visible to other players, Discord OAuth and rich presence, and account-backed purchases. See [docs/ROADMAP.md](docs/ROADMAP.md).

## Getting started

Prerequisites: Node 20+, Rust stable, and the [Tauri 2 system dependencies](https://tauri.app/start/prerequisites/) for your OS.

```bash
npm install
npx tauri icon public/rovian-mark.svg   # once: generates src-tauri/icons/
npm run tauri:dev                       # desktop app with hot reload
npm run tauri:build                     # installers
```

On Windows, `run-windows.bat` checks for Node and Rust, installs dependencies, generates icons if missing, and starts the app.

Other scripts: `npm run typecheck` (TypeScript only) and `npm run dev` (frontend in a browser, without the Tauri shell).

## Configuration

Copy `.env.example` to `.env` to configure the build.

| Variable              | Purpose                                                                                                                                         |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- |
| `VITE_ROVIAN_API_URL` | Public HTTPS base URL of the Rovian bot API. When set, the Shop loads capes from `/api/shop/items`. Leave empty to use only the built-in items. |

The window's Content Security Policy in `src-tauri/tauri.conf.json` allows HTTPS connections and images so the catalog can load. If you know the API host, narrow `connect-src` and `img-src` to it.

## Project layout

```
src/
  App.tsx          app shell and page switching
  pages/           one component per page (Home, Wallet, Shop, Discord, Roadmap)
  components/      Sidebar, Topbar, Notice
  hooks/           useWallet (local Jems wallet), useCapeItems (live catalog)
  lib/             api.ts (Tauri IPC + shop API), storage.ts (safe localStorage)
  data.ts          page list and built-in shop items
  styles.css       styles
src-tauri/         Rust backend (commands in src/lib.rs), Tauri config and capabilities
.github/workflows/ CI builds for Windows, Linux and macOS
```

## Notes

- `public/rovian-mark.svg` is a hand-drawn vector approximation of the Rovian logo. Replace it with the original vector if you have one.
- Inspired by OneClient's feature set; no OneClient code, assets or branding are used.
- Cosmetics must not be described as visible to other players until a backend exists. Nametags require an opt-in presence service, which will be designed in a later phase.

## License

MIT. See [LICENSE](LICENSE).
