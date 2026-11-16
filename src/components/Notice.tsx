interface Props {
  message: string;
  onDismiss: () => void;
}

export function Notice({ message, onDismiss }: Props) {
  return (
    <div className="notice" role="status">
      {message}
      <button type="button" aria-label="Dismiss notification" onClick={onDismiss}>
        ×
      </button>
    </div>
  );
}
