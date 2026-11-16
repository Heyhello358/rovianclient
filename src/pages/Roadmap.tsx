interface Props {
  title: string;
  description: string;
}

export function Roadmap({ title, description }: Props) {
  return (
    <div className="empty-state">
      <div className="empty-symbol" aria-hidden="true">
        ✧
      </div>
      <h2>{title} is on the roadmap</h2>
      <p>{description} This section will become functional in a future development phase.</p>
      <span className="tag">IN DEVELOPMENT</span>
    </div>
  );
}
