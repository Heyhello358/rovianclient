import { useCallback, useMemo, useState } from "react";
import { DISCORD_REWARD_JEMS } from "../data";
import { readNumber, readStringArray, writeString } from "../lib/storage";
import type { ShopItem } from "../types";

const BALANCE_KEY = "rovian.jems.balance";
const PURCHASES_KEY = "rovian.jems.purchases";

/**
 * Device-local Jems wallet. Nothing here is synced to an account or server;
 * the UI says so wherever the wallet is shown.
 */
export function useWallet() {
  const [jems, setJems] = useState(() => readNumber(BALANCE_KEY, 0));
  const [purchases, setPurchases] = useState<string[]>(() => readStringArray(PURCHASES_KEY));
  const owned = useMemo(() => new Set(purchases), [purchases]);

  /** Attempts a purchase and returns the message to show the person. */
  const buy = useCallback(
    (item: ShopItem): string => {
      if (owned.has(item.id)) return "You already own this item.";
      if (jems < item.price) {
        return `Not enough Jems. Link Discord to claim the ${DISCORD_REWARD_JEMS} Jems reward, or earn Jems through future events.`;
      }
      const nextBalance = jems - item.price;
      const nextPurchases = [...purchases, item.id];
      setJems(nextBalance);
      setPurchases(nextPurchases);
      writeString(BALANCE_KEY, String(nextBalance));
      writeString(PURCHASES_KEY, JSON.stringify(nextPurchases));
      return `Purchased ${item.name}!`;
    },
    [jems, owned, purchases],
  );

  return { jems, owned, buy };
}
