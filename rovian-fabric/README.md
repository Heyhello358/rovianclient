# Rovian Client — Fabric Edition

Client-side Fabric mod for Minecraft **1.21.11**.

## Features in this first build

- Replaces the vanilla title screen with a dark Rovian screen.
- Adds working Singleplayer, Multiplayer, Options and Quit buttons.
- Press **Right Shift** in-game to open the Rovian quick menu.
- HUD editor and cosmetics are clearly marked as coming soon; they are not implemented yet.

## Build

Requires Java 21 and Gradle 8.14.3 or newer compatible with Loom 1.14.

```sh
gradle build
```

The mod JAR is written to `build/libs/rovian-client-0.1.0.jar`.

Install Fabric Loader for Minecraft 1.21.11, then place the JAR in the instance's `mods` folder. Fabric API is a dependency.
