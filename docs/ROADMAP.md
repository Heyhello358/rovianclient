# Roadmap

Rovian Client is an early foundation. This lists what is built and what is not, so the UI never promises more than the code does.

## Built

- Tauri 2 desktop shell, navigation and theme
- Local-only Jems wallet and shop
- Optional live cape catalog (`VITE_ROVIAN_API_URL`)

## Not built yet

**Accounts and launching**

- Microsoft sign-in and account storage
- Version, asset and library downloads
- Java runtime management
- Fabric install
- Launching the game

**Content**

- Instances
- Mod manager, Modrinth and CurseForge browsing
- Modpack import/export
- In-game Fabric mod (menu, HUD editor)

**Rovian services**

- Account-backed Jems wallet and purchases
- Cosmetics visible to other players
- Discord OAuth, linking reward and rich presence
- Nametag presence service (opt-in)

## Principles

- Privacy features (Discord rich presence, presence sharing) stay **off by default**.
- Anything stored only on this device says so in the UI.
- A feature is not shown as working until it works.
