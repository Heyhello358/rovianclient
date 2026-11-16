import { PAGES } from "../data";
import type { PageId } from "../types";

interface Props {
  page: PageId;
  appVersion: string;
  onNavigate: (page: PageId) => void;
}

export function Sidebar({ page, appVersion, onNavigate }: Props) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-mark">R</div>
        <div>
          <strong>ROVIAN</strong>
          <span>CLIENT</span>
        </div>
      </div>
      <div className="nav-label">WORKSPACE</div>
      <nav aria-label="Main">
        {PAGES.map(item => (
          <button
            key={item.id}
            type="button"
            className={page === item.id ? "nav-item active" : "nav-item"}
            aria-current={page === item.id ? "page" : undefined}
            onClick={() => onNavigate(item.id)}
          >
            <span className="nav-icon" aria-hidden="true">
              {item.icon}
            </span>
            {item.id}
          </button>
        ))}
      </nav>
      <div className="sidebar-bottom">
        <span className="status-dot" /> Launcher foundation <small>v{appVersion}</small>
      </div>
    </aside>
  );
}
