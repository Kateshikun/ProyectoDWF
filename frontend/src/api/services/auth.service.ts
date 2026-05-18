import api from '../axios';
import type { AuthRequest, UsuarioDTO, AuthResponse } from '../types';

export const authService = {
  login: (data: AuthRequest) => api.post<AuthResponse>('/auth/login', data),
  register: (data: UsuarioDTO) => api.post<AuthResponse>('/auth/registro', data),
};
