# Rovian Client — Fabric Mod

A client-side Fabric mod for Minecraft **1.21.11**.

## Included features

- Custom dark Rovian title screen.
- Right Shift opens the Rovian quick menu.
- Home, Minecraft profile, Jems shop, cosmetics information and settings pages.
- Jems balance and shop catalogue are loaded from the Rovian API.
- Discord purchase confirmation is sent to the linked Discord account.

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Install Fabric API for Minecraft 1.21.11.
3. Put the built Rovian client JAR in your instance's `mods` folder.
4. Launch the Fabric 1.21.11 profile.

## Secure Discord linking

The client intentionally does not create linking codes itself. The old public endpoint accepted a UUID supplied by the client, which could be impersonated.

A server administrator must install the separate **RovianLinkBridge** Paper plugin from the `rovian-link-bridge` folder:
1. Build/download the `Rovian-Link-Bridge-0.1.0` artifact from the GitHub Actions workflow.
2. Install it on a Paper 1.21.11 server running in online mode.
3. Set `api-url` in the plugin config to the Rovian public HTTPS API address.
4. Set `api-secret` to the same private value as the bot host's `API_SECRET`. Never put this secret in the client JAR or share it with players.
5. Restart the server.
6. Players run `/rovianlink` in-game, then `/link CODE` in the Discord server.

A client-only mod cannot prove Minecraft account ownership on its own. The bridge relies on the server's authenticated player UUID; do not use it on an offline-mode server.

## Build client

Requires Java 21.

```sh
gradle build
```

The client JAR is written to `build/libs/`.

## Not complete yet

- The actual Modak font file has not been added to the repository, so the UI still uses Minecraft's default font.
- The cape upload flow can add an image to the shop catalogue, but the client does not yet download, equip, or render custom capes on player models.
- The cosmetics page is informational until inventory/equipping and rendering are implemented.
