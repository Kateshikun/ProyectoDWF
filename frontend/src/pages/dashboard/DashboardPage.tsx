import { useEffect, useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { vueloService } from '../../api/services/vuelo.service';
import { adminService } from '../../api/services/admin.service';
import { handleApiError } from '../../utils/errors';
import StatCard from '../../components/shared/StatCard';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import { Plane, BookOpen, DollarSign, TrendingUp } from 'lucide-react';
import type { VueloResponse } from '../../api/types';
import { formatDateTime, formatCurrency } from '../../utils/format';

export default function DashboardPage() {
  const { isAdmin, user } = useAuth();
  const [vuelos, setVuelos] = useState<VueloResponse[]>([]);
  const [ingresos, setIngresos] = useState<number | null>(null);
  const [reservasTotales, setReservasTotales] = useState<number | null>(null);
  const [demanda, setDemanda] = useState<Record<string, number> | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      setLoading(true);
      try {
        const vuelosRes = await vueloService.listar();
        setVuelos(vuelosRes.data);
      } catch (err) {
        handleApiError(err, 'Error al cargar vuelos');
      }

      if (isAdmin) {
        try {
          const [ingRes, resRes, demRes] = await Promise.all([
            adminService.ingresosTotales(),
            adminService.reservasTotales(),
            adminService.vuelosDemandados(),
          ]);
          setIngresos(ingRes.data);
          setReservasTotales(resRes.data);
          setDemanda(demRes.data);
        } catch (err) {
          handleApiError(err, 'Error al cargar reportes');
        }
      }
      setLoading(false);
    }
    load();
  }, [isAdmin]);

  const statusBadge = (estado: string | null) => {
    if (!estado) return <Badge variant="neutral">-</Badge>;
    const map: Record<string, 'success' | 'warning' | 'error'> = {
      Programado: 'success', Retrasado: 'warning', Cancelado: 'error',
    };
    return <Badge variant={map[estado] || 'neutral'}>{estado}</Badge>;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-slate-900">Bienvenido, {user?.username}</h2>
        <p className="text-slate-500 mt-1">Resumen general del sistema</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard label="Vuelos registrados" value={vuelos.length} icon={<Plane className="h-5 w-5" />} color="text-primary-600 bg-primary-50" />
        {isAdmin && ingresos !== null && (
          <StatCard label="Ingresos totales" value={formatCurrency(ingresos)} icon={<DollarSign className="h-5 w-5" />} color="text-success-600 bg-success-50" />
        )}
        {isAdmin && reservasTotales !== null && (
          <StatCard label="Reservaciones totales" value={reservasTotales} icon={<BookOpen className="h-5 w-5" />} color="text-warning-600 bg-warning-50" />
        )}
        {isAdmin && demanda !== null && (
          <StatCard label="Rutas con demanda" value={Object.keys(demanda).length} icon={<TrendingUp className="h-5 w-5" />} color="text-error-600 bg-error-50" />
        )}
      </div>

      {isAdmin && demanda && Object.keys(demanda).length > 0 && (
        <Card title="Rutas mas demandadas">
          <div className="space-y-3">
            {Object.entries(demanda).map(([ruta, cantidad]) => (
              <div key={ruta} className="flex items-center justify-between py-2 border-b border-slate-100 last:border-0">
                <span className="text-sm text-slate-700">{ruta}</span>
                <span className="text-sm font-semibold text-slate-900">{cantidad} reservas</span>
              </div>
            ))}
          </div>
        </Card>
      )}

      <Card title="Vuelos recientes">
        {vuelos.length === 0 ? (
          <p className="text-sm text-slate-500 py-4">No hay vuelos registrados</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="px-4 py-3 text-left font-semibold text-slate-600">Ruta</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-600">Salida</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-600">Estado</th>
                </tr>
              </thead>
              <tbody>
                {vuelos.slice(0, 5).map((v) => (
                  <tr key={v.id_vuelo} className="border-b border-slate-100 hover:bg-slate-50">
                    <td className="px-4 py-3 text-slate-700">{v.origen} - {v.destino}</td>
                    <td className="px-4 py-3 text-slate-700">{formatDateTime(v.fecha_salida)}</td>
                    <td className="px-4 py-3">{statusBadge(v.estado)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Card>
    </div>
  );
}
