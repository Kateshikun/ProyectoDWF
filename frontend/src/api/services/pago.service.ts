import api from '../axios';
import type { PagoDTO, PagoResponse } from '../types';

export const pagoService = {
  procesar: (data: PagoDTO) => api.post<string>('/pagos/procesar', data),
  consultarPorReserva: (idReservacion: number) =>
    api.get<PagoResponse>(`/pagos/reserva/${idReservacion}`),
  listar: () => api.get<PagoResponse[]>('/pagos/'),
};
