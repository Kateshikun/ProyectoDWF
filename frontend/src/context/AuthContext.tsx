import { createContext, useContext, useState, useCallback, type ReactNode } from 'react';
import type { AuthResponse } from '../api/types';
import { authService } from '../api/services/auth.service';
import { getStoredAuth, setStoredAuth, clearStoredAuth, type StoredAuth } from '../utils/auth';

interface AuthContextValue {
  user: StoredAuth | null;
  isAuthenticated: boolean;
  isAdmin: boolean;
  login: (username: string, password: string) => Promise<AuthResponse>;
  register: (data: { username: string; password: string; email: string; rol: string }) => Promise<AuthResponse>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<StoredAuth | null>(getStoredAuth);

  const login = useCallback(async (username: string, password: string) => {
    const { data } = await authService.login({ username, password });
    const stored: StoredAuth = { token: data.token, username: data.username, rol: data.rol };
    setStoredAuth(stored);
    setUser(stored);
    return data;
  }, []);

  const register = useCallback(async (dto: { username: string; password: string; email: string; rol: string }) => {
    const payload = { ...dto, activo: true };
    const { data } = await authService.register(payload);
    const stored: StoredAuth = { token: data.token, username: data.username, rol: data.rol };
    setStoredAuth(stored);
    setUser(stored);
    return data;
  }, []);

  const logout = useCallback(() => {
    clearStoredAuth();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, isAdmin: user?.rol === 'ADMIN', login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
