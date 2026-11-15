import { useState } from "react";
import { invoke } from "@tauri-apps/api/core";

type Page = "Home" | "Instances" | "Mods" | "Modpacks" | "Cosmetics" | "Settings" | "Accounts";
const pages: Page[] = ["Home", "Instances", "Mods", "Modpacks", "Cosmetics", "Settings", "Accounts"];
const descriptions: Record<Page, string> = {
  Home: "Your Minecraft setup, all in one place.",
  Instances: "Separate game directories and configurations for each setup.",
  Mods: "Browse and manage compatible client-side mods.",
  Modpacks: "Import and export modpack collections.",
  Cosmetics: "Manage your Rovian appearance.",
  Settings: "Configure the launcher and game runtime.",
  Accounts: "Manage your Minecraft accounts securely.",
};
function App() {
  const [page, setPage] = useState<Page>("Home");
  const [status, setStatus] = useState("Desktop shell ready");
  const [appVersion, setAppVersion] = useState("Loading…");
  useState(() => {
    invoke<{ name: string; version: string }>("app_info")
      .then(info => setAppVersion(info.version))
      .catch(() => setAppVersion("Development"));
  });
  return <div className="shell">
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark">R</div><div><strong>ROVIAN</strong><span>CLIENT</span></div></div>
      <div className="nav-label">WORKSPACE</div>
      <nav>{pages.map((item, i) => <button key={item} className={page === item ? "nav-item active" : "nav-item"} onClick={() => setPage(item)}><span className="nav-icon">{["⌂","▦","◇","▧","✧","⚙","◉"][i]}</span>{item}</button>)}</nav>
      <div className="sidebar-bottom"><span className="status-dot" /> Launcher foundation <small>v{appVersion}</small></div>
    </aside>
    <main className="main">
      <header className="topbar"><div className="breadcrumb">ROVIAN <span>/</span> {page.toUpperCase()}</div><div className="profile"><div className="avatar">?</div><span>Not signed in</span></div></header>
      <section className="content">
        <div className="eyebrow">MINECRAFT JAVA EDITION</div>
        <h1>{page === "Home" ? "Welcome to Rovian." : page}</h1>
        <p className="subtitle">{descriptions[page]}</p>
        {page === "Home" ? <><div className="hero">
          <div className="hero-glow" /><div className="hero-copy"><div className="pill"><span className="status-dot" /> EARLY ACCESS FOUNDATION</div><h2>Your game.<br/><em>Your way.</em></h2><p>A clean home for your Minecraft instances, mods, and settings.</p><button className="play-button" disabled title="Game launching is not implemented yet">PLAY <span>→</span></button><div className="disabled-note">Launching will be enabled when authentication and game installation are implemented.</div></div>
          <div className="orbital"><div className="orbit one"/><div className="orbit two"/><div className="core">R</div></div>
        </div><div className="section-heading"><div><h3>Getting started</h3><p>The launcher foundation is in place. Core game features are next.</p></div></div>
        <div className="cards"><article className="info-card"><span className="card-icon">01</span><h4>Connect an account</h4><p>Microsoft sign-in is not implemented yet.</p><span className="tag">NOT AVAILABLE YET</span></article><article className="info-card"><span className="card-icon">02</span><h4>Create an instance</h4><p>Instance management is planned for a later phase.</p><span className="tag">NOT AVAILABLE YET</span></article><article className="info-card"><span className="card-icon">03</span><h4>Manage your mods</h4><p>Mod discovery and installation are not connected yet.</p><span className="tag">NOT AVAILABLE YET</span></article></div></> :
        <div className="empty-state"><div className="empty-symbol">✧</div><h2>{page} is on the roadmap</h2><p>{descriptions[page]} This section will become functional in a future development phase. No placeholder actions are presented as working features.</p><span className="tag">IN DEVELOPMENT</span></div>}
        <footer><span>© ROVIAN CLIENT</span><span>{status}</span></footer>
      </section>
    </main>
  </div>;
}
export default App;
