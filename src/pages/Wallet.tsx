import { DISCORD_REWARD_JEMS } from "../data";
import type { PageId } from "../types";

interface Props {
  jems: number;
  onNavigate: (page: PageId) => void;
}

export function Wallet({ jems, onNavigate }: Props) {
  return (
    <div className="wallet-panel">
      <div className="wallet-top">
        <div>
          <div className="eyebrow">YOUR WALLET</div>
          <div className="wallet-balance">
            ◆ {jems.toLocaleString()} <span>JEMS</span>
          </div>
          <p>Jems are Rovian's virtual currency for in-app cosmetics.</p>
        </div>
        <div className="wallet-gem" aria-hidden="true">
          ◆
        </div>
      </div>
      <div className="wallet-bottom">
        <div>
          <strong>Discord linking reward</strong>
          <p>
            Link your Discord account to receive {DISCORD_REWARD_JEMS} Jems once. Reward is granted
            only after the link is verified.
          </p>
        </div>
        <button type="button" className="secondary-button" onClick={() => onNavigate("Discord")}>
          Link Discord
        </button>
      </div>
      <p className="fine-print">
        Wallet is currently stored on this device. It is not synced to an account or server yet.
      </p>
    </div>
  );
}
