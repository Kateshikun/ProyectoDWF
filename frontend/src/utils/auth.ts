export interface StoredAuth {
  token: string;
  username: string;
  rol: string;
}

const AUTH_KEY = 'sba_auth';

export function getStoredAuth(): StoredAuth | null {
  const raw = localStorage.getItem(AUTH_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as StoredAuth;
  } catch {
    clearStoredAuth();
    return null;
  }
}

export function setStoredAuth(auth: StoredAuth): void {
  localStorage.setItem(AUTH_KEY, JSON.stringify(auth));
}

export function clearStoredAuth(): void {
  localStorage.removeItem(AUTH_KEY);
}
