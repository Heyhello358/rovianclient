import { useEffect, useState } from "react";
import { fetchCapeItems } from "../lib/api";
import type { ShopItem } from "../types";

/** Loads the live cape catalog once. `onError` is called if loading fails. */
export function useCapeItems(onError: (message: string) => void): ShopItem[] {
  const [items, setItems] = useState<ShopItem[]>([]);

  useEffect(() => {
    const controller = new AbortController();
    fetchCapeItems(controller.signal)
      .then(setItems)
      .catch(error => {
        if (controller.signal.aborted) return;
        console.warn(error);
        onError("Could not load the live shop catalog.");
      });
    return () => controller.abort();
    // Run once on mount; onError is intentionally not a dependency.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return items;
}
