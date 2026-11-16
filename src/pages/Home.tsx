import { DISCORD_REWARD_JEMS } from "../data";
import type { PageId } from "../types";

interface Props {
  jems: number;
  discordLinked: boolean;
  onNavigate: (page: PageId) => void;
}

export function Home({ jems, discordLinked, onNavigate }: Props) {
  return (
    <>
      <div className="hero">
        <div className="hero-glow" />
        <div className="hero-copy">
          <div className="pill">
            <span className="status-dot" /> EARLY ACCESS FOUNDATION
          </div>
          <h2>
            Your game.
            <br />
            <em>Your way.</em>
          </h2>
          <p>A clean home for your Minecraft instances, mods, Jems and customisation.</p>
          <button
            type="button"
            className="play-button"
            disabled
            title="Game launching is not implemented yet"
          >
            PLAY <span>→</span>
          </button>
          <div className="disabled-note">Launching is still in development.</div>
        </div>
        <div className="orbital" aria-hidden="true">
          <div className="orbit one" />
          <div className="orbit two" />
          <div className="core">R</div>
        </div>
      </div>

      <div className="section-heading">
        <div>
          <h3>Jems &amp; account</h3>
          <p>Your currency and account connections.</p>
        </div>
      </div>
      <div className="cards">
        <article className="info-card">
          <span className="card-icon">◆ {jems.toLocaleString()}</span>
          <h4>Jem balance</h4>
          <p>Your current wallet balance.</p>
          <button type="button" className="card-action" onClick={() => onNavigate("Jems")}>
            Open wallet →
          </button>
        </article>
        <article className="info-card">
          <span className="card-icon">02</span>
          <h4>Jem Shop</h4>
          <p>Browse profile cosmetics and spend Jems.</p>
          <button type="button" className="card-action" onClick={() => onNavigate("Shop")}>
            Browse shop →
          </button>
        </article>
        <article className="info-card">
          <span className="card-icon">03</span>
          <h4>Discord linking</h4>
          <p>
            {discordLinked
              ? "Discord is linked."
              : `Connect Discord to claim the ${DISCORD_REWARD_JEMS} Jems reward.`}
          </p>
          <button type="button" className="card-action" onClick={() => onNavigate("Discord")}>
            Open Discord →
          </button>
        </article>
      </div>
    </>
  );
}
