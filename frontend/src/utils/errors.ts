import type { AxiosError } from 'axios';
import type { ErrorResponse } from '../api/types';
import toast from 'react-hot-toast';

export function handleApiError(err: unknown, fallback = 'Error inesperado'): string {
  const axiosErr = err as AxiosError<ErrorResponse>;

  // Login/register return 401/400 with no body
  if (!axiosErr?.response?.data) {
    const status = axiosErr?.response?.status;
    if (status === 401) {
      toast.error('Credenciales invalidas');
      return 'Credenciales invalidas';
    }
    if (status === 400) {
      toast.error('Datos invalidos');
      return 'Datos invalidos';
    }
    if (status === 403) {
      toast.error('No tiene permisos para esta accion');
      return 'No tiene permisos para esta accion';
    }
    toast.error(fallback);
    return fallback;
  }

  const resp = axiosErr.response.data;

  if (resp.validationErrors) {
    const msgs = Object.values(resp.validationErrors).join('. ');
    toast.error(msgs);
    return msgs;
  }

  const msg = resp.message || fallback;
  toast.error(msg);
  return msg;
}
