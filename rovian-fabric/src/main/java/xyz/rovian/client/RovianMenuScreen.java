package xyz.rovian.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class RovianMenuScreen extends Screen {
    private final Screen parent;

    public RovianMenuScreen(Screen parent) {
        super(Component.literal("Rovian Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int buttonWidth = 220;
        int x = (width - buttonWidth) / 2;
        int y = height / 2 + 28;

        addRenderableWidget(Button.builder(Component.literal("HUD EDITOR — COMING SOON"), button -> {})
                .bounds(x, y, buttonWidth, 28).build());
        addRenderableWidget(Button.builder(Component.literal("COSMETICS — COMING SOON"), button -> {})
                .bounds(x, y + 37, buttonWidth, 28).build());
        addRenderableWidget(Button.builder(Component.literal("BACK"), button ->
                minecraft.setScreen(parent))
                .bounds(x, y + 74, buttonWidth, 28).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, width, height, 0xF0181B22, 0xF008090D);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 58, 0xFF39C5F2);
        graphics.drawCenteredString(font, Component.literal("RIGHT SHIFT MENU"), width / 2,
                height / 2 - 36, 0xFFB7C2CE);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
