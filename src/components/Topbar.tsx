import type { PageId } from "../types";

interface Props {
  page: PageId;
  jems: number;
  onOpenWallet: () => void;
}

export function Topbar({ page, jems, onOpenWallet }: Props) {
  return (
    <header className="topbar">
      <div className="breadcrumb">
        ROVIAN <span>/</span> {page.toUpperCase()}
      </div>
      <div className="topbar-right">
        <button type="button" className="jems-chip" onClick={onOpenWallet}>
          ◆ {jems.toLocaleString()} Jems
        </button>
        <div className="profile">
          <div className="avatar">?</div>
          <span>Not signed in</span>
        </div>
      </div>
    </header>
  );
}
