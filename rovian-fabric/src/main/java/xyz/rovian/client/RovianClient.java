package xyz.rovian.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public final class RovianClient implements ClientModInitializer {
    public static final String MOD_ID = "rovian";
    public static final ResourceLocation MODAK_FONT =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "modak");

    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rovian.open_menu",
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.screen instanceof RovianMenuScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new RovianMenuScreen(client.screen));
                }
            }
        });
    }

    public static Minecraft minecraft() {
        return Minecraft.getInstance();
    }
}
