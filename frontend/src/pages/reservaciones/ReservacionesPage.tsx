import { useEffect, useState, type FormEvent } from 'react';
import { reservacionService } from '../../api/services/reservacion.service';
import { useApi } from '../../hooks/useApi';
import DataTable, { type Column } from '../../components/shared/DataTable';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Modal from '../../components/ui/Modal';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import ConfirmDialog from '../../components/shared/ConfirmDialog';
import EmptyState from '../../components/ui/EmptyState';
import { BookOpen, Plus, Circle as XCircle, CircleCheck as CheckCircle, Search } from 'lucide-react';
import type { ReservacionResponse, ReservacionDTO } from '../../api/types';
import { formatDateTime } from '../../utils/format';
import toast from 'react-hot-toast';

export default function ReservacionesPage() {
  const { loading, execute } = useApi();
  const [reservas, setReservas] = useState<ReservacionResponse[]>([]);
  const [showCreate, setShowCreate] = useState(false);
  const [cancelId, setCancelId] = useState<number | null>(null);
  const [pasajeroId, setPasajeroId] = useState('');
  const [searchMode, setSearchMode] = useState<'all' | 'pasajero'>('all');
  const [form, setForm] = useState<ReservacionDTO>({
    idVuelo: 0, idPasajero: 0, asientoAsignado: '', estadoReserva: 'Pendiente',
  });

  const loadAll = async () => {
    setSearchMode('all');
    const data = await execute(() => reservacionService.listar());
    if (data) setReservas(data);
  };

  const loadByPasajero = async () => {
    if (!pasajeroId) return;
    setSearchMode('pasajero');
    const data = await execute(() => reservacionService.listarPorPasajero(Number(pasajeroId)));
    if (data) setReservas(data);
  };

  useEffect(() => { loadAll(); }, []);

  const handleSearch = (e: FormEvent) => {
    e.preventDefault();
    if (pasajeroId) loadByPasajero();
    else loadAll();
  };

  const handleCreate = async (e: FormEvent) => {
    e.preventDefault();
    const data = await execute(() => reservacionService.crear(form));
    if (data) {
      toast.success('Reservacion creada');
      setShowCreate(false);
      setForm({ idVuelo: 0, idPasajero: 0, asientoAsignado: '', estadoReserva: 'Pendiente' });
      searchMode === 'pasajero' && pasajeroId ? loadByPasajero() : loadAll();
    }
  };

  const handleCancel = async () => {
    if (!cancelId) return;
    const ok = await execute(() => reservacionService.cancelar(cancelId));
    if (ok !== null) {
      toast.success('Reservacion cancelada');
      setCancelId(null);
      searchMode === 'pasajero' && pasajeroId ? loadByPasajero() : loadAll();
    }
  };

  const handleConfirm = async (id: number) => {
    const ok = await execute(() => reservacionService.confirmar(id));
    if (ok !== null) {
      toast.success('Reservacion confirmada');
      searchMode === 'pasajero' && pasajeroId ? loadByPasajero() : loadAll();
    }
  };

  const statusBadge = (estado: string | null) => {
    if (!estado) return <Badge variant="neutral">-</Badge>;
    const map: Record<string, 'success' | 'warning' | 'error'> = {
      Pendiente: 'warning', Confirmada: 'success', Cancelada: 'error',
    };
    return <Badge variant={map[estado] || 'neutral'}>{estado}</Badge>;
  };

  const columns: Column<ReservacionResponse>[] = [
    { key: 'codigo_vuelo', header: 'Codigo' },
    { key: 'ruta', header: 'Ruta', render: (r) => `${r.origen || '-'} - ${r.destino || '-'}` },
    { key: 'fecha_salida', header: 'Salida', render: (r) => formatDateTime(r.fecha_salida) },
    { key: 'pasajero', header: 'Pasajero', render: (r) => `${r.nombre_pasajero || ''} ${r.apellido_pasajero || ''}` },
    { key: 'asiento_asignado', header: 'Asiento' },
    { key: 'estado_reserva', header: 'Estado', render: (r) => statusBadge(r.estado_reserva) },
    {
      key: 'actions',
      header: 'Acciones',
      render: (r) => (
        <div className="flex items-center gap-2">
          {r.estado_reserva === 'Pendiente' && (
            <>
              <Button size="sm" variant="secondary" onClick={() => handleConfirm(r.id_reservacion)}>
                <CheckCircle className="h-3.5 w-3.5" /> Confirmar
              </Button>
              <button onClick={() => setCancelId(r.id_reservacion)} className="p-1.5 rounded-lg hover:bg-error-50 text-error-600 transition-colors">
                <XCircle className="h-4 w-4" />
              </button>
            </>
          )}
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-900">Reservaciones</h2>
        <Button onClick={() => setShowCreate(true)}><Plus className="h-4 w-4" /> Nueva reservacion</Button>
      </div>

      <Card>
        <form onSubmit={handleSearch} className="flex flex-wrap gap-3 items-end mb-6">
          <Input label="ID Pasajero" type="number" value={pasajeroId} onChange={(e) => setPasajeroId(e.target.value)} placeholder="Filtrar por pasajero" className="w-48" />
          <Button type="submit" variant="secondary" size="sm"><Search className="h-4 w-4" /> Buscar</Button>
          <Button variant="ghost" size="sm" onClick={loadAll}>Ver todas</Button>
        </form>

        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full" /></div>
        ) : reservas.length === 0 ? (
          <EmptyState icon={<BookOpen className="h-12 w-12" />} title="Sin reservaciones" description="No se encontraron reservaciones" />
        ) : (
          <DataTable columns={columns} data={reservas} keyExtractor={(r) => r.id_reservacion} />
        )}
      </Card>

      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="Crear reservacion">
        <form onSubmit={handleCreate} className="space-y-4">
          <Input label="ID Vuelo" type="number" value={form.idVuelo || ''} onChange={(e) => setForm((p) => ({ ...p, idVuelo: Number(e.target.value) }))} />
          <Input label="ID Pasajero" type="number" value={form.idPasajero || ''} onChange={(e) => setForm((p) => ({ ...p, idPasajero: Number(e.target.value) }))} />
          <Input label="Asiento" value={form.asientoAsignado} onChange={(e) => setForm((p) => ({ ...p, asientoAsignado: e.target.value }))} placeholder="Ej: 12A (numero + letra A-F)" />
          <Input label="Estado" value={form.estadoReserva} onChange={(e) => setForm((p) => ({ ...p, estadoReserva: e.target.value }))} placeholder="Pendiente" />
          <div className="flex justify-end gap-3 pt-4">
            <Button variant="secondary" onClick={() => setShowCreate(false)}>Cancelar</Button>
            <Button type="submit" loading={loading}>Crear</Button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={!!cancelId}
        onClose={() => setCancelId(null)}
        onConfirm={handleCancel}
        title="Cancelar reservacion"
        message="Esta seguro de cancelar esta reservacion? Solo se pueden cancelar reservaciones pendientes."
        confirmLabel="Cancelar reservacion"
        loading={loading}
      />
    </div>
  );
}
