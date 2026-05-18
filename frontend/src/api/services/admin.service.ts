import api from '../axios';
import type { UsuarioDTO, UsuarioResponse } from '../types';

export const adminService = {
  ingresosTotales: () => api.get<number>('/admin/reportes/ingresos'),
  vuelosDemandados: () => api.get<Record<string, number>>('/admin/reportes/demanda'),
  reservasTotales: () => api.get<number>('/admin/reportes/reservas-totales'),
  listarUsuarios: () => api.get<UsuarioResponse[]>('/admin/usuarios'),
  obtenerUsuario: (id: number) => api.get<UsuarioResponse>(`/admin/usuarios/${id}`),
  cambiarEstadoUsuario: (id: number, activo: boolean) =>
    api.patch<string>(`/admin/usuarios/${id}/estado`, null, { params: { activo } }),
  eliminarUsuario: (id: number) => api.delete<string>(`/admin/usuarios/${id}`),
  actualizarUsuario: (id: number, data: UsuarioDTO) =>
    api.put<UsuarioResponse>(`/admin/usuarios/${id}`, data),
};
