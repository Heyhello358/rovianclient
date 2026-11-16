export type PageId =
  | "Home"
  | "Instances"
  | "Mods"
  | "Modpacks"
  | "Cosmetics"
  | "Jems"
  | "Shop"
  | "Discord"
  | "Settings"
  | "Accounts";

export interface PageInfo {
  id: PageId;
  icon: string;
  description: string;
}

export interface ShopItem {
  id: string;
  name: string;
  description: string;
  price: number;
  icon: string;
  imageUrl?: string;
}
