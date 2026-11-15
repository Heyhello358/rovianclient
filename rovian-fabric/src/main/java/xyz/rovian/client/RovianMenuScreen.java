package xyz.rovian.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
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
        int y = height / 2 + 4;

        addRenderableWidget(Button.builder(Component.literal("MINECRAFT OPTIONS"), button ->
                minecraft.setScreen(new OptionsScreen(this, minecraft.options)))
                .bounds(x, y, buttonWidth, 28).build());

        addRenderableWidget(Button.builder(Component.literal("BACK"), button ->
                minecraft.setScreen(parent))
                .bounds(x, y + 38, buttonWidth, 28).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, width, height, 0xF0181B22, 0xF008090D);
        graphics.drawCenteredString(font, Component.literal("ROVIAN CLIENT"), width / 2,
                height / 2 - 66, 0xFF39C5F2);
        graphics.drawCenteredString(font, Component.literal("QUICK MENU"), width / 2,
                height / 2 - 46, 0xFFFFFFFF);
        graphics.drawCenteredString(font, Component.literal("Right Shift opens this menu"),
                width / 2, height / 2 - 27, 0xFFB7C2CE);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
