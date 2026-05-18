import api from '../axios';
import type { ReservacionDTO, ReservacionResponse } from '../types';

export const reservacionService = {
  crear: (data: ReservacionDTO) => api.post<ReservacionResponse>('/reservas/', data),
  obtener: (id: number) => api.get<ReservacionResponse>(`/reservas/${id}`),
  listar: () => api.get<ReservacionResponse[]>('/reservas/'),
  listarPorPasajero: (idPasajero: number) =>
    api.get<ReservacionResponse[]>(`/reservas/pasajero/${idPasajero}`),
  cancelar: (id: number) => api.delete<string>(`/reservas/${id}`),
  confirmar: (id: number) => api.post<string>(`/reservas/${id}/confirmar`),
};
