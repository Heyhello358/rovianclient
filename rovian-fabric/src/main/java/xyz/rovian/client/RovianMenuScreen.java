package xyz.rovian.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public final class RovianMenuScreen extends Screen {
    private enum Page { HOME, PROFILE, SHOP, COSMETICS, SETTINGS }

    private final Screen parent;
    private Page page = Page.HOME;
    private EditBox apiUrl;

    private static final int BG = 0xFF090F18;
    private static final int SIDEBAR = 0xFF0C1420;
    private static final int PANEL = 0xFF121E2B;
    private static final int PANEL_SOFT = 0xFF172636;
    private static final int BORDER = 0xFF26394B;
    private static final int ACCENT = 0xFF55D6A6;
    private static final int ACCENT_DARK = 0xFF173D36;
    private static final int TEXT = 0xFFF2F6FA;
    private static final int MUTED = 0xFF91A5B9;
    private static final int WARNING = 0xFFFFC078;

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
        clearWidgets();
        init();
    }

    private int sidebarWidth() { return Math.max(132, Math.min(158, width / 4)); }
    private int contentLeft() { return sidebarWidth() + 22; }
    private int contentWidth() { return Math.max(180, width - contentLeft() - 22); }
    private int top() { return 76; }

    @Override
    protected void init() {
        int sw = sidebarWidth();
        int navW = sw - 24;
        int navY = 100;
        addRenderableWidget(nav("Overview", Page.HOME, 12, navY, navW));
        addRenderableWidget(nav("My account", Page.PROFILE, 12, navY + 36, navW));
        addRenderableWidget(nav("Jems store", Page.SHOP, 12, navY + 72, navW));
        addRenderableWidget(nav("Collection", Page.COSMETICS, 12, navY + 108, navW));
        addRenderableWidget(nav("Settings", Page.SETTINGS, 12, navY + 144, navW));

        int x = contentLeft();
        int w = contentWidth();

        if (page == Page.HOME) {
            addRenderableWidget(action("BROWSE STORE", b -> navigate(Page.SHOP), x + 18, 282, 142));
            addRenderableWidget(action("LINK DISCORD", b -> RovianApi.startLink(), x + 170, 282, 142));
            addRenderableWidget(action("REFRESH", b -> RovianApi.refresh(), width - 104, 18, 82));
        } else if (page == Page.PROFILE) {
            addRenderableWidget(action("START LINKING", b -> RovianApi.startLink(), x + 18, 250, 142));
            addRenderableWidget(action("REFRESH ACCOUNT", b -> RovianApi.refresh(), x + 170, 250, 150));
        } else if (page == Page.SHOP) {
            JsonArray items = RovianApi.items();
            int columns = w >= 560 ? 2 : 1;
            int cardW = (w - 36 - (columns - 1) * 12) / columns;
            for (int i = 0; i < Math.min(items.size(), 8); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String id = string(item, "id", "");
                int price = integer(item, "price", 0);
                int col = i % columns;
                int row = i / columns;
                int bx = x + 14 + col * (cardW + 12);
                int by = 174 + row * 82;
                addRenderableWidget(action("BUY  ·  " + price + " JEMS",
                        b -> RovianApi.purchase(id), bx + Math.max(8, cardW - 126), by + 43, 116));
            }
            addRenderableWidget(action("REFRESH", b -> RovianApi.refresh(), width - 104, 18, 82));
        } else if (page == Page.COSMETICS) {
            JsonArray owned = RovianApi.cosmetics();
            for (int i = 0; i < Math.min(owned.size(), 7); i++) {
                JsonObject item = owned.get(i).getAsJsonObject();
                String id = string(item, "id", "");
                int y = 146 + i * 39;
                addRenderableWidget(action("EQUIP", b -> RovianApi.equip(id), width - 126, y - 5, 104));
            }
            addRenderableWidget(action("OPEN STORE", b -> navigate(Page.SHOP), x + 18, height - 62, 132));
            addRenderableWidget(action("REFRESH", b -> RovianApi.refresh(), width - 104, 18, 82));
        } else if (page == Page.SETTINGS) {
            apiUrl = new EditBox(font, x + 18, 170, Math.max(120, Math.min(420, w - 36)), 22,
                    Component.literal("Public API URL"));
            apiUrl.setMaxLength(200);
            apiUrl.setValue(RovianApi.baseUrl());
            addRenderableWidget(apiUrl);
            addRenderableWidget(action("SAVE & CONNECT", b -> {
                RovianApi.setBaseUrl(apiUrl.getValue());
                RovianApi.refresh();
            }, x + 18, 210, 142));
            addRenderableWidget(action("MINECRAFT OPTIONS", b ->
                    minecraft.setScreen(new OptionsScreen(this, minecraft.options)),
                    x + 170, 210, 166));
        }

        addRenderableWidget(action("CLOSE", b -> minecraft.setScreen(parent), 12, height - 40, sw - 24));
        if (RovianApi.items().isEmpty()) RovianApi.refresh();
    }

    private Button nav(String label, Page target, int x, int y, int w) {
        String prefix = page == target ? "◆  " : "   ";
        return Button.builder(Component.literal(prefix + label), b -> navigate(target))
                .bounds(x, y, w, 29).build();
    }

    private Button action(String label, Button.OnPress press, int x, int y, int w) {
        return Button.builder(Component.literal(label), press).bounds(x, y, Math.max(76, w), 25).build();
    }

    private void panel(GuiGraphics g, int x, int y, int w, int h, int color) {
        if (w <= 0 || h <= 0) return;
        g.fill(x, y, x + w, y + h, color);
        g.fill(x, y, x + w, y + 1, BORDER);
        g.fill(x, y + h - 1, x + w, y + h, BORDER);
        g.fill(x, y, x + 1, y + h, BORDER);
        g.fill(x + w - 1, y, x + w, y + h, BORDER);
    }

    private void accentBar(GuiGraphics g, int x, int y, int w) {
        g.fill(x, y, x + w, y + 2, ACCENT);
    }

    private void text(GuiGraphics g, String value, int x, int y, int color) {
        g.drawString(font, Component.literal(value), x, y, color);
    }

    private void centered(GuiGraphics g, String value, int x, int y, int color) {
        g.drawCenteredString(font, Component.literal(value), x, y, color);
    }

    private String string(JsonObject object, String key, String fallback) {
        return object.has(key) && !object.get(key).isJsonNull() ? object.get(key).getAsString() : fallback;
    }

    private int integer(JsonObject object, String key, int fallback) {
        try { return object.has(key) ? object.get(key).getAsInt() : fallback; }
        catch (Exception ignored) { return fallback; }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fillGradient(0, 0, width, height, 0xFF111D2A, BG);

        int sw = sidebarWidth();
        g.fill(0, 0, sw, height, SIDEBAR);
        g.fill(sw - 1, 0, sw, height, BORDER);
        g.fill(14, 17, 39, 39, ACCENT_DARK);
        centered(g, "R", 26, 24, ACCENT);
        text(g, "ROVIAN", 46, 20, TEXT);
        text(g, "CLIENT  ·  1.21.11", 46, 33, MUTED);
        text(g, "WORKSPACE", 14, 77, 0xFF6E879D);

        int x = contentLeft();
        int w = contentWidth();
        text(g, pageTitle(), x, 22, TEXT);
        text(g, pageSubtitle(), x, 40, MUTED);
        text(g, "RIGHT SHIFT  ·  MENU", width - 151, 40, 0xFF71879B);
        g.fill(x, 59, x + w, 60, BORDER);
        accentBar(g, x, 59, Math.min(48, w));

        if (page == Page.HOME) {
            panel(g, x, 78, w, 104, PANEL);
            g.fill(x + 1, 79, x + 4, 181, ACCENT);
            text(g, "YOUR SPACE. YOUR STYLE.", x + 18, 95, ACCENT);
            text(g, "Welcome back, " + playerName(), x + 18, 119, TEXT);
            text(g, "Your Minecraft identity and Rovian account, together.", x + 18, 142, MUTED);
            text(g, RovianApi.linked() ? "DISCORD CONNECTED" : "DISCORD NOT CONNECTED",
                    x + 18, 162, RovianApi.linked() ? ACCENT : WARNING);

            int gap = 10;
            int cw = Math.max(54, (w - 20 - gap * 2) / 3);
            int sy = 196;
            statCard(g, x, sy, cw, "JEMS BALANCE", String.valueOf(RovianApi.jems()), ACCENT);
            statCard(g, x + cw + gap, sy, cw, "ACCOUNT", RovianApi.linked() ? "LINKED" : "GUEST", TEXT);
            statCard(g, x + (cw + gap) * 2, sy, cw, "STORE ITEMS",
                    String.valueOf(RovianApi.items().size()), TEXT);

            panel(g, x, 270, w, 68, PANEL_SOFT);
            text(g, "GETTING STARTED", x + 16, 283, MUTED);
            text(g, "Browse cosmetics, connect Discord, and manage your collection.", x + 16, 306, TEXT);
        } else if (page == Page.PROFILE) {
            panel(g, x, 82, w, 132, PANEL);
            text(g, "MINECRAFT PROFILE", x + 18, 98, ACCENT);
            text(g, playerName(), x + 18, 125, TEXT);
            UUID uuid = minecraft.getUser().getProfileId();
            text(g, "UUID  " + (uuid == null ? "Unavailable" : uuid.toString()), x + 18, 149, MUTED);
            g.fill(x + 18, 169, x + w - 18, 170, BORDER);
            text(g, "DISCORD STATUS", x + 18, 184, MUTED);
            text(g, RovianApi.linked() ? "Connected" : "Not connected",
                    x + 150, 184, RovianApi.linked() ? ACCENT : WARNING);
            panel(g, x, 230, w, 60, PANEL_SOFT);
            text(g, "Linking is completed in Discord after running /rovianlink", x + 16, 253, TEXT);
        } else if (page == Page.SHOP) {
            panel(g, x, 78, w, 72, PANEL);
            text(g, "JEMS STORE", x + 16, 91, ACCENT);
            text(g, "BALANCE  " + RovianApi.jems() + " JEMS", x + 16, 116, TEXT);
            text(g, "Purchases send a confirmation to your linked Discord account.", x + 16, 133, MUTED);
            JsonArray items = RovianApi.items();
            if (items.isEmpty()) {
                panel(g, x, 164, w, 82, PANEL_SOFT);
                centered(g, "THE STORE IS QUIET", x + w / 2, 187, TEXT);
                centered(g, "No items loaded. Check your connection and refresh.", x + w / 2, 207, MUTED);
            } else {
                int columns = w >= 560 ? 2 : 1;
                int cardW = (w - 36 - (columns - 1) * 12) / columns;
                for (int i = 0; i < Math.min(items.size(), 8); i++) {
                    JsonObject item = items.get(i).getAsJsonObject();
                    String name = string(item, "name", "Cosmetic");
                    int price = integer(item, "price", 0);
                    int col = i % columns;
                    int row = i / columns;
                    int cx = x + 14 + col * (cardW + 12);
                    int cy = 162 + row * 82;
                    panel(g, cx, cy, cardW, 72, PANEL_SOFT);
                    g.fill(cx + 1, cy + 1, cx + 3, cy + 71, ACCENT_DARK);
                    text(g, shorten(name, Math.max(12, cardW / 7)), cx + 12, cy + 12, TEXT);
                    text(g, price + " JEMS", cx + 12, cy + 34, ACCENT);
                    text(g, "COSMETIC", cx + 12, cy + 53, MUTED);
                }
            }
        } else if (page == Page.COSMETICS) {
            panel(g, x, 82, w, Math.max(170, height - 150), PANEL);
            text(g, "YOUR COLLECTION", x + 18, 99, ACCENT);
            JsonArray owned = RovianApi.cosmetics();
            if (owned.isEmpty()) {
                centered(g, RovianApi.linked() ? "Your collection is empty." : "Connect Discord to view your collection.",
                        x + w / 2, 155, TEXT);
                centered(g, "Cosmetics you own will appear here.", x + w / 2, 176, MUTED);
            } else {
                for (int i = 0; i < Math.min(owned.size(), 7); i++) {
                    JsonObject item = owned.get(i).getAsJsonObject();
                    String name = string(item, "name", "Cosmetic");
                    boolean equipped = integer(item, "equipped", 0) == 1;
                    int y = 137 + i * 39;
                    if (i % 2 == 0) g.fill(x + 10, y - 8, x + w - 10, y + 23, 0xFF142230);
                    text(g, shorten(name, 28), x + 18, y, TEXT);
                    text(g, equipped ? "EQUIPPED" : "OWNED", x + Math.max(140, w - 200), y,
                            equipped ? ACCENT : MUTED);
                }
            }
            text(g, "Cape visuals depend on client-side rendering support.", x + 18, height - 82, MUTED);
        } else if (page == Page.SETTINGS) {
            panel(g, x, 82, w, 154, PANEL);
            text(g, "SERVICE CONNECTION", x + 18, 99, ACCENT);
            text(g, "Public API address", x + 18, 140, TEXT);
            text(g, "Use your Railway public HTTPS domain.", x + 18, 156, MUTED);
            panel(g, x, 256, w, 68, PANEL_SOFT);
            text(g, "CONNECTION STATUS", x + 16, 269, MUTED);
            text(g, shorten(RovianApi.status(), 76), x + 16, 292, TEXT);
        }

        text(g, shorten(RovianApi.status(), 64), contentLeft(), height - 20, MUTED);
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void statCard(GuiGraphics g, int x, int y, int w, String label, String value, int valueColor) {
        panel(g, x, y, w, 62, PANEL_SOFT);
        text(g, label, x + 10, y + 11, MUTED);
        text(g, shorten(value, Math.max(5, w / 7)), x + 10, y + 34, valueColor);
    }

    private String pageTitle() {
        return switch (page) {
            case HOME -> "Overview";
            case PROFILE -> "My account";
            case SHOP -> "Jems store";
            case COSMETICS -> "Collection";
            case SETTINGS -> "Settings";
        };
    }

    private String pageSubtitle() {
        return switch (page) {
            case HOME -> "Everything you need, in one place.";
            case PROFILE -> "Your Minecraft identity and linked services.";
            case SHOP -> "Spend Jems on cosmetics for your account.";
            case COSMETICS -> "The items you've collected so far.";
            case SETTINGS -> "Manage your Rovian connection.";
        };
    }

    private String playerName() {
        return minecraft == null ? "Player" : minecraft.getUser().getName();
    }

    private String shorten(String value, int max) {
        if (value == null) return "";
        return value.length() <= max ? value : value.substring(0, Math.max(0, max - 3)) + "...";
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
