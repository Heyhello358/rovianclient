package xyz.rovian.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;

public final class RovianTitleScreen extends Screen {
    public RovianTitleScreen() {
        super(Component.literal("Rovian Client"));
    }

    @Override
    protected void init() {
        int buttonWidth = Math.min(250, width - 40);
        int buttonHeight = 30;
        int gap = 9;
        int x = (width - buttonWidth) / 2;
        int firstY = height / 2 - 2;

        addRenderableWidget(Button.builder(Component.literal("SINGLEPLAYER"), button ->
                minecraft.setScreen(new SelectWorldScreen(this)))
                .bounds(x, firstY, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("MULTIPLAYER"), button ->
                minecraft.setScreen(new JoinMultiplayerScreen(this)))
                .bounds(x, firstY + buttonHeight + gap, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("OPTIONS"), button ->
                minecraft.setScreen(new OptionsScreen(this, minecraft.options)))
                .bounds(x, firstY + (buttonHeight + gap) * 2, buttonWidth, buttonHeight).build());

        addRenderableWidget(Button.builder(Component.literal("QUIT"), button ->
                minecraft.stop())
                .bounds(x, firstY + (buttonHeight + gap) * 3, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, width, height, 0xFF17191F, 0xFF08090D);

        int logoSize = 54;
        int centerX = width / 2;
        int logoY = Math.max(24, height / 2 - 170);

        graphics.fill(centerX - logoSize / 2, logoY, centerX + logoSize / 2,
                logoY + logoSize, 0xFF21B8E8);
        graphics.fill(centerX - 15, logoY + 11, centerX + 15, logoY + 19, 0xFF10151D);
        graphics.fill(centerX - 15, logoY + 11, centerX - 7, logoY + 43, 0xFF10151D);
        graphics.fill(centerX - 15, logoY + 36, centerX + 15, logoY + 44, 0xFF10151D);
        graphics.fill(centerX + 7, logoY + 28, centerX + 15, logoY + 44, 0xFF10151D);

        Component title = Component.literal("Rovian Client")
                .withStyle(style -> style.withFont(new FontDescription.Resource(RovianClient.MODAK_FONT)));
        graphics.drawCenteredString(font, title, centerX, logoY + logoSize + 12, 0xFFFFFFFF);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
