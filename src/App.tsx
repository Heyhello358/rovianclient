import { useCallback, useEffect, useMemo, useState } from "react";
import { Notice } from "./components/Notice";
import { Sidebar } from "./components/Sidebar";
import { Topbar } from "./components/Topbar";
import { LOCAL_SHOP_ITEMS, PAGES } from "./data";
import { useCapeItems } from "./hooks/useCapeItems";
import { useWallet } from "./hooks/useWallet";
import { getAppVersion } from "./lib/api";
import { readString } from "./lib/storage";
import { Discord } from "./pages/Discord";
import { Home } from "./pages/Home";
import { Roadmap } from "./pages/Roadmap";
import { Shop } from "./pages/Shop";
import { Wallet } from "./pages/Wallet";
import type { PageId } from "./types";

function App() {
  const [page, setPage] = useState<PageId>("Home");
  const [appVersion, setAppVersion] = useState("Loading…");
  const [notice, setNotice] = useState("");
  const [discordLinked] = useState(() => readString("rovian.discord.linked") === "true");

  const { jems, owned, buy } = useWallet();
  const capeItems = useCapeItems(setNotice);
  const shopItems = useMemo(() => [...LOCAL_SHOP_ITEMS, ...capeItems], [capeItems]);
  const pageInfo = PAGES.find(p => p.id === page) ?? PAGES[0];

  useEffect(() => {
    getAppVersion().then(setAppVersion);
  }, []);

  const navigate = useCallback((next: PageId) => {
    setPage(next);
    setNotice("");
  }, []);

  const startDiscordLink = () =>
    setNotice(
      "Discord OAuth is not configured yet. Add the Discord application Client ID and a secure backend callback before linking can work. No account has been linked and no reward has been granted.",
    );

  return (
    <div className="shell">
      <Sidebar page={page} appVersion={appVersion} onNavigate={navigate} />
      <main className="main">
        <Topbar page={page} jems={jems} onOpenWallet={() => navigate("Jems")} />
        <section className="content">
          <div className="eyebrow">MINECRAFT JAVA EDITION</div>
          <h1>{page === "Home" ? "Welcome to Rovian." : page}</h1>
          <p className="subtitle">{pageInfo.description}</p>

          {page === "Home" ? (
            <Home jems={jems} discordLinked={discordLinked} onNavigate={navigate} />
          ) : page === "Jems" ? (
            <Wallet jems={jems} onNavigate={navigate} />
          ) : page === "Shop" ? (
            <Shop
              items={shopItems}
              jems={jems}
              owned={owned}
              onBuy={item => setNotice(buy(item))}
            />
          ) : page === "Discord" ? (
            <Discord linked={discordLinked} onLink={startDiscordLink} />
          ) : (
            <Roadmap title={page} description={pageInfo.description} />
          )}

          {notice && <Notice message={notice} onDismiss={() => setNotice("")} />}
          <footer>
            <span>© ROVIAN CLIENT</span>
            <span>Desktop shell ready</span>
          </footer>
        </section>
      </main>
    </div>
  );
}

export default App;
