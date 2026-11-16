import type { PageInfo, ShopItem } from "./types";

export const PAGES: readonly PageInfo[] = [
  { id: "Home", icon: "⌂", description: "Your Minecraft setup, all in one place." },
  {
    id: "Instances",
    icon: "▦",
    description: "Separate game directories and configurations for each setup.",
  },
  { id: "Mods", icon: "◇", description: "Browse and manage compatible client-side mods." },
  { id: "Modpacks", icon: "▧", description: "Import and export modpack collections." },
  { id: "Cosmetics", icon: "✧", description: "Manage your Rovian appearance." },
  { id: "Jems", icon: "◆", description: "Your Rovian currency balance and transaction history." },
  { id: "Shop", icon: "🛍", description: "Spend Jems on profile cosmetics and customisation." },
  {
    id: "Discord",
    icon: "◉",
    description: "Connect Discord to your Rovian account and claim your linking reward.",
  },
  { id: "Settings", icon: "⚙", description: "Configure the launcher and game runtime." },
  { id: "Accounts", icon: "◎", description: "Manage your Minecraft accounts securely." },
];

export const DISCORD_REWARD_JEMS = 200;

export const LOCAL_SHOP_ITEMS: readonly ShopItem[] = [
  {
    id: "cyan-name",
    name: "Cyan Username",
    description: "A bright cyan name accent on your Rovian profile.",
    price: 250,
    icon: "✦",
  },
  {
    id: "blue-banner",
    name: "Blue Banner",
    description: "A clean blue profile banner.",
    price: 400,
    icon: "▰",
  },
  {
    id: "star-tag",
    name: "Star Tag",
    description: "A star-themed profile tag.",
    price: 600,
    icon: "★",
  },
];
