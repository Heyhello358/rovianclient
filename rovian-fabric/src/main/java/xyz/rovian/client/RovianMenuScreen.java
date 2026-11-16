package xyz.rovian.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public final class RovianMenuScreen extends Screen {
    private enum Page { HOME, PROFILE, SHOP, COSMETICS, SETTINGS }

    private final Screen parent;
    private Page page = Page.HOME;
    private EditBox apiUrl;

    private static final int BG = 0xFF0B1018;
    private static final int PANEL = 0xFF141D29;
    private static final int PANEL_ALT = 0xFF192535;
    private static final int BORDER = 0xFF29394B;
    private static final int ACCENT = 0xFF55D6A6;
    private static final int TEXT = 0xFFF0F5FA;
    private static final int MUTED = 0xFF9AAABD;

    public RovianMenuScreen(Screen parent) {
        super(Component.literal("Rovian Client"));
        this.parent = parent;
    }

    private void navigate(Page next) {
        page = next;
        clearWidgets();
        init();
    }

    public void reloadShop() {
        if (page == Page.SHOP || page == Page.HOME) {
            clearWidgets();
            init();
        }
    }

    private int contentLeft() { return 166; }
    private int contentWidth() { return Math.max(250, width - contentLeft() - 20); }

    @Override
    protected void init() {
        int sideWidth = 142;
        addRenderableWidget(nav("HOME", Page.HOME, 14, 78, sideWidth));
        addRenderableWidget(nav("PROFILE", Page.PROFILE, 14, 114, sideWidth));
        addRenderableWidget(nav("JEMS SHOP", Page.SHOP, 14, 150, sideWidth));
        addRenderableWidget(nav("COSMETICS", Page.COSMETICS, 14, 186, sideWidth));
        addRenderableWidget(nav("SETTINGS", Page.SETTINGS, 14, 222, sideWidth));

        if (page == Page.HOME) {
            addRenderableWidget(Button.builder(Component.literal("OPEN JEMS SHOP"), b -> navigate(Page.SHOP))
                    .bounds(contentLeft() + 18, 255, 160, 28).build());
            addRenderableWidget(Button.builder(Component.literal("LINK DISCORD"), b -> RovianApi.startLink())
                    .bounds(contentLeft() + 190, 255, 150, 28).build());
            addRenderableWidget(Button.builder(Component.literal("REFRESH"), b -> RovianApi.refresh())
                    .bounds(width - 112, 18, 92, 24).build());
        } else if (page == Page.PROFILE) {
            addRenderableWidget(Button.builder(Component.literal("LINK DISCORD"), b -> RovianApi.startLink())
                    .bounds(contentLeft() + 18, 220, 150, 28).build());
            addRenderableWidget(Button.builder(Component.literal("REFRESH ACCOUNT"), b -> RovianApi.refresh())
                    .bounds(contentLeft() + 180, 220, 160, 28).build());
        } else if (page == Page.SHOP) {
            JsonArray items = RovianApi.items();
            int shown = Math.min(items.size(), 8);
            int cardW = Math.min(330, (contentWidth() - 54) / 2);
            for (int i = 0; i < shown; i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String id = item.has("id") ? item.get("id").getAsString() : "";
                String name = item.has("name") ? item.get("name").getAsString() : "Cosmetic";
                int price = item.has("price") ? item.get("price").getAsInt() : 0;
                int col = i % 2;
                int row = i / 2;
                int x = contentLeft() + 18 + col * (cardW + 14);
                int y = 164 + row * 66;
                addRenderableWidget(Button.builder(Component.literal("BUY  •  " + price + " JEMS"),
                        b -> RovianApi.purchase(id)).bounds(x + cardW - 116, y + 31, 108, 24).build());
            }
            addRenderableWidget(Button.builder(Component.literal("REFRESH SHOP"), b -> RovianApi.refresh())
                    .bounds(width - 150, 18, 130, 24).build());
        } else if (page == Page.SETTINGS) {
            apiUrl = new EditBox(font, contentLeft() + 18, 166, Math.min(420, contentWidth() - 36), 22,
                    Component.literal("Public API URL"));
            apiUrl.setMaxLength(200);
            apiUrl.setValue(RovianApi.baseUrl());
            addRenderableWidget(apiUrl);
            addRenderableWidget(Button.builder(Component.literal("SAVE + CONNECT"), b -> {
                RovianApi.setBaseUrl(apiUrl.getValue());
                RovianApi.refresh();
            }).bounds(contentLeft() + 18, 202, 150, 28).build());
            addRenderableWidget(Button.builder(Component.literal("MINECRAFT OPTIONS"), b ->
                    minecraft.setScreen(new OptionsScreen(this, minecraft.options)))
                    .bounds(contentLeft() + 180, 202, 170, 28).build());
        } else if (page == Page.COSMETICS) {
            JsonArray owned = RovianApi.cosmetics();
            int shown = Math.min(owned.size(), 6);
            for (int i = 0; i < shown; i++) {
                JsonObject item = owned.get(i).getAsJsonObject();
                String id = item.get("id").getAsString();
                boolean equipped = item.has("equipped") && item.get("equipped").getAsInt() == 1;
                int y = 166 + i * 34;
                addRenderableWidget(Button.builder(Component.literal(equipped ? "EQUIPPED" : "EQUIP"),
                        button -> RovianApi.equip(id))
                        .bounds(width - 150, y, 112, 24).build());
            }
            addRenderableWidget(Button.builder(Component.literal("REFRESH INVENTORY"), b -> RovianApi.refresh())
                    .bounds(width - 178, 18, 158, 24).build());
            addRenderableWidget(Button.builder(Component.literal("VISIT JEMS SHOP"), b -> navigate(Page.SHOP))
                    .bounds(contentLeft() + 18, 225, 160, 28).build());
        }

        addRenderableWidget(Button.builder(Component.literal("CLOSE"), b -> minecraft.setScreen(parent))
                .bounds(14, height - 42, 124, 26).build());

        if (RovianApi.items().isEmpty()) RovianApi.refresh();
    }

    private Button nav(String label, Page target, int x, int y, int w) {
        String text = page == target ? "●  " + label : "    " + label;
        return Button.builder(Component.literal(text), b -> navigate(target))
                .bounds(x, y, w, 28).build();
    }

    private void panel(GuiGraphics g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + h, color);
        g.fill(x, y, x + w, y + 1, BORDER);
        g.fill(x, y + h - 1, x + w, y + h, BORDER);
        g.fill(x, y, x + 1, y + h, BORDER);
        g.fill(x + w - 1, y, x + w, y + h, BORDER);
    }

    private Component modak(String value) {
        return Component.literal(value).withStyle(style -> style.withFont(ResourceLocation.fromNamespaceAndPath("rovian", "default")));
    }

    private void text(GuiGraphics g, String value, int x, int y, int color) {
        g.drawString(font, modak(value), x, y, color);
    }

    private void centered(GuiGraphics g, String value, int x, int y, int color) {
        g.drawCenteredString(font, modak(value), x, y, color);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fillGradient(0, 0, width, height, 0xFF111A25, BG);
        // Sidebar
        g.fill(0, 0, 154, height, 0xFF0E151F);
        g.fill(153, 0, 155, height, 0xFF263647);
        text(g, "ROVIAN", 17, 22, ACCENT);
        text(g, "CLIENT  /  1.21.11", 17, 42, MUTED);
        text(g, "MAIN MENU", 18, 64, 0xFF71869C);

        // Top bar
        text(g, page.name().replace('_', ' '), contentLeft() + 2, 24, TEXT);
        text(g, "A cleaner Minecraft experience", contentLeft() + 2, 42, MUTED);
        panel(g, contentLeft(), 60, contentWidth(), 1, ACCENT);

        int x = contentLeft();
        int w = contentWidth();
        if (page == Page.HOME) {
            panel(g, x, 78, w, 92, PANEL);
            text(g, "WELCOME BACK", x + 18, 94, MUTED);
            text(g, playerName(), x + 18, 116, TEXT);
            text(g, "Your Rovian profile and services at a glance.", x + 18, 140, MUTED);

            int gap = 12;
            int cardW = (w - 36 - gap * 2) / 3;
            panel(g, x, 184, cardW, 58, PANEL_ALT);
            panel(g, x + cardW + gap, 184, cardW, 58, PANEL_ALT);
            panel(g, x + (cardW + gap) * 2, 184, cardW, 58, PANEL_ALT);
            text(g, "JEMS BALANCE", x + 12, 194, MUTED);
            text(g, String.valueOf(RovianApi.jems()), x + 12, 214, ACCENT);
            text(g, "DISCORD", x + cardW + gap + 12, 194, MUTED);
            text(g, RovianApi.linked() ? "LINKED" : "NOT LINKED", x + cardW + gap + 12, 214, TEXT);
            text(g, "SHOP ITEMS", x + (cardW + gap) * 2 + 12, 194, MUTED);
            text(g, String.valueOf(RovianApi.items().size()), x + (cardW + gap) * 2 + 12, 214, TEXT);
            panel(g, x, 286, w, 76, PANEL);
            text(g, "QUICK ACTIONS", x + 18, 301, MUTED);
            text(g, "Open the shop to browse cosmetics or link Discord to access account features.", x + 18, 324, TEXT);
        } else if (page == Page.PROFILE) {
            panel(g, x, 82, w, 122, PANEL);
            text(g, "MINECRAFT ACCOUNT", x + 18, 98, MUTED);
            text(g, playerName(), x + 18, 124, TEXT);
            UUID uuid = minecraft.getUser().getProfileId();
            text(g, "UUID: " + (uuid == null ? "Unavailable" : uuid), x + 18, 150, MUTED);
            text(g, "DISCORD CONNECTION", x + 18, 178, MUTED);
            text(g, RovianApi.linked() ? "Connected" : "Not connected", x + 180, 178,
                    RovianApi.linked() ? ACCENT : 0xFFFFC078);
            panel(g, x, 250, w, 66, PANEL_ALT);
            text(g, "Account linking starts in-game and finishes in your Discord server.", x + 18, 274, TEXT);
        } else if (page == Page.SHOP) {
            panel(g, x, 78, w, 66, PANEL);
            text(g, "JEMS SHOP", x + 18, 91, ACCENT);
            text(g, "Balance: " + RovianApi.jems() + " Jems   •   Purchases require Discord confirmation.", x + 18, 115, TEXT);
            JsonArray items = RovianApi.items();
            if (items.isEmpty()) {
                panel(g, x, 158, w, 78, PANEL_ALT);
                centered(g, "No shop items loaded yet.", x + w / 2, 184, TEXT);
                centered(g, "Check the API connection or refresh the shop.", x + w / 2, 202, MUTED);
            } else {
                int cardW = Math.min(330, (w - 54) / 2);
                for (int i = 0; i < Math.min(items.size(), 8); i++) {
                    JsonObject item = items.get(i).getAsJsonObject();
                    String name = item.has("name") ? item.get("name").getAsString() : "Cosmetic";
                    int price = item.has("price") ? item.get("price").getAsInt() : 0;
                    int col = i % 2;
                    int row = i / 2;
                    int cx = x + 18 + col * (cardW + 14);
                    int cy = 158 + row * 66;
                    panel(g, cx, cy, cardW, 56, PANEL_ALT);
                    text(g, name, cx + 10, cy + 10, TEXT);
                    text(g, price + " Jems", cx + 10, cy + 30, ACCENT);
                }
            }
        } else if (page == Page.COSMETICS) {
            panel(g, x, 82, w, 310, PANEL);
            text(g, "YOUR COSMETICS", x + 18, 100, ACCENT);
            JsonArray owned = RovianApi.cosmetics();
            if (owned.isEmpty()) {
                text(g, RovianApi.linked() ? "No purchased cosmetics yet." : "Link Discord to load your inventory.", x + 18, 132, TEXT);
            } else {
                for (int i = 0; i < Math.min(owned.size(), 6); i++) {
                    JsonObject item = owned.get(i).getAsJsonObject();
                    String name = item.has("name") ? item.get("name").getAsString() : "Cosmetic";
                    boolean equipped = item.has("equipped") && item.get("equipped").getAsInt() == 1;
                    int y = 132 + i * 34;
                    text(g, name, x + 18, y, TEXT);
                    text(g, equipped ? "EQUIPPED" : "OWNED", x + 220, y, equipped ? ACCENT : MUTED);
                }
            }
            text(g, "Equipping is saved to your account. Cape rendering is not implemented yet.", x + 18, 360, MUTED);
        } else if (page == Page.SETTINGS) {
            panel(g, x, 82, w, 126, PANEL);
            text(g, "CONNECTION", x + 18, 100, ACCENT);
            text(g, "Rovian API public address", x + 18, 144, TEXT);
            text(g, "Use your Railway HTTPS domain. Do not use a .railway.internal address.", x + 18, 180, MUTED);
            panel(g, x, 242, w, 70, PANEL_ALT);
            text(g, "STATUS", x + 18, 254, MUTED);
            text(g, shorten(RovianApi.status(), 90), x + 18, 278, TEXT);
        }

        text(g, shorten(RovianApi.status(), 54), 164, height - 22, MUTED);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private String playerName() {
        return minecraft == null ? "Player" : minecraft.getUser().getName();
    }

    private String shorten(String value, int max) {
        if (value == null) return "";
        return value.length() <= max ? value : value.substring(0, max - 3) + "...";
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
