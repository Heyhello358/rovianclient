import { useEffect, useMemo, useState } from "react";
import { invoke } from "@tauri-apps/api/core";

type Page = "Home" | "Instances" | "Mods" | "Modpacks" | "Cosmetics" | "Jems" | "Shop" | "Discord" | "Settings" | "Accounts";
const pages: Page[] = ["Home", "Instances", "Mods", "Modpacks", "Cosmetics", "Jems", "Shop", "Discord", "Settings", "Accounts"];
const descriptions: Record<Page, string> = {
  Home: "Your Minecraft setup, all in one place.",
  Instances: "Separate game directories and configurations for each setup.",
  Mods: "Browse and manage compatible client-side mods.",
  Modpacks: "Import and export modpack collections.",
  Cosmetics: "Manage your Rovian appearance.",
  Jems: "Your Rovian currency balance and transaction history.",
  Shop: "Spend Jems on profile cosmetics and customisation.",
  Discord: "Connect Discord to your Rovian account and claim your linking reward.",
  Settings: "Configure the launcher and game runtime.",
  Accounts: "Manage your Minecraft accounts securely.",
};
type ShopItem = { id: string; name: string; description: string; price: number; icon: string; imageUrl?: string };
const shopItems: ShopItem[] = [
  { id: "cyan-name", name: "Cyan Username", description: "A bright cyan name accent on your Rovian profile.", price: 250, icon: "✦" },
  { id: "blue-banner", name: "Blue Banner", description: "A clean blue profile banner.", price: 400, icon: "▰" },
  { id: "star-tag", name: "Star Tag", description: "A star-themed profile tag.", price: 600, icon: "★" },
];
const readNumber = (key: string, fallback: number) => {
  try { const n = Number(localStorage.getItem(key)); return Number.isFinite(n) && n >= 0 ? n : fallback; }
  catch { return fallback; }
};
function App() {
  const [page, setPage] = useState<Page>("Home");
  const [appVersion, setAppVersion] = useState("Loading…");
  const [jems, setJems] = useState(() => readNumber("rovian.jems.balance", 0));
  const [purchases, setPurchases] = useState<string[]>(() => {
    try { return JSON.parse(localStorage.getItem("rovian.jems.purchases") || "[]"); } catch { return []; }
  });
  const [notice, setNotice] = useState("");\n  const [capeItems, setCapeItems] = useState<ShopItem[]>([]);
  const [discordLinked, setDiscordLinked] = useState(false);
  useEffect(() => {
    invoke<{ name: string; version: string }>("app_info")
      .then(info => setAppVersion(info.version))
      .catch(() => setAppVersion("Development"));
    try { setDiscordLinked(localStorage.getItem("rovian.discord.linked") === "true"); } catch { /* storage unavailable */ }
  }, []);
  useEffect(() => {\n    const base = (import.meta as any).env?.VITE_ROVIAN_API_URL as string | undefined;\n    if (!base) return;\n    fetch(`${base.replace(/\\/$/, "")}/api/shop/items`).then(r => r.ok ? r.json() : Promise.reject()).then(data => {\n      if (Array.isArray(data.items)) setCapeItems(data.items.filter((x: any) => x.type === "cape").map((x: any) => ({ id: x.id, name: x.name, description: "Community cape uploaded by Rovian staff.", price: x.price, icon: "✦", imageUrl: x.imageUrl })));\n    }).catch(() => setNotice("Could not load the live shop catalog."));\n  }, []);\n  const ownedItems = useMemo(() => new Set(purchases), [purchases]);
  function buy(item: ShopItem) {
    if (ownedItems.has(item.id)) { setNotice("You already own this item."); return; }
    if (jems < item.price) { setNotice("Not enough Jems. Link Discord to claim the 200 Jems reward, or earn Jems through future events."); return; }
    const nextBalance = jems - item.price;
    const nextPurchases = [...purchases, item.id];
    setJems(nextBalance); setPurchases(nextPurchases);
    try {
      localStorage.setItem("rovian.jems.balance", String(nextBalance));
      localStorage.setItem("rovian.jems.purchases", JSON.stringify(nextPurchases));
    } catch { /* storage unavailable */ }
    setNotice(`Purchased ${item.name}!`);
  }
  function startDiscordLink() {
    setNotice("Discord OAuth is not configured yet. Add the Discord application Client ID and a secure backend callback before linking can work. No account has been linked and no reward has been granted.");
  }
  return <div className="shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark">R</div><div><strong>ROVIAN</strong><span>CLIENT</span></div></div>
      <div className="nav-label">WORKSPACE</div>
      <nav>{pages.map((item, i) => <button key={item} className={page === item ? "nav-item active" : "nav-item"} onClick={() => { setPage(item); setNotice(""); }}><span className="nav-icon">{["⌂","▦","◇","▧","✧","◆","🛍","◉","⚙","◎"][i]}</span>{item}</button>)}</nav>
      <div className="sidebar-bottom"><span className="status-dot" /> Launcher foundation <small>v{appVersion}</small></div>
    </aside>
    <main className="main">
      <header className="topbar"><div className="breadcrumb">ROVIAN <span>/</span> {page.toUpperCase()}</div><div className="topbar-right"><button className="jems-chip" onClick={() => setPage("Jems")}>◆ {jems.toLocaleString()} Jems</button><div className="profile"><div className="avatar">?</div><span>Not signed in</span></div></div></header>
      <section className="content">
        <div className="eyebrow">MINECRAFT JAVA EDITION</div>
        <h1>{page === "Home" ? "Welcome to Rovian." : page}</h1>
        <p className="subtitle">{descriptions[page]}</p>
        {page === "Home" ? <><div className="hero">
          <div className="hero-glow" /><div className="hero-copy"><div className="pill"><span className="status-dot" /> EARLY ACCESS FOUNDATION</div><h2>Your game.<br/><em>Your way.</em></h2><p>A clean home for your Minecraft instances, mods, Jems and customisation.</p><button className="play-button" disabled title="Game launching is not implemented yet">PLAY <span>→</span></button><div className="disabled-note">Launching is still in development.</div></div>
          <div className="orbital"><div className="orbit one"/><div className="orbit two"/><div className="core">R</div></div>
        </div><div className="section-heading"><div><h3>Jems & account</h3><p>Your currency and account connections.</p></div></div>
        <div className="cards"><article className="info-card"><span className="card-icon">◆ {jems.toLocaleString()}</span><h4>Jem balance</h4><p>Your current wallet balance.</p><button className="card-action" onClick={() => setPage("Jems")}>Open wallet →</button></article><article className="info-card"><span className="card-icon">02</span><h4>Jem Shop</h4><p>Browse profile cosmetics and spend Jems.</p><button className="card-action" onClick={() => setPage("Shop")}>Browse shop →</button></article><article className="info-card"><span className="card-icon">03</span><h4>Discord linking</h4><p>{discordLinked ? "Discord is linked." : "Connect Discord to claim the 200 Jems reward."}</p><button className="card-action" onClick={() => setPage("Discord")}>Open Discord →</button></article></div></> :
        page === "Jems" ? <div className="wallet-panel"><div className="wallet-top"><div><div className="eyebrow">YOUR WALLET</div><div className="wallet-balance">◆ {jems.toLocaleString()} <span>JEMS</span></div><p>Jems are Rovian's virtual currency for in-app cosmetics.</p></div><div className="wallet-gem">◆</div></div><div className="wallet-bottom"><div><strong>Discord linking reward</strong><p>Link your Discord account to receive 200 Jems once. Reward is granted only after the link is verified.</p></div><button className="secondary-button" onClick={() => setPage("Discord")}>Link Discord</button></div><p className="fine-print">Wallet is currently stored on this device. It is not synced to an account or server yet.</p></div> :
        page === "Shop" ? <><div className="shop-toolbar"><span>PROFILE CUSTOMISATION</span><span className="balance-inline">◆ {jems.toLocaleString()} Jems</span></div><div className="shop-grid">{[...shopItems, ...capeItems].map(item => <article className="shop-card" key={item.id}><div className="shop-art">{item.imageUrl ? <img src={item.imageUrl} alt={item.name} /> : item.icon}</div><h3>{item.name}</h3><p>{item.description}</p><div className="shop-buy-row"><strong>◆ {item.price}</strong><button disabled={ownedItems.has(item.id)} onClick={() => buy(item)}>{ownedItems.has(item.id) ? "OWNED" : "BUY"}</button></div></article>)}</div><p className="fine-print">Purchases are currently local to this device. Online account sync and gifting are not connected yet.</p></> :
        page === "Discord" ? <div className="connect-panel"><div className="discord-mark">◉</div><h2>{discordLinked ? "Discord connected" : "Connect your Discord"}</h2><p>Link your Discord account to your Rovian profile. Verified linking grants a one-time bonus of <strong>200 Jems</strong>.</p><div className="connect-status"><span className={discordLinked ? "status-dot" : "status-dot muted"} />{discordLinked ? "Connected" : "Not connected"}</div><button className="primary-button" onClick={startDiscordLink} disabled={discordLinked}>{discordLinked ? "LINKED" : "LINK DISCORD"}</button><p className="fine-print">OAuth is not active yet because this repository has no Discord application credentials or secure callback service. This button will not pretend to link an account.</p></div> :
        <div className="empty-state"><div className="empty-symbol">✧</div><h2>{page} is on the roadmap</h2><p>{descriptions[page]} This section will become functional in a future development phase.</p><span className="tag">IN DEVELOPMENT</span></div>}
        {notice && <div className="notice" role="status">{notice}<button onClick={() => setNotice("")}>×</button></div>}
        <footer><span>© ROVIAN CLIENT</span><span>Desktop shell ready</span></footer>
      </section>
    </main>
  </div>;
}
export default App;
