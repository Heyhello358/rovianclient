# Rovian Client — Fabric Mod

A client-side Fabric mod for Minecraft **1.21.11**.

## Included features

- Custom dark Rovian title screen with Singleplayer, Multiplayer, Options and Quit.
- Right Shift opens the in-game Rovian quick menu.
- Quick menu has a working shortcut to Minecraft Options and a Back button.
- Uses the normal Minecraft font safely; no missing custom font resource is referenced.

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Install Fabric API for Minecraft 1.21.11.
3. Put `Rovian-Client-0.1.1.jar` in your Minecraft instance's `mods` folder.
4. Launch the Fabric 1.21.11 profile.

## Build

Requires Java 21.

```sh
gradle build
```

The mod JAR is written to `build/libs/Rovian-Client-0.1.1.jar`.

## Planned, not yet implemented

HUD editor, cosmetics, nametag/presence features, and custom Modak font support are not included in this build. They will not appear as fake or non-functional buttons.
