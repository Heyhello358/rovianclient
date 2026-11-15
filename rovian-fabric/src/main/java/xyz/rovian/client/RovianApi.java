package xyz.rovian.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class RovianApi {
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static String baseUrl = "https://YOUR-RAILWAY-DOMAIN";
    private static volatile String status = "Set your bot's public Railway URL.";
    private static volatile int jems = 0;
    private static volatile boolean linked = false;
    private static volatile JsonArray items = new JsonArray();

    private RovianApi() {}

    public static String baseUrl() { return baseUrl; }
    public static void setBaseUrl(String value) {
        String v = value == null ? "" : value.trim().replaceAll("/+$", "");
        if (v.startsWith("https://") || v.startsWith("http://")) baseUrl = v;
    }
    public static String status() { return status; }
    public static int jems() { return jems; }
    public static boolean linked() { return linked; }
    public static JsonArray items() { return items.deepCopy(); }

    private static UUID playerUuid() {
        return Minecraft.getInstance().getUser().getProfileId();
    }
    private static String playerName() {
        return Minecraft.getInstance().getUser().getName();
    }
    private static String uuid() {
        UUID id = playerUuid();
        return id == null ? "" : id.toString();
    }
    private static URI uri(String path) {
        return URI.create(baseUrl + path);
    }
    private static CompletableFuture<String> request(String method, String path, String body) {
        if (baseUrl.contains("YOUR-RAILWAY-DOMAIN")) {
            status = "Enter your Railway public domain first.";
            return CompletableFuture.failedFuture(new IllegalStateException(status));
        }
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(uri(path))
                    .header("Content-Type", "application/json");
            if ("GET".equals(method)) builder.GET();
            else builder.POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
            return HTTP.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() < 200 || response.statusCode() >= 300)
                            throw new IllegalStateException("API error " + response.statusCode() + ": " + response.body());
                        return response.body();
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public static void refresh() {
        String id = uuid();
        if (id.isBlank()) { status = "Minecraft profile UUID is unavailable."; return; }
        request("GET", "/api/balance/" + id, "").thenAccept(body -> {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            linked = json.has("linked") && json.get("linked").getAsBoolean();
            jems = json.has("jems") ? json.get("jems").getAsInt() : 0;
            status = linked ? "Discord linked. Balance updated." : "Discord not linked yet.";
        }).exceptionally(e -> { status = "Connection failed: " + rootMessage(e); return null; });
        request("GET", "/api/shop/items", "").thenAccept(body -> {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            items = json.has("items") ? json.getAsJsonArray("items") : new JsonArray();
            if (!items.isEmpty()) Minecraft.getInstance().execute(() -> {
                if (Minecraft.getInstance().screen instanceof RovianMenuScreen screen) screen.reloadShop();
            });
        }).exceptionally(e -> { status = "Shop load failed: " + rootMessage(e); return null; });
    }

    public static void startLink() {
        JsonObject body = new JsonObject();
        body.addProperty("minecraftUuid", uuid());
        body.addProperty("minecraftName", playerName());
        request("POST", "/api/link/start", body.toString()).thenAccept(response -> {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            status = "Run /link " + json.get("code").getAsString() + " in your Discord server within 10 minutes.";
        }).exceptionally(e -> { status = "Link failed: " + rootMessage(e); return null; });
    }

    public static void purchase(String itemId) {
        JsonObject body = new JsonObject();
        body.addProperty("uuid", uuid());
        body.addProperty("itemId", itemId);
        request("POST", "/api/shop/purchase/request", body.toString()).thenAccept(response -> {
            status = "Purchase confirmation sent to your linked Discord DMs.";
        }).exceptionally(e -> { status = "Purchase request failed: " + rootMessage(e); return null; });
    }

    private static String rootMessage(Throwable error) {
        Throwable e = error;
        while (e.getCause() != null) e = e.getCause();
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }
}
