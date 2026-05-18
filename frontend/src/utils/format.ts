import { format, parseISO } from 'date-fns';
import { es } from 'date-fns/locale';

export function formatDate(iso: string | null | undefined): string {
  if (!iso) return '-';
  try {
    return format(parseISO(iso), "dd/MM/yyyy", { locale: es });
  } catch {
    return iso;
  }
}

export function formatDateTime(iso: string | null | undefined): string {
  if (!iso) return '-';
  try {
    return format(parseISO(iso), "dd/MM/yyyy HH:mm", { locale: es });
  } catch {
    return iso;
  }
}

export function formatCurrency(value: number | null | undefined): string {
  if (value == null) return '$0.00';
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(value);
}
