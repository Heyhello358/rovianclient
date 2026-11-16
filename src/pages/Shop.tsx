import type { ShopItem } from "../types";

interface Props {
  items: readonly ShopItem[];
  jems: number;
  owned: ReadonlySet<string>;
  onBuy: (item: ShopItem) => void;
}

export function Shop({ items, jems, owned, onBuy }: Props) {
  return (
    <>
      <div className="shop-toolbar">
        <span>PROFILE CUSTOMISATION</span>
        <span className="balance-inline">◆ {jems.toLocaleString()} Jems</span>
      </div>
      <div className="shop-grid">
        {items.map(item => {
          const isOwned = owned.has(item.id);
          return (
            <article className="shop-card" key={item.id}>
              <div className="shop-art">
                {item.imageUrl ? <img src={item.imageUrl} alt={item.name} /> : item.icon}
              </div>
              <h3>{item.name}</h3>
              <p>{item.description}</p>
              <div className="shop-buy-row">
                <strong>◆ {item.price}</strong>
                <button type="button" disabled={isOwned} onClick={() => onBuy(item)}>
                  {isOwned ? "OWNED" : "BUY"}
                </button>
              </div>
            </article>
          );
        })}
      </div>
      <p className="fine-print">
        Purchases are currently local to this device. Online account sync and gifting are not
        connected yet.
      </p>
    </>
  );
}
