/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** Public HTTPS base URL of the Rovian bot API. Optional. */
  readonly VITE_ROVIAN_API_URL?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
