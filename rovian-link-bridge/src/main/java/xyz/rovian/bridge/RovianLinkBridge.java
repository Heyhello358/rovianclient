package xyz.rovian.bridge;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class RovianLinkBridge extends JavaPlugin {
    private HttpClient http;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8)).build();
        getLogger().info("RovianLinkBridge enabled. Use /rovianlink in-game.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command must be run by a player in-game.");
            return true;
        }
        String base = getConfig().getString("api-url", "").replaceAll("/+$", "");
        String secret = getConfig().getString("api-secret", "");
        if (base.isBlank() || secret.isBlank() || secret.equals("CHANGE-ME")) {
            player.sendMessage("§cRovian linking is not configured by the server administrator.");
            return true;
        }

        UUID uuid = player.getUniqueId();
        String name = player.getName();
        JsonObject body = new JsonObject();
        body.addProperty("minecraftUuid", uuid.toString());
        body.addProperty("minecraftName", name);

        HttpRequest request = HttpRequest.newBuilder(URI.create(base + "/api/link/server-start"))
                .timeout(Duration.ofSeconds(12))
                .header("Content-Type", "application/json")
                .header("x-rovian-secret", secret)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        player.sendMessage("§aRequesting a secure Rovian link code...");
        CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() < 200 || response.statusCode() >= 300)
                    throw new IllegalStateException("Rovian API returned HTTP " + response.statusCode());
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return json.get("code").getAsString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((code, error) -> Bukkit.getScheduler().runTask(this, () -> {
            if (!player.isOnline()) return;
            if (error != null) {
                getLogger().warning("Could not create link code: " + error.getMessage());
                player.sendMessage("§cCould not reach Rovian services. Try again later.");
                return;
            }
            player.sendMessage("§aYour one-time Rovian code: §f" + code);
            player.sendMessage("§7Enter §f/link " + code + " §7in the Rovian Discord server within 10 minutes.");
            player.sendMessage("§8This code links only the Minecraft account authenticated by this server.");
        }));
        return true;
    }
}
