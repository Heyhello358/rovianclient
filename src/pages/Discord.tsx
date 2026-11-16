import { DISCORD_REWARD_JEMS } from "../data";

interface Props {
  linked: boolean;
  onLink: () => void;
}

export function Discord({ linked, onLink }: Props) {
  return (
    <div className="connect-panel">
      <div className="discord-mark" aria-hidden="true">
        ◉
      </div>
      <h2>{linked ? "Discord connected" : "Connect your Discord"}</h2>
      <p>
        Link your Discord account to your Rovian profile. Verified linking grants a one-time bonus
        of <strong>{DISCORD_REWARD_JEMS} Jems</strong>.
      </p>
      <div className="connect-status">
        <span className={linked ? "status-dot" : "status-dot muted"} />
        {linked ? "Connected" : "Not connected"}
      </div>
      <button type="button" className="primary-button" onClick={onLink} disabled={linked}>
        {linked ? "LINKED" : "LINK DISCORD"}
      </button>
      <p className="fine-print">
        OAuth is not active yet because this repository has no Discord application credentials or
        secure callback service. This button will not pretend to link an account.
      </p>
    </div>
  );
}
