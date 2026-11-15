package xyz.rovian.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;

public final class RovianMenuScreen extends Screen {
    private final Screen parent;
    private EditBox apiUrl;

    public RovianMenuScreen(Screen parent) {
        super(Component.literal("Rovian Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = width / 2;
        apiUrl = new EditBox(font, center - 150, 42, 300, 20, Component.literal("Bot API URL"));
        apiUrl.setMaxLength(200);
        apiUrl.setValue(RovianApi.baseUrl());
        addRenderableWidget(apiUrl);

        addRenderableWidget(Button.builder(Component.literal("SAVE URL + REFRESH"), b -> {
            RovianApi.setBaseUrl(apiUrl.getValue());
            RovianApi.refresh();
        }).bounds(center - 150, 68, 145, 24).build());

        addRenderableWidget(Button.builder(Component.literal("LINK DISCORD"), b -> {
            RovianApi.setBaseUrl(apiUrl.getValue());
            RovianApi.startLink();
        }).bounds(center + 5, 68, 145, 24).build());

        addRenderableWidget(Button.builder(Component.literal("REFRESH JEMS + SHOP"), b -> {
            RovianApi.setBaseUrl(apiUrl.getValue());
            RovianApi.refresh();
        }).bounds(center - 150, 98, 300, 24).build());

        JsonArray items = RovianApi.items();
        int shown = Math.min(items.size(), 5);
        for (int i = 0; i < shown; i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String id = item.get("id").getAsString();
            String name = item.get("name").getAsString();
            int price = item.get("price").getAsInt();
            int y = 134 + i * 29;
            addRenderableWidget(Button.builder(
                    Component.literal("BUY " + name + "  —  " + price + " Jems"),
                    b -> RovianApi.purchase(id)
            ).bounds(center - 150, y, 300, 25).build());
        }

        addRenderableWidget(Button.builder(Component.literal("MINECRAFT OPTIONS"), b ->
                minecraft.setScreen(new OptionsScreen(this, minecraft.options)))
                .bounds(center - 150, height - 64, 145, 26).build());

        addRenderableWidget(Button.builder(Component.literal("BACK"), b ->
                minecraft.setScreen(parent))
                .bounds(center + 5, height - 64, 145, 26).build());

        if (items.isEmpty()) RovianApi.refresh();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fillGradient(0, 0, width, height, 0xF0181B22, 0xF008090D);
        graphics.drawCenteredString(font, Component.literal("ROVIAN CLIENT"), width / 2,
                12, 0xFF39C5F2);
        graphics.drawCenteredString(font, Component.literal("Jems: " + RovianApi.jems()
                + (RovianApi.linked() ? "  •  Discord linked" : "  •  Discord not linked")),
                width / 2, 128, 0xFFFFFFFF);
        graphics.drawCenteredString(font, Component.literal(RovianApi.status()),
                width / 2, height - 88, 0xFFB7C2CE);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
