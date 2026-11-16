import { invoke } from "@tauri-apps/api/core";
import type { ShopItem } from "../types";

const API_BASE = import.meta.env.VITE_ROVIAN_API_URL?.trim().replace(/\/+$/, "") || "";

export const hasLiveShop = API_BASE !== "";

/** Version of the running app, or "Development" outside the Tauri shell. */
export async function getAppVersion(): Promise<string> {
  try {
    const info = await invoke<{ name: string; version: string }>("app_info");
    return info.version;
  } catch {
    return "Development";
  }
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

/** Only HTTPS image URLs are accepted from the remote catalog. */
function safeImageUrl(value: unknown): string | undefined {
  if (typeof value !== "string") return undefined;
  try {
    return new URL(value).protocol === "https:" ? value : undefined;
  } catch {
    return undefined;
  }
}

/** Cape items from the Rovian bot API. Malformed entries are skipped. */
export async function fetchCapeItems(signal?: AbortSignal): Promise<ShopItem[]> {
  if (!hasLiveShop) return [];
  const response = await fetch(`${API_BASE}/api/shop/items`, { signal });
  if (!response.ok) throw new Error(`Shop catalog request failed (${response.status})`);

  const data: unknown = await response.json();
  if (!isRecord(data) || !Array.isArray(data.items)) return [];

  const items: ShopItem[] = [];
  for (const raw of data.items) {
    if (!isRecord(raw) || raw.type !== "cape") continue;
    const { id, name, price } = raw;
    if (typeof id !== "string" || typeof name !== "string") continue;
    if (typeof price !== "number" || !Number.isFinite(price) || price < 0) continue;
    items.push({
      id,
      name,
      description: "Community cape uploaded by Rovian staff.",
      price,
      icon: "✦",
      imageUrl: safeImageUrl(raw.imageUrl),
    });
  }
  return items;
}
