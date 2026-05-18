import api from '../axios';
import type { VueloDto, VueloResponse } from '../types';

export const vueloService = {
  listar: () => api.get<VueloResponse[]>('/vuelos/'),
  buscar: (origen: string, destino: string, fecha: string) =>
    api.get<VueloResponse[]>('/vuelos/buscar', { params: { origen, destino, fecha } }),
  obtener: (id: number) => api.get<VueloResponse>(`/vuelos/${id}`),
  crear: (data: VueloDto) => api.post<VueloResponse>('/vuelos/', data),
  eliminar: (id: number) => api.delete<string>(`/vuelos/${id}`),
  actualizarEstado: (id: number, estado: string) =>
    api.patch<string>(`/vuelos/${id}/estado`, null, { params: { estado } }),
};
